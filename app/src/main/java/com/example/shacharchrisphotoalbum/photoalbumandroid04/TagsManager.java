package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Photo;
import model.Tag;
import model.User;

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
    private SwipeMenuListView tagsListView;
    private List<String> tags = new ArrayList<String>();
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

        for(Tag t : currentPhoto.getTags()) {
            tags.add(" - " + t.getTagType()+ ", " + t.getTagValue());
        }

        tagsListView = (SwipeMenuListView) findViewById(R.id.tagsListView);
        singleImageView.setImageResource(currentPhoto.getImageRef());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
        tagsListView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                createMenu1(menu);
            }

            private void createMenu1(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(24);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_white_24px);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        tagsListView.setMenuCreator(creator);

        tagsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        editTag(position);
                        break;
                    case 1:
                        removeTag(position);
                        break;
                }

                return false;
            }
        });


        addTagBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTag(12);
                    }
                }
        );
    }

    public void addTag(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 12);
        Intent intent = new Intent(getApplicationContext(), AddEditTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_TAG_CODE);
    }

    public void editTag(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString("tagType", currentPhoto.getTags().get(pos).getTagType());
        bundle.putString("tagValue", currentPhoto.getTags().get(pos).getTagValue());
        bundle.putInt("pos", pos);

        Intent intent = new Intent(getApplicationContext(), AddEditTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_TAG_CODE);
    }

    public void removeTag(int pos) {
        adapter.remove(tags.get(pos));
        currentPhoto.removeTag(currentPhoto.getTags().get(pos));

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
            adapter.add(tagType + ", " + tagValue);
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
            int pos = bundle.getInt("pos");
            Tag t = currentPhoto.getTags().get(pos);
            t.setTagType(tagType);
            t.setTagValue(tagValue);
            currentPhoto.getTags().set(pos, t);

            tags = new ArrayList<String>();
            for(Tag t1 : currentPhoto.getTags()) {
                tags.add(t1.getTagType()+ ", " + t1.getTagValue());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
            tagsListView.setAdapter(adapter);
        }

        try {
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
