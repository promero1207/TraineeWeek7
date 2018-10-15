package com.app.paul.galleryapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.paul.galleryapp.MainActivity.ADAPTER_LIST;
import static com.app.paul.galleryapp.MainActivity.POSITION_ADAPTER;

/**
 * Activity to display the image in full size
 */
public class DisplayImageActivity extends AppCompatActivity implements AdapterRecyclerGallery.OnItemClickListener, AdapterRecyclerGallery.OnLongItemClickListener, AdapterRecyclerGallery.SourceReady {
    private boolean isHomeItemInvisible = false;
    private static final String TAG_DIALOG = "Rover Info Dialog";
    public static final String LAUNCH = "LAUNCH";
    public static final String LANDING = "LANDING";
    public static final String ROVER_NAME = "ROVER_NAME";
    public static final String SRC = "SRC";
    private List<Photos> listPhotos;
    @BindView(R.id.display_image_gallery_recycler) RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_image);

        //ButterKnife Binding view
        ButterKnife.bind(this);

        getWindow().setEnterTransition(makeEnterTransition());

        Bundle extras = getIntent().getExtras();
        int positionAdapter = 0;
        if(extras != null) {
            listPhotos = extras.getParcelableArrayList(ADAPTER_LIST);
            positionAdapter = extras.getInt(POSITION_ADAPTER);
        }

        AdapterRecyclerGallery adapter = new AdapterRecyclerGallery(this,listPhotos, this, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(positionAdapter);


        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);




        //Making recyclerView act like a viewPager
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        //Back arrow
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            hideSystemUI();
            isHomeItemInvisible = false;
        }
    }



    //Hide ui method
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    //Show ui method
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    //Listener implementations
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick() {
        if(isHomeItemInvisible) {
            isHomeItemInvisible = false;
            hideSystemUI();
        }
        else {
            isHomeItemInvisible = true;
            showSystemUI();
        }
    }

    @Override
    public void onLonClickItemListener(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ROVER_NAME, listPhotos.get(position).getRover().getName());
        bundle.putString(LANDING, listPhotos.get(position).rover.getLanding_date());
        bundle.putString(LAUNCH, listPhotos.get(position).rover.getLaunch_date());
        bundle.putString(SRC, listPhotos.get(position).getImg_src());
        BottomDialogInfo dialog = new BottomDialogInfo();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    @Override
    public void ready() {
        DisplayImageActivity.this.supportStartPostponedEnterTransition();
    }
}
