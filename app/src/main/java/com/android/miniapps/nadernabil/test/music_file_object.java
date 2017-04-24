package com.android.miniapps.nadernabil.test;

/**
 * Created by NaderNabil on 3/26/2017.
 */

public class music_file_object {
    private String audio_name;
    private int duration; // in seconds
    private String audio_cover_image;
    private String stream_url;
    private String artist;

    public music_file_object(String audio_name,String artist , int duration, String audio_cover_image, String stream_url) {
        this.audio_name = audio_name;
        this.duration = duration;
        this.audio_cover_image = audio_cover_image;
        this.stream_url=stream_url;
        this.artist=artist;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public int getDuration() {
        return duration;
    }

    public String getAudio_cover_image() {
        return audio_cover_image;
    }

    public String getStream_url() {
        return stream_url;
    }

    public String getArtist() {
        return artist;
    }
}
