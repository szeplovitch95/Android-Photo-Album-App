package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import model.Album;
import model.Photo;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class SlideshowScreen extends AppCompatActivity {
    private Button previousBtn;
    private Button nextBtn;
    ImageView imageView;
    User user;
    Album currentAlbum;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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
        previousBtn = (Button) findViewById(R.id.previousBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapFactory.decodeFile(currentAlbum.getPhotos().get(currentIndex).getImageRef()));

        previousBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentIndex - 1 < 0) return;
                        Photo p = currentAlbum.previousPhoto(currentAlbum.getPhotos().get(--currentIndex));
                        if(p == null) return;
                        imageView.setImageBitmap(BitmapFactory.decodeFile(currentAlbum.getPhotos().get(currentIndex).getImageRef()));
                    }
                }
        );

        nextBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentIndex + 1 >= currentAlbum.getSize()) return;
                        Photo p = currentAlbum.getPhotos().get(++currentIndex);
                        if(p == null) return;
                        imageView.setImageBitmap(BitmapFactory.decodeFile(currentAlbum.getPhotos().get(currentIndex).getImageRef()));
                    }
                }
        );
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }
}
