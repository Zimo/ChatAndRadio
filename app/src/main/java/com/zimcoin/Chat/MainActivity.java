package com.zimcoin.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Button chatbtn;
    private Button playbtn;
    private MediaPlayer mediaPlayer;
    private String stream = "http://listen.radionomy.com/exfestradio?type=.mp3";
    private Boolean prepared = false;
    private Boolean started = false;
    private ProgressDialog _progressDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Radio

        playbtn = (Button) findViewById(R.id.play);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        _progressDialog1 = new ProgressDialog(MainActivity.this);
        _progressDialog1.setMessage("Connecting ...");
        _progressDialog1.show();
         new PlayeTask().execute(stream);


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(started)
                {
                    started =false;
                    mediaPlayer.pause();
                    playbtn.setText("PLAY");
                }else
                {
                    started = true;
                    mediaPlayer.start();
                    playbtn.setText("PAUSE");
                }
            }
        });

        //Chat
        chatbtn = (Button) findViewById(R.id.btnChat);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                access_chat_room();
            }
        });
    }

    //===================================//
    private void access_chat_room() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }
    //SignInActivity
    /*private void access_chat_room_old() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Name:");
        final EditText input_filed = new EditText(this);
        builder.setView(input_filed);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input_filed.getText().toString();
                Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);  //Intent lead us to the new class "ChatRoomActivity"
                intent.putExtra("room_name", "ExFestRoom"); // send chat room name (key, value) from the mainActivity to ChatRoomActivity activity
                intent.putExtra("user_name", name); // send chat user name (key, value) from the mainActivity to ChatRoomActivity activity
                startActivity(intent); // start the activity ChatRoomActivity
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }*/
    //===================================//
    private class PlayeTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                mediaPlayer.setDataSource(params[0]); //Set the data source as stream
                mediaPlayer.prepare(); //Prepare the player for playback, synchronously
                prepared = true;
                _progressDialog1.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }
    }
}
