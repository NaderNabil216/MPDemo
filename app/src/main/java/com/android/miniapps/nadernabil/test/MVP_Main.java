package com.android.miniapps.nadernabil.test;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 3/25/2017.
 */

public interface MVP_Main {

    interface view_stuff extends View.OnClickListener {

        void play_view(int position);

        void pause_view(int position);

        void reset_view();

        void set_track_info(String track_name, String artist, String cover_link, int max_duration);

        void setposition_and_state(int position, boolean isplaying);

        void set_progress_buffer(int progress);

        void appear_progressbar();

    }

    interface service_stuff
            extends
            MediaPlayer.OnCompletionListener,
            MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnPreparedListener,
            AudioManager.OnAudioFocusChangeListener {
        void play();

        void pause();

        void initMediaPlayer();

        void stopMedia();

        void resumeMedia();

        boolean requestAudioFocus();

        boolean removeAudioFocus();

        void registerBecomingNoisyReceiver();

        void callStateListener();

        void register_playNewAudio();

        void register_playNextAudio();

        void register_playPreviousAudio();

        void register_pause();

        void register_resume();

        void register_request_current_position();

        void register_request_resume_position();

        void initMediaSession() throws RemoteException;

        void updateMetaData();

        void skipToNext();

        void skipToPrevious();

        void buildNotification(Status status);

        void removeNotification();

        PendingIntent playbackAction(int actionNumber);

        void handleIncomingActions(Intent playbackAction);

        Bitmap getBitmapFromURL(String img_url);
    }

    interface model_stuff {

        void get_data_array(model.connect_presenter presenter);

    }

    interface presenter_stuff extends
            model.connect_presenter {

        void set_buffer(int progress);

        void play(int position);// ha2ol lel service play , tero7 el service menadeya 3la el presenter w te2olo e3mel play lel view

        void pause(int position); // el service hat2oly pause fain

        void call_pause();//ha2ol lel service pause

        void call_play_next();

        void call_play_prev();

        void call_resume();

        void play_next();

        void play_prev();

        ArrayList<music_file_object> getAudioList();

        int request_resume_position();

        int request_current_position();

        void set_resume_position(int resume_position);

        void set_current_position(int current_position);

        int from_milli_to_second(int milli);

        void updateUI(boolean isPlaying, int position);

        void reseting_view();
        void appear_progressbar();

        void setInfoForFirstTime();
    }
}
