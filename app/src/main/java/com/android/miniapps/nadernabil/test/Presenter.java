package com.android.miniapps.nadernabil.test;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by NaderNabil on 3/25/2017.
 */

public class Presenter implements MVP_Main.presenter_stuff {
    private MVP_Main.view_stuff view;
    private MVP_Main.model_stuff model;
    private ArrayList<music_file_object> arrayList;
    private int current_position;
    private int resume_position;
    Context context;

    public Presenter(Context context, MVP_Main.view_stuff view, MVP_Main.model_stuff model) {
        this.view = view;
        this.model = model;
        this.model.get_data_array(this);
        this.context = context;
    }

    @Override
    public void set_buffer(int progress) {
        view.set_progress_buffer(progress);
    }

    @Override
    public void play(int position) {
        view.play_view(position);
    }

    @Override
    public void pause(int position) {
        view.pause_view(position);
    }

    @Override
    public void call_pause() {
        Intent intent = new Intent(Constants.pauseIntent);
        context.sendBroadcast(intent);
    }

    @Override
    public void call_play_next() {
        Intent intent = new Intent(Constants.playNextIntent);
        context.sendBroadcast(intent);
    }

    @Override
    public void call_play_prev() {
        Intent intent = new Intent(Constants.playPreviousIntent);
        context.sendBroadcast(intent);
    }

    @Override
    public void call_resume() {
        Intent intent = new Intent(Constants.ResumeIntent);
        context.sendBroadcast(intent);
    }

    @Override
    public void play_next() {
        music_file_object object = arrayList.get(new StorageUtil(context).loadAudioIndex());
        view.set_track_info(object.getAudio_name(), object.getArtist(), object.getAudio_cover_image(), object.getDuration());

    }

    @Override
    public void play_prev() {
        music_file_object object = arrayList.get(new StorageUtil(context).loadAudioIndex());
        view.set_track_info(object.getAudio_name(), object.getArtist(), object.getAudio_cover_image(), object.getDuration());

    }


    @Override
    public ArrayList<music_file_object> getAudioList() {
        return this.arrayList;
    }

    @Override
    public int request_resume_position() {
        Intent intent = new Intent(Constants.RequestResumePositionIntent);
        (context).sendBroadcast(intent);
        return from_milli_to_second(this.resume_position);
    }

    @Override
    public int request_current_position() {
        Intent intent = new Intent(Constants.RequestCurrentPositionIntent);
        (context).sendBroadcast(intent);
        return from_milli_to_second(this.current_position);
    }

    @Override
    public void set_resume_position(int resume_position) {
        this.resume_position = resume_position;
    }

    @Override
    public void set_current_position(int current_position) {
        this.current_position = current_position;
    }

    @Override
    public int from_milli_to_second(int milli) {
        return (int) TimeUnit.MILLISECONDS.toSeconds(milli);
    }

    @Override
    public void updateUI(boolean isPlaying, int position) {
        reseting_view();
        music_file_object object = arrayList.get(new StorageUtil(context).loadAudioIndex());
        view.set_track_info(object.getAudio_name(), object.getArtist(), object.getAudio_cover_image(), object.getDuration());
        if (isPlaying) {
            view.setposition_and_state(position, false);
            view.appear_progressbar();
        } else {
            view.setposition_and_state(position, true);

            // TODO: 4/20/2017
        }
    }

    @Override
    public void reseting_view() {
        view.reset_view();
    }

    @Override
    public void appear_progressbar() {
        view.appear_progressbar();
    }

    @Override
    public void setInfoForFirstTime() {
        music_file_object object = arrayList.get(new StorageUtil(context).loadAudioIndex());
        view.set_track_info(object.getAudio_name(), object.getArtist(), object.getAudio_cover_image(), object.getDuration());
    }

    @Override
    public void receiving_data(ArrayList<music_file_object> arrayList) {
        this.arrayList = arrayList;
    }


}
