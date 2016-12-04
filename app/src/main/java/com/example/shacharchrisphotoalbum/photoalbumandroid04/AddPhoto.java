package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


public class AddPhoto extends AppCompatActivity {
    private int albumIndex;
    private EditText photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        photoName = (EditText)findViewById(R.id.album_name);
    }

    //cancels the activity
    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


    //STILL NEEDS THE CORRECT IMPLEMENTATION ONCE KNOWING HOW AND FROM WHERE TO ADD PHOTOS TO THE APPLICATION.
    public void save(View view) {
        String name = photoName.getText().toString();

        //checks if input is valid
        if (name == null || name.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Name is required");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }

        //creates the bundle to be sent back to the caller
        Bundle bundle = new Bundle();
        bundle.putInt("index", albumIndex);
        bundle.putString("albumName",photoName.getText().toString());

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}

