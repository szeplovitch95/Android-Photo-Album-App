package model;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 4108914196952100996L;
    public static final String storeDir = "Users";
    public static final String storeFile = "albums";


    private List<Album> albums;

    public User() {
        albums = new ArrayList<Album>();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album a) {
        albums.add(a);
    }

    public void removeAlbum(Album a) {
        albums.remove(a);
    }

    public boolean albumExists(Album a) {
        return albums.contains(a);
    }

    public Album getAlbumByName(String aName) {
        for(Album a: albums) {
            if(aName.equals(a.getAlbumName())) {
                return a;
            }
        }

        return null;
    }

    public String toString() {
        String albumList = "";

        if (albums == null) {
            return "No users exist.";
        }

        for (Album a : albums) {
            albumList += a.getAlbumName() + "\n";
        }
        return albumList;
    }

    public static User read(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(storeFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        User user = (User) ois.readObject();
        ois.close();
        return user;
    }

    public static void write(Context context, User user) throws IOException {
        FileOutputStream fos = context.openFileOutput(storeFile, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(user);
        oos.close();
    }
}
