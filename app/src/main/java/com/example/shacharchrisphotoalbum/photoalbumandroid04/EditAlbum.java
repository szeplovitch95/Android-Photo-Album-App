package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;



public class EditAlbum extends AppCompatActivity {
    private int albumIndex;
    private EditText albumName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        albumName = (EditText)findViewById(R.id.album_name);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            albumIndex = bundle.getInt("index");
            albumName.setText(bundle.getString("albumName"));
        }

    }

    // called when the user taps the Cancel button
    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    // called when the user taps the Save button
    public void save(View view) {
        String name = albumName.getText().toString();


        // name is mandatory
        if (name == null || name.length() == 0) {

            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Name is required");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");

            return;   // does not quit activity, just returns from method
        }

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("index", albumIndex);
        bundle.putString("albumName",albumName.getText().toString());
        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        finish(); // pops the activity from the call stack, returns to parent
    }
}
