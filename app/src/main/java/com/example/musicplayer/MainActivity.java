package com.example.musicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange==AudioManager.AUDIOFOCUS_LOSS)
            {
                releaseMediplayer();
            }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
                mediaPlayer.seekTo(0);
            }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK|| focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                mediaPlayer.pause();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionMedia = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Toast.makeText(MainActivity.this, "I am done !!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioManager= (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer=MediaPlayer.create(this,R.raw.kar_har_maidaan);


        Button play= findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            public void onClick(View v) {

                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mediaPlayer.start();
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        Toast.makeText(MainActivity.this, "I am done !!", Toast.LENGTH_SHORT).show();
//                    }
//                });
                    //or,
                    mediaPlayer.setOnCompletionListener(mCompletionMedia);
                }
            }
        });
        Button pause=findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                Toast.makeText(MainActivity.this, "pause", Toast.LENGTH_LONG).show();
                releaseMediplayer();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediplayer();
    }

    // To release the memory for song and carry on for next song
    public void releaseMediplayer(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;

            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }
}