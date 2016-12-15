package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Photo;
import model.Tag;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class PhotoResult_Info extends AppCompatActivity {
    private User user;
    private int albumIndex;
    private Album currentAlbum;
    private Photo currentPhoto;
    private ImageView singleImageView;
    private ListView tagsListView;
    private List<String> tags = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result__info);

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

        currentAlbum = user.getAlbumByName(data.getString("albumName"));
        currentPhoto = currentAlbum.getPhotoByRef(data.getString("photoName"));
        singleImageView = (ImageView) findViewById(R.id.singleImageVIew);
        tagsListView = (ListView) findViewById(R.id.tagsListVIew);
        singleImageView.setImageBitmap(BitmapFactory.decodeFile(currentPhoto.getImageRef()));

        for(Tag t : currentPhoto.getTags()) {
            tags.add(" - " + t.getTagType()+ ", " + t.getTagValue());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
        tagsListView.setAdapter(adapter);
    }
}
