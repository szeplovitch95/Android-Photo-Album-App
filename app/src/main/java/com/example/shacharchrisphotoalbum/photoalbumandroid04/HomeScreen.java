package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.IOException;

import model.*;

public class HomeScreen extends AppCompatActivity {
    private ListView albumsListView;
    private Toolbar myToolbar;
//    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Album a = new Album("A1");
        User u = new User();
        u.addAlbum(a);

        try {
            User.write(getApplicationContext(), u);
            u = User.read(getApplicationContext());
        } catch (IOException | ClassNotFoundException o) {
            o.printStackTrace();
        }


        //prints the list of albums in the console.
        Log.d("message", u.toString());

        albumsListView = (ListView) findViewById(R.id.albums_list_view);
        String[] foods = {"Bacon", "Ham", "Tuna", "Candy", "Meatball", "Potato"};
        ListAdapter listAdapter = new CustomAdapter(this, foods);
        albumsListView.setAdapter(listAdapter);

        albumsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String food = String.valueOf(adapterView.getItemAtPosition(position));
                        Toast.makeText(HomeScreen.this, food, Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
}
