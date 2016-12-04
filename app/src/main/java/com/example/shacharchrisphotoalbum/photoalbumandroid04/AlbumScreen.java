package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Intent;
import android.graphics.Color;
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
    public static final int ADD_PHOTO_CODE = 1;
    public static final int SLIDESHOW_CODE = 2;
    public static final int MANAGE_TAGS_CODE = 3;
    public static final int MOVE_TO_CODE = 4;
    private Album currentAlbumOpen;
    private User user;
    private Toolbar myToolbar;
    private TextView nameOfAlbum;
    private TextView sizeOfAlbum;
    private ImageAdapter myImgAdapter;
    private int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        myImgAdapter = new ImageAdapter(this);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
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
                for (int i = 0; i < myImgAdapter.getCount(); i++) {
                    if(gridview.getChildAt(i) == null) break;
                    if(position == i ){
                        gridview.getChildAt(i).setBackgroundColor(Color.BLUE);
                    }else{
                        gridview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

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
            case R.id.action_add_photo:
                addPhoto();
                return true;
            case R.id.manageTags:
                manageTags(selectedItem);
                return true;
            case R.id.removePhoto:
                removePhoto(selectedItem);
                return true;
            case R.id.movePhoto:
                movePhoto(selectedItem);
                return true;
            case R.id.slideshow:
                openSlideShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPhoto() {
        Intent intent = new Intent(getApplicationContext(), AddPhoto.class);
        startActivityForResult(intent, ADD_PHOTO_CODE);
    }

    public void openSlideShow() {
        Bundle bundle = new Bundle();
        Album album = currentAlbumOpen;

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("albumIndex", selectedItem);

        Intent intent = new Intent(getApplicationContext(), SlideshowScreen.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SLIDESHOW_CODE);
    }

    public void movePhoto(int position) {
        Bundle bundle = new Bundle();
        Album album = currentAlbumOpen;

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("albumIndex", selectedItem);

            Intent intent = new Intent(getApplicationContext(), MoveToAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, MOVE_TO_CODE);
    }

    public void removePhoto(int position) {

    }

    public void manageTags(int position) {
        Bundle bundle = new Bundle();
        Album album = currentAlbumOpen;

        bundle.putString("albumName", album.getAlbumName());
        bundle.putInt("albumIndex", selectedItem);

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
    }
}
