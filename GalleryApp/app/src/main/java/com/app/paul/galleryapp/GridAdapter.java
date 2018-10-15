package com.app.paul.galleryapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class GridAdapter extends BaseAdapter {

    private List<Photos> adapterList;
    private Context context;
    private LayoutInflater inflater;
    private final OnItemClick listener;


    GridAdapter(Context context, List<Photos> adapterList, OnItemClick listener) {
        this.adapterList = adapterList;
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }   

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = inflater
                    .inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        GlideApp.with(context)
                .load(Uri.parse(adapterList.get(position).getImg_src()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .placeholder(R.drawable.nasa)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        holder.position = position;

        return convertView;
    }

    public final class ViewHolder {
        public int position;
        @BindView(R.id.rover_img) ImageView image;
        @BindView(R.id.progressBar) ProgressBar progressBar;


        @OnClick(R.id.rover_img)
        void onItemClick(){
            listener.onItemClick(position, image);
        }

        ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }

    public interface OnItemClick {
        void onItemClick(Integer position, ImageView v);
    }





}
