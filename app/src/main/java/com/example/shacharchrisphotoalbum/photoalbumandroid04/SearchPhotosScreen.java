package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import model.User;

public class SearchPhotosScreen extends AppCompatActivity {
    public static final int SEARCH_RESULTS_CODE = 1;
    private User user;
    private Toolbar toolbar;
    private EditText tagTypeEText;
    private EditText tagValueEText;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photos_screen);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        try {
            user = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }

        tagTypeEText = (EditText) findViewById(R.id.tag_type);
        tagValueEText = (EditText) findViewById(R.id.tag_value);
        searchBtn = (Button) findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchPhotos();
                    }
                }
        );
    }

    public void searchPhotos() {
        if(tagTypeEText.getText().toString().trim().equals("location") || tagTypeEText.getText().toString().trim().equals("person")) {
            if(tagValueEText.getText().toString() != null && tagValueEText.getText().toString().trim().length() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("tagType", tagTypeEText.getText().toString().trim());
                bundle.putString("tagValue", tagValueEText.getText().toString().trim());

                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, SEARCH_RESULTS_CODE);
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Tag Value field is empty");
                DialogFragment newFragment = new AlbumDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "Album cannot be empty");
                return;
            }
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Tag Type field is empty or is not one of person/location");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "Album cannot be empty");
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = data.getExtras();
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}