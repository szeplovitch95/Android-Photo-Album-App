package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.IOException;

import model.Album;
import model.User;

public class SlideshowScreen extends AppCompatActivity {

    private Album currentAlbumOpen;
    private User user;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }

        Bundle data = getIntent().getExtras();

        if(data == null)
        {
            return;
        }


    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}
