package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import model.Album;
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

//        if(photoList.size() == 0) {
//            return customView;
//        }

        //File singlePhotoFile = getItem(position).getImageFile();
        ImageView albumPic = (ImageView) customView.findViewById(R.id.albumPic);

        FileInputStream in;
        BufferedInputStream buf;

        try {
            in = new FileInputStream("C:\\Users\\Dror\\Desktop\\bowtie.png");
            buf = new BufferedInputStream(in);
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            albumPic.setImageBitmap(bMap);

            if(in != null) {
                in.close();
            }
            if(buf != null) {
                buf.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return customView;
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(mContext, user);
    }
}
