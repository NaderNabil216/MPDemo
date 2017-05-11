package com.android.miniapps.nadernabil.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MVP_Main.view_stuff {

    @BindView(R.id.artist_image)
    ImageView cover;
    @BindView(R.id.artist_name)
    TextView artist;
    @BindView(R.id.progressBar)
    ProgressBar buffering_ProgressBar;
    @BindView(R.id.song_title)
    TextView title;
    @BindView(R.id.current_time)
    TextView Current;
    @BindView(R.id.max_duration)
    TextView Max;
    @BindView(R.id.fab)
    RelativeLayout fab;
    @BindView(R.id.control)
    ImageView playback;

    Presenter presenter;
    boolean isBind = false;
    player_service Service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new Presenter(getApplicationContext(), this, new model());
        controller.getInstance().setPresenter(presenter);
        fab.setOnClickListener(this);
        music_file_object object = presenter.getAudioList().get(0);
        cover.setDrawingCacheEnabled(true);
        set_track_info(object.getAudio_name(), object.getArtist(), object.getAudio_cover_image(), presenter.change(object.getDuration()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBind) {
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(presenter.getAudioList());
            storage.storeAudioIndex(0);
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
        if (new StorageUtil(getApplicationContext()).loadAudioState()) {
            presenter.call_pause();
        } else {
            presenter.call_resume();
        }
    }

    @Override
    public void play_view(int position) {
        change_playback(this, playback, R.drawable.pause);
        if (position == 0) {
            Current.setText("00:00");
        }
    }

    @Override
    public void pause_view(int position) {
        change_playback(this, playback, R.drawable.play);
    }

    @Override
    public void reset_view() {
        title.setText("");
        artist.setText("");
    }

    @Override
    public void set_track_info(String track_name, String artist, String cover_link, String max_duration) {
        Picasso.with(this).load(cover_link).into(cover);
        Log.d("nader", "Reached picasso");
        Max.setText(max_duration);
        this.artist.setText(artist);
        title.setText(track_name);
    }

    @Override
    public void setposition_and_state(String position, boolean isplaying) {
        change_playback(this, playback, R.drawable.play);
        Current.setText(position);
        if (isplaying) {
            change_playback(this, playback, R.drawable.pause);
        }
    }

    @Override
    public void set_progress_buffer(int progress) {
        buffering_ProgressBar.setProgress(progress);
    }

    @Override
    public Bitmap get_bitmap() {
        Bitmap bitmap = cover.getDrawingCache();
        if (bitmap == null) {
            Log.d("nader", "bitmap is null");
            return BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.placeholder);
        }
        return bitmap;
    }

    @Override
    public void set_current_timer(String timer) {
        Current.setText(timer);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            player_service.LocalBinder localBinder = (player_service.LocalBinder) service;
            Service = localBinder.getService();
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
            Service.stopSelf();
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

    public static void change_playback(Context c, final ImageView v, final int src) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(src);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

}
