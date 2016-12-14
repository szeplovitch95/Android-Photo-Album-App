package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.User;

public class SearchResults extends AppCompatActivity {
    private static final int PHOTO_INFO_CODE = 99 ;
    private User user;
    private Toolbar toolbar;
    private TextView tag_desc;
    private GridView gridView;
    private AlbumImageAdapter myImgAdapter;
    private List<PhotoSRO> photosResult = new ArrayList<PhotoSRO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;

        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }

        tag_desc = (TextView) findViewById(R.id.tag_desc);
        gridView = (GridView) findViewById(R.id.gridview);

        tag_desc.setText("Tag type = " + bundle.getString("tagType") + "   AND   Tag value = " +
                bundle.getString("tagValue"));

        PhotoSRO.allphotos = new ArrayList<String>();

        photosResult = user.searchPhotos(bundle.getString("tagType"), bundle.getString("tagValue"));
        myImgAdapter = new AlbumImageAdapter(this, PhotoSRO.allphotos);

        gridView.setAdapter(myImgAdapter);

        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showPhotoInfo(position);
                    }
                }
        );
    }

    public void showPhotoInfo(int pos) {
        Bundle bundle = new Bundle();
        Album album = photosResult.get(pos).getAlbum();
        bundle.putString("albumName", album.getAlbumName());
        bundle.putString("photoName", photosResult.get(pos).getPhoto().getImageRef());

        Intent intent = new Intent(getApplicationContext(), PhotoResult_Info.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, PHOTO_INFO_CODE);
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
