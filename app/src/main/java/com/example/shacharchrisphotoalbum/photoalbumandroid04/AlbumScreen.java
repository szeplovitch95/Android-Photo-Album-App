package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

import model.Album;
import model.Photo;
import model.User;

public class AlbumScreen extends AppCompatActivity {
    private Album currentAlbumOpen;
    private User user;
    private Toolbar myToolbar;
    private TextView nameOfAlbum;
    private TextView sizeOfAlbum;
    private ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        myImgAdapter = new ImageAdapter(this);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(myImgAdapter);



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

        //set data recieved from HomeScreen class
        String albumName = data.getString("albumName");
        int albumIndex = data.getInt("albumIndex");

        for(Album a : user.getAlbums()) {
            if(a.getAlbumName().equals(albumName)) {
                currentAlbumOpen = a;
                break;
            }
        }

        nameOfAlbum = (TextView) findViewById(R.id.nameOfAlbum);
        nameOfAlbum.setText("Album Name: " + currentAlbumOpen.getAlbumName());

        sizeOfAlbum = (TextView) findViewById(R.id.sizeOfAlbum);
        sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.relativeLayout1));
                view.setBackgroundResource(myImgAdapter.getImgID(position));

                Toast toast = new Toast(parent.getContext());
                toast.setView(view);
                toast.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                addPhoto();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPhoto() {

    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}
