package com.app.paul.galleryapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.paul.galleryapp.DisplayImageActivity.LANDING;
import static com.app.paul.galleryapp.DisplayImageActivity.LAUNCH;
import static com.app.paul.galleryapp.DisplayImageActivity.ROVER_NAME;
import static com.app.paul.galleryapp.DisplayImageActivity.SRC;

/**
 * Bottom fragment for showing rocket information: name, landing date, launching date and share option
 */
public class BottomDialogInfo extends BottomSheetDialogFragment{
    private String src;
    //Fields
    @BindView(R.id.rover_name) TextView name;
    @BindView(R.id.rover_launch_date) TextView launch;
    @BindView(R.id.rover_landing_date) TextView landing;
    @BindView(R.id.share) Button button;

    public BottomDialogInfo() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //Getting Bundle
        Bundle bundle = getArguments();

        View v =inflater.inflate(R.layout.bottom_dialog_fragment, container, false);
        //ButterKnife binding view
        ButterKnife.bind(this, v);
        Typeface typeface = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), "nasalization_rg.ttf");

        if(bundle != null) {
            src = bundle.getString(SRC);

            name.setText(bundle.getString(ROVER_NAME));
            launch.setText(bundle.getString(LAUNCH));
            landing.setText(bundle.getString(LANDING));

            name.setTypeface(typeface);
            launch.setTypeface(typeface);
            landing.setTypeface(typeface);
        }
        return v;
    }


    @OnClick(R.id.share)
    void onClickShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, src);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
}
