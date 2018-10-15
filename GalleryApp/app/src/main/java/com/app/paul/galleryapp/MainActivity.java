package com.app.paul.galleryapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements GridAdapter.OnItemClick {
    private List<Photos> adapterList;
    public static final String ADAPTER_LIST = "ADAPTER_LIST";
    public static final String POSITION_ADAPTER = "POSITION_ADAPTER";
    private static final String GRID_STATE = "GRID_STATE";
    private boolean isScrolling = false;
    private int cont = 1;
    NasaApi nasaApi;
    @BindView(R.id.grid) GridView grid;
    GridAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ButterKnife binding view
        ButterKnife.bind(this);

        nasaApi = RetrofitClient.getInstance();

        //Loading data from api
        Call<RoverImage> call = load(nasaApi, 1);

        call.enqueue(new Callback<RoverImage>() {
            @Override
            public void onResponse(@NonNull Call<RoverImage> call, @NonNull Response<RoverImage> response) {
                RoverImage list = response.body();
                if(list != null) {
                    adapterList = list.getPhotos();
                    setAdapter(adapterList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RoverImage> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method for setting grid adapter
    private void setAdapter(final List<Photos> adapterList) {
        adapter = new GridAdapter(MainActivity.this, adapterList, this);
        grid.setAdapter(adapter);
        //Checking for scroll position to fetch new data
        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentItems = grid.getChildCount();
                int totalItems = grid.getCount();
                int scrollItems = grid.getFirstVisiblePosition();

                if(isScrolling && (currentItems + scrollItems == totalItems)){
                    isScrolling = false;
                    cont ++;
                    Call<RoverImage> call = load(nasaApi, cont);
                    call.enqueue(new Callback<RoverImage>() {
                        @Override
                        public void onResponse(@NonNull Call<RoverImage> call, @NonNull Response<RoverImage> response) {
                            RoverImage list = response.body();
                            if(list!= null) {
                                adapterList.addAll(list.getPhotos());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RoverImage> call, @NonNull Throwable t) {

                        }
                    });
                }
            }
        });
    }

    //On item click event, if clicked the image display activity is shown
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(Integer position, ImageView img) {
        Intent intent = new Intent(this, DisplayImageActivity.class);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, img, "my_transition" /*ViewCompat.getTransitionName(img)*/);
        intent.putParcelableArrayListExtra(ADAPTER_LIST, (ArrayList<? extends Parcelable>) adapterList);
        intent.putExtra(POSITION_ADAPTER, position);
        startActivity(intent, compat.toBundle());
    }

    /**
     * Load method
     * @param nasaApi Nasa api instance
     * @param page page number to be fetched
     * @return Nasa api call
     */
    public Call<RoverImage> load(NasaApi nasaApi, int page){
       return nasaApi.getRocketImages(50,page,"D4qaqQHftAk3mp7OkM7kbUArBzTiGaxEZymsHXHT");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(GRID_STATE,grid.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        grid.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_STATE));
    }

}
