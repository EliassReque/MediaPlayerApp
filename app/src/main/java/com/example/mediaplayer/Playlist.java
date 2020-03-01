package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class Playlist extends AppCompatActivity {
    ListView allMusicList;
    ArrayAdapter<String> musicArrayAdapter;
    String songs[];
    ArrayList<File> musics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        allMusicList = (ListView) findViewById(R.id.musicList);
        //He optado por utilizar la libreria dexter por que facilita la gestion de permisos
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //display list of musics
                //Tuve que añadir esta linea "android:requestLegacyExternalStorage="true"" en el manifiesto
                //por que los dispositivos "nuevos" no accedian a la informacion de storage
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new String[musics.size()];

                for(int i =0;i<musics.size();i++){
                    songs[i] = musics.get(i).getName();
                }

                musicArrayAdapter = new ArrayAdapter<>(Playlist.this,R.layout.custom_listview,songs);
                allMusicList.setAdapter(musicArrayAdapter);

                //Al hacer un click en un audio de la playlist cambiamos a la activity main con el reproductor
                allMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent player = new Intent(Playlist.this,Reproductor.class);
                        //Enviamos la informacion del audio a la otra activity
                        player.putExtra("audioList",musics);
                        player.putExtra("position",position);
                        startActivity(player);
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                //keep asking permission each time app is open until granted
                token.continuePermissionRequest();
            }
        }).check();

    }
    //
    private ArrayList<File> findMusicFiles(File file){
        ArrayList<File> listaAudios = new ArrayList<>();
        //listFiles te saca todos los files de un directorio
        File[] files = file.listFiles();
        //Tuve que poner este primer if por si son nulls que no pete el programa
        if (files != null) {
            for (File currentFile: files){
                if (currentFile.isDirectory() && !currentFile.isHidden()){
                    listaAudios.addAll(findMusicFiles(currentFile));
                }else{
                    //select only music audios
                    if (currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".wav")) {
                        listaAudios.add(currentFile);
                    }
                }

            }
        }
        return listaAudios;
    }
}
