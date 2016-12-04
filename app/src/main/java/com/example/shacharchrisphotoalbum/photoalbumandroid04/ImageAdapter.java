package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
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

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public Integer getImgID(int position){
        return mThumbIds[position];
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7,
            R.mipmap.sample_0, R.mipmap.sample_1,
            R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7,
            R.mipmap.sample_0, R.mipmap.sample_1,
            R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7
    };
}
