package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import model.*;

public class HomeScreen extends AppCompatActivity {
    public static final int EDIT_ALBUM_CODE = 1;
    public static final int ADD_ALBUM_CODE = 2;
    public static final int OPEN_ALBUM_CODE = 3;
    public static final int SEARCH_ALL_PHOTOS = 4;
    private SwipeMenuListView albumsListView;
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

        albumsListView = (SwipeMenuListView) findViewById(R.id.albums_listView);
        listAdapter = new CustomAlbumAdapter(getApplicationContext(), user.getAlbums(), user);
        albumsListView.setAdapter(listAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                createMenu1(menu);
            }

            private void createMenu1(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(24);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_white_24px);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        albumsListView.setMenuCreator(creator);

        // step 2. listener item click event
        albumsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item =  user.getAlbums().get(position).getAlbumName();
                switch (index) {
                    case 0:
                        editAlbum(position);
                        break;
                    case 1:
                        user.getAlbums().remove(position);
                        listAdapter = new CustomAlbumAdapter(getApplicationContext(), user.getAlbums(), user);
                        albumsListView.setAdapter(listAdapter);
                        break;
                }
                return false;
            }
        });

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

            case R.id.action_search:
                if(albumsListView.getCount() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Cannot search on an empty album");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return true;
                }
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

    public void editAlbum(int pos) {
        Bundle bundle = new Bundle();
        Album album = user.getAlbums().get(pos);

        //sets up the bundle to be sent to the AddEditAlbum class
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}