package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumScreen extends AppCompatActivity {
    private TextView albumsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_screen);
        Bundle data = getIntent().getExtras();

        if(data == null)
        {
            return;
        }

        albumsText = (TextView) findViewById(R.id.albumsText);
        albumsText.setText(data.getString("viewT"));
    }
}
