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

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class HomeScreen extends AppCompatActivity {
    public static final int EDIT_ALBUM_CODE = 1;
    public static final int ADD_ALBUM_CODE = 2;
    public static final int OPEN_ALBUM_CODE = 3;
    public static final int SEARCH_ALL_PHOTOS = 4;

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

        if(user == null) {
            user = new User();
        }

        //prints the list of albums in the console.
        Log.d("message", user.toString());

        //sets the listview of albums and the adapter
        albumsListView = (ListView) findViewById(R.id.albums_list_view);
        listAdapter = new CustomAlbumAdapter(getApplicationContext(), user.getAlbums(), user);
        albumsListView.setAdapter(listAdapter);

        albumsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        openAlbum(position);
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
            case R.id.action_search_photo:
                searchPhotos();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addAlbum() {
        Intent intent = new Intent(getApplicationContext(), AddEditAlbum.class);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    public void openAlbum(int pos) {
        Bundle bundle = new Bundle();
        Album album = user.getAlbums().get(pos);

        //sets up the bundle to be sent to the AlbumScreen class
        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("albumIndex", pos);

        Intent intent = new Intent(getApplicationContext(), AlbumScreen.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, OPEN_ALBUM_CODE);
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
            Album a = new Album(albumNewName);
            user.getAlbums().add(a);
        }

        albumsListView.setAdapter(listAdapter);

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void searchPhotos() {
        Intent intent = new Intent(getApplicationContext(), SearchPhotosScreen.class);
        startActivityForResult(intent, SEARCH_ALL_PHOTOS);
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}