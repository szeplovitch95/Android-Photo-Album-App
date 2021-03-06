package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.DialogFragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Photo;
import model.Tag;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class TagsManager extends AppCompatActivity {
    public static final int ADD_TAG_CODE = 1;
    public static final int EDIT_TAG_CODE = 2;

    private User user;
    private int albumIndex;
    private Album currentAlbum;
    private Photo currentPhoto;
    private Toolbar toolbar;
    private ImageView singleImageView;
    private Button addTagBtn;
    private Button removeTagBtn;
    private Button editTagBtn;
    private Button removeBtn;
    private Button slideshowBtn;
    private Button moveBtn;

    private ListView tagsListView;
    private List<String> tags = new ArrayList<String>();
    int selectedIndex = -1;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_manager);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
        albumIndex = data.getInt("albumIndex");
        currentPhoto = currentAlbum.getPhotos().get(albumIndex);
        singleImageView = (ImageView) findViewById(R.id.singleImageVIew);
        addTagBtn = (Button) findViewById(R.id.addTagBtn);
        removeTagBtn = (Button) findViewById(R.id.tagRemoveBtn);
        editTagBtn = (Button) findViewById(R.id.editTagBtn);
        tagsListView = (ListView) findViewById(R.id.tagsListVIew);
        singleImageView.setImageBitmap(BitmapFactory.decodeFile(currentPhoto.getImageRef()));

        removeBtn = (Button) findViewById(R.id.removeBtn);
        slideshowBtn = (Button) findViewById(R.id.slideshowBtn);
        moveBtn = (Button) findViewById(R.id.moveBtn);


        for(Tag t : currentPhoto.getTags()) {
            tags.add(" - " + t.getTagType()+ ", " + t.getTagValue());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
        tagsListView.setAdapter(adapter);

        addTagBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTag();
                    }
                }
        );

        removeTagBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedIndex == -1) {
                            Bundle bundle = new Bundle();
                            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"A tag needs to be selected");
                            DialogFragment newFragment = new AlbumDialogFragment();
                            newFragment.setArguments(bundle);
                            newFragment.show(getFragmentManager(), "Album cannot be empty");
                            return;
                        }

                        removeTag();
                    }
                }
        );

        editTagBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedIndex == -1) {
                            Bundle bundle = new Bundle();
                            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"A tag needs to be selected");
                            DialogFragment newFragment = new AlbumDialogFragment();
                            newFragment.setArguments(bundle);
                            newFragment.show(getFragmentManager(), "Album cannot be empty");
                            return;
                        }

                        editTag();
                    }
                }
        );

        tagsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedIndex = position;
                        for (int i = 0; i < adapter.getCount(); i++) {
                            if(tagsListView.getChildAt(i) == null) break;
                            if(position == i ) {
                                tagsListView.getChildAt(i).setBackgroundColor(Color.rgb(22, 175, 202));
                            } else{
                                tagsListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    }
                }
        );

        removeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removePhoto();
                    }
                }
        );

        slideshowBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSlideshow();
                    }
                }
        );

        moveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePhoto();
                    }
                }
        );
    }

    public void removePhoto(){
        Log.d("Current photo", currentAlbum.getSize() + "");
        currentAlbum.removePhoto(currentPhoto);
        Log.d("Current photo", currentAlbum.getSize() + "");
        Intent intent = new Intent(getApplicationContext(), AlbumScreen.class);
        intent.putExtra("albumName", currentAlbum.getAlbumName());

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, 100);
    }

    public void movePhoto() {
        Bundle bundle = new Bundle();
        bundle.putString("albumName", currentAlbum.getAlbumName());
        Intent intent = new Intent(getApplicationContext(), MoveToAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, AlbumScreen.MOVE_TO_CODE);
    }

    public void openSlideshow() {
        Bundle bundle = new Bundle();
        bundle.putString("albumName", currentAlbum.getAlbumName());
        Intent intent = new Intent(getApplicationContext(), SlideshowScreen.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void addTag() {
        Intent intent = new Intent(getApplicationContext(), AddEditTag.class);
        startActivityForResult(intent, ADD_TAG_CODE);
    }

    public void editTag() {
        Bundle bundle = new Bundle();
        bundle.putString("tagType", currentPhoto.getTags().get(selectedIndex).getTagType());
        bundle.putString("tagValue", currentPhoto.getTags().get(selectedIndex).getTagValue());

        Intent intent = new Intent(getApplicationContext(), AddEditTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_TAG_CODE);
    }

    public void removeTag() {
        adapter.remove(tags.get(selectedIndex));
        currentPhoto.removeTag(currentPhoto.getTags().get(selectedIndex));
        selectedIndex = -1;

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            if(tagsListView.getChildAt(i) == null) break;
            tagsListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(getApplicationContext(), user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = data.getExtras();
        if(bundle == null) return;

        if(requestCode == ADD_TAG_CODE) {
            if(currentPhoto.tagExists(new Tag(bundle.getString("tagType"), bundle.getString("tagValue")))) {
                    Bundle info = new Bundle();
                    info.putString(AlbumDialogFragment.MESSAGE_KEY,"The tag value pair already exists.\n" +
                            "A photo may not have duplicate tags");
                    DialogFragment newFragment = new AlbumDialogFragment();
                    newFragment.setArguments(info);
                    newFragment.show(getFragmentManager(), "Album cannot be empty");
                    return;
            }
            String tagType = bundle.getString("tagType");
            String tagValue = bundle.getString("tagValue");
            currentPhoto.addTag(new Tag(tagType, tagValue));
            adapter.add(" - " + tagType + ", " + tagValue);
            tagsListView.setAdapter(adapter);
        }

        if(requestCode == EDIT_TAG_CODE) {
            if(currentPhoto.tagExists(new Tag(bundle.getString("tagType"), bundle.getString("tagValue")))) {
                Bundle info = new Bundle();
                info.putString(AlbumDialogFragment.MESSAGE_KEY,"The tag value pair already exists.\n" +
                        "A photo may not have duplicate tags");
                DialogFragment newFragment = new AlbumDialogFragment();
                newFragment.setArguments(info);
                newFragment.show(getFragmentManager(), "Album cannot be empty");
                return;
            }

            String tagType = bundle.getString("tagType");
            String tagValue = bundle.getString("tagValue");
            Tag t = currentPhoto.getTags().get(selectedIndex);
            t.setTagType(tagType);
            t.setTagValue(tagValue);
            currentPhoto.getTags().set(selectedIndex, t);

            tags = new ArrayList<String>();
            for(Tag t1 : currentPhoto.getTags()) {
                tags.add(" - " + t1.getTagType()+ ", " + t1.getTagValue());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
            tagsListView.setAdapter(adapter);
        }

        if(requestCode == AlbumScreen.MOVE_TO_CODE) {
            Bundle bundleData = data.getExtras();
            String albumRef = bundleData.getString("albumRefName");
            Album a = user.getAlbumByName(albumRef);
            a.addPhoto(currentPhoto);
            currentAlbum.removePhoto(currentPhoto);

            try {
                saveData();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), AlbumScreen.class);
            intent.putExtra("albumName", currentAlbum.getAlbumName());
            startActivity(intent);
        }

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
