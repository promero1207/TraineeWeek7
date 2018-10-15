package com.app.paul.galleryapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * adapter for recycler view
 */
public class AdapterRecyclerGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener listener;
    private OnLongItemClickListener longListener;
    private List<Photos> adapterList;
    private SourceReady sourceReadyListener;
    private Context context;


    //constructor
    AdapterRecyclerGallery(Context context, List<Photos> list, OnItemClickListener listener, OnLongItemClickListener longListener, SourceReady sourceReadyListener) {
        this.context = context;
        adapterList = list;
        this.listener = listener;
        this.longListener = longListener;
        this.sourceReadyListener = sourceReadyListener;
    }

    //creates holder and returns the view to be inflated
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_image, parent, false);

        return new ViewHolder(v);
    }

    //on binding holder
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == 0) {
            //Casting holder to type ViewHolder
            final ViewHolder h = (ViewHolder) holder;
            //Glide for loading images from url using cache
            GlideApp.with(context)
                    .load(Uri.parse(adapterList.get(position).getImg_src()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            h.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            sourceReadyListener.ready();
                            h.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.nasa)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(h.img);
        }
    }

    //geting list size count
    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    //View holder
    class ViewHolder extends RecyclerView.ViewHolder {
        //Views
        @BindView(R.id.rover_img) ImageView img;
        @BindView(R.id.item_display_image_progress) ProgressBar progressBar;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //ButterKnife binding the view
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void onClick() {
            listener.onItemClick();
            //clicked item position
        }

        @OnLongClick
        boolean OnLongClick(){
            longListener.onLonClickItemListener(getAdapterPosition());
            //long click on item
            return false;
        }

    }

    //on click interface
    public interface OnItemClickListener {
        void onItemClick();
    }

    //on long click interface returns the position of the element clicked
    public interface OnLongItemClickListener {
        void onLonClickItemListener(int position);
    }

    //ready listener to notigy when Glide is done loading
    public interface SourceReady {
        void ready();
    }

}
