package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.User;

public class MoveToAlbum extends AppCompatActivity {
    private int albumIndex;
    private EditText photoName;
    private ListView listView;
    private User user;
    private Album currentAlbum;
    int selectedItem = -1;
    List<String> temp = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_album);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.albums_list_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;

        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }

        for(Album a: user.getAlbums()) {
            if(!a.getAlbumName().equals(bundle.getString("albumName"))) {
                temp.add(a.getAlbumName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, temp);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = position;
                    }
                }
        );
    }

    //cancels the activity
    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void save(View view) {

        if (selectedItem == -1) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"No album was selected");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "NO selection");
            return;
        }

        //creates the bundle to be sent back to the caller
        Bundle bundle = new Bundle();
        bundle.putString("albumRefName", temp.get(selectedItem));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
