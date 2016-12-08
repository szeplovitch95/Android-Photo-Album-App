package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
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
    private int selectedItem = -1;
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

        if(data == null) return;

        String albumName = data.getString("albumName");

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
        nameOfAlbum.setText("" + currentAlbumOpen.getAlbumName());
        sizeOfAlbum = (TextView) findViewById(R.id.sizeOfAlbum);
        sizeOfAlbum.setText("Size: " + currentAlbumOpen.getSize());

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
            case R.id.manageTags:
                if(myImgAdapter.getCount() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"No photo was selected or/and album is empty");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return true;
                }

                manageTags(selectedItem);
                return true;
            case R.id.removePhoto:
                if(myImgAdapter.getCount() == 0 || selectedItem == -1) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"No photo was selected or/and album is empty");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return true;
                }

                removePhoto(selectedItem);
                return true;
            case R.id.movePhoto:
                if(myImgAdapter.getCount() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"No photo was selected or/and album is empty");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return true;
                }

                movePhoto(selectedItem);
                return true;
            case R.id.slideshow:
                if(myImgAdapter.getCount() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Album is empty");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return true;
                }
                openSlideShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPhoto() {
        Intent intent = new Intent(getApplicationContext(), PhotoChooser.class);
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
        bundle.putString("albumName", currentAlbumOpen.getAlbumName());
        Intent intent = new Intent(getApplicationContext(), MoveToAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, MOVE_TO_CODE);
    }

    // still needs more implementation to work perfectly.
    // fix this so when you move a photo that already exists, you remove the right
    // photo and have no problems with the index. VERY IMPORTANT.
    public void removePhoto(int position) {
        Integer ref = myImgAdapter.getItem(position);
        myImgAdapter.removePicture(ref);
        myImgAdapter.notifyDataSetChanged();
        currentAlbumOpen.removePhotoByRef(ref);
        sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());
        selectedItem = -1;

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < myImgAdapter.getCount(); i++) {
            if(gridview.getChildAt(i) == null) break;
            gridview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
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

        if(requestCode == ADD_PHOTO_CODE) {
            Bundle bundle = intent.getExtras();
            Integer x = bundle.getInt("ref");
            myImgAdapter.addPicture(x);
            myImgAdapter.notifyDataSetChanged();
            currentAlbumOpen.addPhoto(new Photo(x));
            sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());
        }

        else if(requestCode == MOVE_TO_CODE) {
            Bundle bundle = intent.getExtras();
            String albumRef = bundle.getString("albumRefName");
            Integer ref = myImgAdapter.getItem(selectedItem);
            Album a = user.getAlbumByName(albumRef);
            a.addPhoto(currentAlbumOpen.getPhotos().get(selectedItem));

            myImgAdapter.removePicture(ref);
            myImgAdapter.notifyDataSetChanged();
            currentAlbumOpen.removePhotoByRef(ref);
            sizeOfAlbum.setText("Album's Size: " + currentAlbumOpen.getSize());
            selectedItem = -1;
        }

        else if(requestCode == MANAGE_TAGS_CODE) {
            //leave empty for now. most likely will stay empty.
        }

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}