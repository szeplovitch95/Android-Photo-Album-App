package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private TextView albumsText;
    private Album currentAlbumOpen;
    private User user;
    private ListView photosListView;
    private Toolbar myToolbar;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_screen);

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


//        for(int i = 0; i < 10; i++) {
//            currentAlbumOpen.addPhoto(new Photo(new File("C:\\Users\\Dror\\Desktop\\bowtie.png")));
//        }

        photosListView = (ListView) findViewById(R.id.photos_list_view);
        listAdapter = new CustomPhotoAdapter(getApplicationContext(), currentAlbumOpen.getPhotos(), user);
        photosListView.setAdapter(listAdapter);
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }

}
