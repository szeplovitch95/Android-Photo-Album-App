package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.io.IOException;

import model.Album;
import model.Photo;
import model.User;

public class AlbumScreen extends AppCompatActivity {
    public static final int ADD_PHOTO_CODE = 1;
    public static final int SLIDESHOW_CODE = 2;
    public static final int MANAGE_TAGS_CODE = 3;
    public static final int MOVE_TO_CODE = 4;
    private Album currentAlbumOpen;
    private User user;
    private TextView nameOfAlbum;
    private TextView sizeOfAlbum;
    private AlbumImageAdapter myImgAdapter;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_screen);

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

        String albumName = data.getString("albumName");
        int albumIndex = data.getInt("albumIndex");

        for(Album a : user.getAlbums()) {
            if(a.getAlbumName().equals(albumName)) {
                currentAlbumOpen = a;
                break;
            }
        }

        myImgAdapter = new AlbumImageAdapter(this);
        gridview = (GridView) findViewById(R.id.gridview);

        for(Photo p : currentAlbumOpen.getPhotos()) {
            myImgAdapter.addPicture(p.getImageRef());
        }

        gridview.setAdapter(myImgAdapter);
        nameOfAlbum = (TextView) findViewById(R.id.nameOfAlbum);
        nameOfAlbum.setText("Album Name: " + currentAlbumOpen.getAlbumName());
        sizeOfAlbum = (TextView) findViewById(R.id.sizeOfAlbum);
        sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                manageTags(position);
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
            case R.id.action_add_photo:
                addPhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPhoto() {
        Intent intent = new Intent(getApplicationContext(), PhotoChooser.class);
        startActivityForResult(intent, ADD_PHOTO_CODE);
    }

    public void manageTags(int position) {
        Bundle bundle = new Bundle();
        Album album = currentAlbumOpen;

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("albumIndex", position);

        Intent intent = new Intent(getApplicationContext(), TagsManager.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, MANAGE_TAGS_CODE);
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == ADD_PHOTO_CODE) {
            Bundle bundle = intent.getExtras();
            Integer x = bundle.getInt("ref");
            myImgAdapter.addPicture(x);
            myImgAdapter.notifyDataSetChanged();
            currentAlbumOpen.addPhoto(new Photo(x));
            sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());
        }


        else if(requestCode == MANAGE_TAGS_CODE) {

        }

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}