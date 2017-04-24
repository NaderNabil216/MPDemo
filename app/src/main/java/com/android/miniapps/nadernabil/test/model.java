package com.android.miniapps.nadernabil.test;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 4/6/2017.
 */

public class model implements MVP_Main.model_stuff {

    @Override
    public void get_data_array(connect_presenter presenter) {
        ArrayList<music_file_object> arrayList = new ArrayList<>();
        music_file_object mfo = new music_file_object("ma balash",
                "hamaki",
                248,
                "https://i.ytimg.com/vi/jD3yn2Ib-fE/maxresdefault.jpg",
                "http://sr1.dndnha.com/mem/mohamed-hamaki/albums/omro-ma-ygheb/Hamaki%20-%20Ma%20Balash.mp3");
        arrayList.add(mfo);
        arrayList.add(mfo);
        arrayList.add(mfo);
        arrayList.add(mfo);
        arrayList.add(mfo);
        presenter.receiving_data(arrayList);
    }

    interface connect_presenter {
        void receiving_data(ArrayList<music_file_object> arrayList);
    }
}
