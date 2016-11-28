package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;

import java.io.IOException;
import java.util.List;

import model.*;

public class HomeScreen extends AppCompatActivity {

    private ListView albumsListView;
    private Toolbar myToolbar;
    private User user;
    private GestureDetectorCompat gestureDetector;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }




        //prints the list of albums in the console.
        Log.d("message", user.toString());


        albumsListView = (ListView) findViewById(R.id.albums_list_view);

        listAdapter = new CustomAlbumAdapter(getApplicationContext(), user.getAlbums(), user);

        albumsListView.setAdapter(listAdapter);

        albumsListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                        editAlbum(position);
                        return true;
                    }
                }
        );
    }

    public void editAlbum(int pos) {
        Bundle bundle = new Bundle();
        Album album = user.getAlbums().get(pos);

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("index", pos);

        Intent intent = new Intent(getApplicationContext(), EditAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            return;
        }

        String albumNewName = bundle.getString("albumName");
        int index = bundle.getInt("index");
        Album album = user.getAlbums().get(index);
        album.setAlbumName(albumNewName);
        Log.d("hello SIR", bundle.getString("albumName"));
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}