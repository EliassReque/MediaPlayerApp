package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;

public class Reproductor extends AppCompatActivity {
    //PAra que no se reproduzcan dos audios a la vez
    static MediaPlayer player;
    Bundle songExtraDatos;
    ArrayList<File> myAudiosList;
    SeekBar mSeekBar;
    TextView mSongTitle;
    ImageView nextBtn;
    ImageView playBtn;
    ImageView prevBtn;
    int totalTiempo;
    TextView startTiempo;
    TextView timeLeft;
    int position;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        //BottomNavMenu
        bottomNavigationView =(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Switch para cambiar de activities
                switch (item.getItemId()){
                    case R.id.playlistItem:
                        Intent First= new Intent(Reproductor.this, Playlist.class);
                        startActivity(First);
                        break;
                    case R.id.videoItem:
                        Intent Second= new Intent(Reproductor.this, VideoPlayer.class);
                        startActivity(Second);
                        break;
                }

                return true;
            }
        });

        //textview con la info del audio
        mSongTitle = (TextView) findViewById(R.id.infoTextView);

        //Botones play/previous/next
        playBtn =  findViewById(R.id.playBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        //Time de la seekbar
        startTiempo =(TextView) findViewById(R.id.startTime);
        timeLeft = (TextView) findViewById(R.id.endTime);




        //barra tiempo
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);


        //Ver si media es null o no
        if (player != null){
            player.stop();
        }else{
            playBtn.setImageResource(R.drawable.ic_play);
        }
        //Recibimos los datos de la activity Playlist
        Intent songData = getIntent();
        songExtraDatos = songData.getExtras();
        myAudiosList = (ArrayList) songExtraDatos.getParcelableArrayList("audioList");
        position = songExtraDatos.getInt("position",0);

        iniciarPlayer(position);
        play();

        //play/pause button
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        //Pasar a la siguiente cancion
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < myAudiosList.size()-1){
                    //Si la posicion es menor que el numero total que hay pasa a la siguiente
                    position++;
                }else{
                    //Si es mayor, se resetea --> primer audio
                    position=0;
                }
                iniciarPlayer(position);
                //LLamamos el metodo play aqui para que la cancion empiece automáticamente
                play();
            }
        });

        //volver a la siguiente cancion
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 0){
                    //Si la posicion es igual o menor que 0
                    position = myAudiosList.size()-1;
                }else{
                    //Si es mayor, se resetea --> primer audio
                    position--;
                }
                iniciarPlayer(position);
                //LLamamos el metodo play aqui para que la cancion empiece automáticamente
                play();
            }
        });
    }
    //---------------------------------
    //Metodos


    //Calcular los tiempos de la barra
    public String createTimeLabel(int tiempo){
        String time = "";
        int minutos = tiempo/1000/60;
        int segundos = tiempo/1000%60;
        time = minutos + ":";
        if (segundos < 10){
            time = time + "0";
        }
        time = time + segundos;
        return time;
    }
    //Iniciar el reproductor
    private void iniciarPlayer(int position){
        if (player != null && player.isPlaying()){
            player.reset();
        }

        String valor = myAudiosList.get(position).getName();
        mSongTitle.setText(valor);

        //path del audio
        Uri audioResourceUri = Uri.parse(myAudiosList.get(position).toString());

        //Generamos el mediaplayer
        player = MediaPlayer.create(getApplicationContext(),audioResourceUri);
        /*
        player.setLooping(true);
        player.seekTo(0);*/
        player.setVolume(0.5f,0.5f);
        totalTiempo = player.getDuration();
        mSeekBar.setMax(totalTiempo);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Mover la barra y que avance o retroceda el audio
                if (fromUser){
                    player.seekTo(progress);
                    mSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Thread(hilo) para seekbar y end/startTime
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null){
                    try {
                        if (player.isPlaying()){
                            Message mess = new Message();
                            mess.what = player.getCurrentPosition();
                            handler.sendMessage(mess);
                            Thread.sleep(1000);
                        }

                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @SuppressLint("HandlerLeak") private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int currentPosition = msg.what;
            //update positionBar.
            mSeekBar.setProgress(currentPosition);
            //Update labels
            String elapsedTime = createTimeLabel(currentPosition);
            startTiempo.setText(elapsedTime);
            String timeQueFalta = createTimeLabel(totalTiempo - currentPosition);
            timeLeft.setText(timeQueFalta);

        }
    };
    private void play(){
        if (player != null && player.isPlaying()){
            player.pause();
            playBtn.setImageResource(R.drawable.ic_play);
        }else{
            player.start();
            playBtn.setImageResource(R.drawable.ic_stop);
        }
    }
}
