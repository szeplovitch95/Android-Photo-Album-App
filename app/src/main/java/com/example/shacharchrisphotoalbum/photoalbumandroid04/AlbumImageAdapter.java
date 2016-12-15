package com.example.shacharchrisphotoalbum.photoalbumandroid04;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class AlbumImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mThumbIds = new ArrayList<String>();

    public AlbumImageAdapter(Context c, List<String> images) {
        mContext = c;
        this.mThumbIds = images;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public String getItem(int position) {
        return this.mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }



        imageView.setImageBitmap(BitmapFactory.decodeFile(mThumbIds.get(position)));
        return imageView;
    }

    public String getImgID(int position){
        return mThumbIds.get(position);
    }

    public List<String> getmThumbIds() {
        return this.mThumbIds;
    }

    public void setmThumbIds(List<String> photos) {
        this.mThumbIds = photos;
    }

    public void addPicture(String ref) {
        mThumbIds.add(ref);
    }

    public void removePicture(Uri ref) {
        mThumbIds.remove(ref);
    }
}
