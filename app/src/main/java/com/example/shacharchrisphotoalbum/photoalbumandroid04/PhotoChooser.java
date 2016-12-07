package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class PhotoChooser extends AppCompatActivity {
    private ImageAdapter myImgAdapter;
    private int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chooser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myImgAdapter = new ImageAdapter(this);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(myImgAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedItem = position;
                for (int i = 0; i < myImgAdapter.getCount(); i++) {
                    if(gridview.getChildAt(i) == null) break;
                    if(position == i ){
                        gridview.getChildAt(i).setBackgroundColor(Color.RED);
                    } else {
                        gridview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
    }

    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    //STILL NEEDS THE CORRECT IMPLEMENTATION ONCE KNOWING HOW AND FROM WHERE TO ADD PHOTOS TO THE APPLICATION.
    public void save(View view) {

        if(selectedItem == -1) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Choose a photo in order add it to the album");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }

        //creates the bundle to be sent back to the caller
        Bundle bundle = new Bundle();
        Integer ref = myImgAdapter.getItem(selectedItem);
        Log.d("Integer: ", ref + "" );

        bundle.putInt("ref", ref);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
