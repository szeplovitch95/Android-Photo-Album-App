package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import model.User;

public class AddEditAlbum extends AppCompatActivity {
    private int albumIndex;
    private EditText albumName;
    private User user;
    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }

        albumName = (EditText)findViewById(R.id.album_name);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            isEditMode = true;
            albumIndex = bundle.getInt("index");
            albumName.setText(bundle.getString("albumName"));
        }
    }

    //cancels the activity
    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        String name = albumName.getText().toString();

        //checks if input is valid
        if (name == null || name.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Name is required");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }

        if(user.getAlbumByName(name) != null && !isEditMode) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Album name already exists.\n Please choose another name");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }

        //creates the bundle to be sent back to the caller
        Bundle bundle = new Bundle();
        bundle.putInt("index", albumIndex);
        bundle.putString("albumName",albumName.getText().toString());

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}