package com.example.shacharchrisphotoalbum.photoalbumandroid04;

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
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> mThumbIds = new ArrayList<Integer>();

    public ImageAdapter(Context c) {
        mContext = c;
        mThumbIds.add(R.mipmap.sample_0);
        mThumbIds.add(R.mipmap.sample_1);
        mThumbIds.add(R.mipmap.sample_2);
        mThumbIds.add(R.mipmap.sample_3);
        mThumbIds.add(R.mipmap.sample_4);
        mThumbIds.add(R.mipmap.sample_5);
        mThumbIds.add(R.mipmap.sample_6);
        mThumbIds.add(R.mipmap.sample_7);
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Integer getItem(int position) {
        return this.mThumbIds.get(position);
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

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }


    public Integer getImgID(int position){
        return mThumbIds.get(position);
    }

    public List<Integer> getmThumbIds() {
        return this.mThumbIds;
    }

    public void addPicture(Integer ref) {
        mThumbIds.add(ref);
    }

    public void removePicture(Integer ref) {
        mThumbIds.remove(ref);
    }
}
