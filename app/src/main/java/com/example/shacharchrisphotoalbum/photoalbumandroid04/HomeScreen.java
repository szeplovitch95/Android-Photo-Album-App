package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.io.IOException;

import model.*;

public class HomeScreen extends AppCompatActivity {

    public static final int EDIT_ALBUM_CODE = 1;
    public static final int ADD_ALBUM_CODE = 2;

    private ListView albumsListView;
    private Toolbar myToolbar;
    private User user;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                addAlbum();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addAlbum() {
        Intent intent = new Intent(getApplicationContext(), AddEditAlbum.class);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    public void editAlbum(int pos) {
        Bundle bundle = new Bundle();
        Album album = user.getAlbums().get(pos);

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("index", pos);

        Intent intent = new Intent(getApplicationContext(), AddEditAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_ALBUM_CODE);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            return;
        }

        String albumNewName = bundle.getString("albumName");
        int index = bundle.getInt("index");

        if(requestCode == EDIT_ALBUM_CODE) {
            Album album = user.getAlbums().get(index);
            album.setAlbumName(albumNewName);
        } else if(requestCode == ADD_ALBUM_CODE) {
            user.getAlbums().add(new Album(albumNewName));
        }

        albumsListView.setAdapter(listAdapter);


        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}