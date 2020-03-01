package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VideoPlayer extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    VideoView videoView;
    MediaController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        videoView = (VideoView) findViewById(R.id.videoViewId);
        mController = new MediaController(this);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.video);
        mController.setAnchorView(videoView);
        videoView.setMediaController(mController);
        videoView.start();
    }
}
