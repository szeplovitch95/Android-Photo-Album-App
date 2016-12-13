package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Photo;
import model.User;

public class AlbumScreen extends AppCompatActivity {
    public static final int ADD_PHOTO_CODE = 1;
    public static final int SLIDESHOW_CODE = 2;
    public static final int MANAGE_TAGS_CODE = 3;
    public static final int MOVE_TO_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9;
    private Album currentAlbumOpen;
    private String imgDecodableString;
    private User user;
    private TextView nameOfAlbum;
    private TextView sizeOfAlbum;
    private AlbumImageAdapter myImgAdapter;
    private GridView gridview;
    private List<String> photosRef = new ArrayList<String>();

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

        Log.i("size: ", currentAlbumOpen.getSize() + "");

        for(Photo p: currentAlbumOpen.getPhotos()) {
            photosRef.add(p.getImageRef());
        }

        myImgAdapter = new AlbumImageAdapter(this, photosRef);
        gridview = (GridView) findViewById(R.id.gridview);
//

//        for(Photo p : currentAlbumOpen.getPhotos()) {
//            myImgAdapter.addPicture(p.getImageRef());
//        }

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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, ADD_PHOTO_CODE);
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


        if (requestCode == ADD_PHOTO_CODE) {
            Uri selectedImage = intent.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            myImgAdapter.addPicture(imgDecodableString);
            myImgAdapter.notifyDataSetChanged();
            currentAlbumOpen.addPhoto(new Photo(imgDecodableString));
            sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());
        }

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}