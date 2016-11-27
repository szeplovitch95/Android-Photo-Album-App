package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, String[] foods) {
        super(context, R.layout.custom_row, foods);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        String singleFoodItem = getItem(position);
        TextView myText = (TextView) customView.findViewById(R.id.albumsTextView);
        ImageView imageView = (ImageView) customView.findViewById(R.id.albumPic);

        myText.setText(singleFoodItem);
        imageView.setImageResource(R.mipmap.bucky);
        return customView;
    }
}
