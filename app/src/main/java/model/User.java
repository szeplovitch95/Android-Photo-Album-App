package model;

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

    private static final long serialVersionUID = 1L;
    public static final String storeDir = "Users";
    public static final String storeFile = "albums.dat";


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

    public static User read() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C://Users//Dror//AndroidStudioProjects//PhotoAlbumAndroid04//app//Users/albums.dat"));
        User user = (User) ois.readObject();
        ois.close();
        return user;
    }

    public static void write(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C://Users//Dror//AndroidStudioProjects//PhotoAlbumAndroid04//app//Users/albums.dat"));
        oos.writeObject(user);
        oos.close();
    }
}
