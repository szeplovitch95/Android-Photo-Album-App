package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class SearchResults extends AppCompatActivity {
    private User user;
    private Toolbar toolbar;
    private TextView tag_desc;
    private GridView gridView;
    private AlbumImageAdapter myImgAdapter;
    private List<String> photosResult = new ArrayList<String>();

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

        photosResult = user.searchPhotos(bundle.getString("tagType"), bundle.getString("tagValue"));
        myImgAdapter = new AlbumImageAdapter(this, photosResult);

        gridView.setAdapter(myImgAdapter);
    }
}
