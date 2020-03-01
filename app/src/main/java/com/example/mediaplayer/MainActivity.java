package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //He desabilitado la opcion de landscape mode(ver la aplicacion con la pantalla en horizontal)
    //en el manifiesto --> "android:screenOrientation="portrait""
    private BottomNavigationView bottomNavigationView;
    //Playlist 
    //https://www.youtube.com/watch?v=NTWcen1MkBs
    //Footer menu
    //https://www.youtube.com/watch?v=JjfSjMs0ImQ
    //Play/pause and barra de la cancion
    //https://www.youtube.com/watch?v=zCYQBIcePaw

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //BottomNavMenu
        bottomNavigationView =(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.playlistItem:
                        Intent First= new Intent(MainActivity.this, Playlist.class);
                        startActivity(First);
                        break;
                    case R.id.videoItem:
                        Intent Second= new Intent(MainActivity.this, VideoPlayer.class);
                        startActivity(Second);
                        break;
                }

                return true;
            }
        });


    }



}

