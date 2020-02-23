package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    MediaPlayer player;
    private TextView infoTextView;
     Button playBtn;

    private BottomNavigationView bottomNavigationView;
    //Playlist 
    //https://www.youtube.com/watch?v=FMpGMKCIOVA
    //Footer menu
    //https://www.youtube.com/watch?v=JjfSjMs0ImQ
    //Play/pause and barra de la cancion
    //https://www.youtube.com/watch?v=zCYQBIcePaw
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Boton play
        playBtn = (Button) findViewById(R.id.playBtn);
        //Text de hola mundo
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        //BottomNavMenu
        bottomNavigationView =(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeItem){
                    infoTextView.setText(R.string.Home);
                }else if(item.getItemId() == R.id.searchItem){
                    infoTextView.setText(R.string.Search);
                }else if(item.getItemId() == R.id.playlistItem){
                    infoTextView.setText(R.string.Library);
                }
                return true;
            }
        });
        //Mediaplayer
        player = MediaPlayer.create(this,R.raw.example);
        player.setLooping(true);
        player.seekTo(0);
        player.setVolume(0.5f,0.5f);

    }
    public void playBtnClick(View view){
        if (!player.isPlaying()){
            //Stopping
            player.start();
            playBtn.setBackgroundResource(R.drawable.ic_stop);
        }else{
            //Playing
            player.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }


}

