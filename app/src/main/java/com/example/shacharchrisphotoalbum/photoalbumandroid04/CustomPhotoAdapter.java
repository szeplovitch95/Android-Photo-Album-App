package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Photo;
import model.User;

class CustomPhotoAdapter extends ArrayAdapter<Photo>{
    private List<Photo> photoList;
    private Context mContext;
    private User user;

    public CustomPhotoAdapter(Context context, List<Photo> photos, User user) {
        super(context, R.layout.custom_row, photos);
        photoList = photos;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

//        File singlePhotoFile = getItem(position).getImageFile();
        ImageView albumPic = (ImageView) customView.findViewById(R.id.albumPic);

//        albumPic.setImageResource(R.drawable.images);


        return customView;
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(mContext, user);
    }
}
