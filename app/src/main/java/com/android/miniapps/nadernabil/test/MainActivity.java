package com.android.miniapps.nadernabil.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.mobiwise.library.MusicPlayerView;


public class MainActivity extends AppCompatActivity implements MVP_Main.view_stuff {

    @BindView(R.id.mpv)
    MusicPlayerView mpv;
    @BindView(R.id.artist_textView)
    TextView artist;
    @BindView(R.id.progressbar)
    ProgressBar buffering_ProgressBar;
    @BindView(R.id.track_title)
    TextView title;
    @BindView(R.id.playing_next)
    ImageView next_btn;
    @BindView(R.id.playing_previous)
    ImageView prev_btn;
    Presenter presenter;

    boolean isBind = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new Presenter(getApplicationContext(), this, new model());
        mpv.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        prev_btn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBind) {
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(presenter.getAudioList());
            storage.storeAudioIndex(0);
//            presenter.setInfoForFirstTime();
            Intent playerIntent = new Intent(this, player_service.class);
            startService(playerIntent);
            bindService(playerIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            if (new StorageUtil(getApplicationContext()).loadAudioState()) {
                //hena el player sha8al
                presenter.updateUI(true, presenter.request_current_position());
            } else {
                //hena el player wa2ef
                presenter.updateUI(false, presenter.request_resume_position());
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mpv:
                if (new StorageUtil(getApplicationContext()).loadAudioState()){
                    presenter.call_pause();
                }else{
                    presenter.call_resume();
                }
                break;
            case R.id.playing_next:
                presenter.call_play_next();
                break;
            case R.id.playing_previous:
                presenter.call_play_prev();
                break;

        }
    }

    @Override
    public void play_view(int position) {
        mpv.setProgress(position);
        mpv.start();
    }

    @Override
    public void pause_view(int position) {
        mpv.stop();
        mpv.setProgress(position);
    }

    @Override
    public void reset_view() {
        mpv.stop();
        mpv.setMax(0);
        mpv.setProgressVisibility(false);
        title.setText("");
        artist.setText("");
        buffering_ProgressBar.setProgress(0);
    }

    @Override
    public void set_track_info(String track_name, String artist, String cover_link, int max_duration) {
        mpv.setCoverURL(cover_link);
        mpv.setMax(max_duration);
        this.artist.setText(artist);
        title.setText(track_name);
    }

    @Override
    public void setposition_and_state(int position, boolean isplaying) {

        if (isplaying) {
            mpv.stop();
        } else {

            mpv.start();
            mpv.setProgress(position);
        }
    }

    @Override
    public void set_progress_buffer(int progress) {
     buffering_ProgressBar.setProgress(progress);
    }

    @Override
    public void appear_progressbar() {
        mpv.setProgressVisibility(true);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            player_service.LocalBinder localBinder = (player_service.LocalBinder) service;
            localBinder.set_presenter(presenter);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(mConnection);
            isBind = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("serviceStatus", isBind);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isBind = savedInstanceState.getBoolean("serviceStatus");
    }


}
