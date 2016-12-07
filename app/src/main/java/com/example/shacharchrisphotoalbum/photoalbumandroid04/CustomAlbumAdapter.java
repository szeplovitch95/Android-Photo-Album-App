package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import model.Album;
import model.User;

class CustomAlbumAdapter extends ArrayAdapter<Album>{
    private List<Album> albumList;
    private Context mContext;
    private User user;

    public CustomAlbumAdapter(Context context, List<Album> albums, User user) {
        super(context, R.layout.custom_album_row, albums);
        albumList = albums;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_album_row, parent, false);

        String singleAlbumItem = getItem(position).getAlbumName();
        TextView albumTextView = (TextView) customView.findViewById(R.id.albumNameTextView);
        TextView albumSizeTextView = (TextView) customView.findViewById(R.id.albumSize);
        albumTextView.setText(singleAlbumItem);
        albumSizeTextView.setText("Size: " + getItem(position).getSize() + "");
        ImageView deleteBtn = (ImageView) customView.findViewById(R.id.removeAlbumButton);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumList.remove(position);
                notifyDataSetChanged();
                try {
                    saveData();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteBtn.setFocusable(false);
        deleteBtn.setFocusableInTouchMode(false);


        return customView;
    }

    public void saveData() throws IOException, ClassNotFoundException {
        User.write(mContext, user);
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

}
