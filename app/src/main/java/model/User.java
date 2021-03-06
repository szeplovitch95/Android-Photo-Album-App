package model;

import android.content.Context;
import android.net.Uri;

import com.example.shacharchrisphotoalbum.photoalbumandroid04.PhotoSRO;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class User implements Serializable {
    public static final String storeDir = "/data/user/0/com.example.shacharchrisphotoalbum.photoalbumandroid04/files/";
    public static final String storeFile = "albums.dat";
    private static final long serialVersionUID = -1984438222161407315L;

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

    public List<PhotoSRO> searchPhotos(String type, String value) {
        List<PhotoSRO> resultPhotos = new ArrayList<PhotoSRO>();

        for(Album a : albums) {
            INNER: for(Photo p : a.getPhotos()) {
                for(Tag t : p.getTags()) {
                    if(t.getTagType().equalsIgnoreCase(type)) {
                        if(t.getTagValue().startsWith(value)) {
                            resultPhotos.add(new PhotoSRO(p, a));
                            PhotoSRO.allphotos.add(p.getImageRef());
                            break INNER;
                        }
                    }
                }
            }
        }

        return resultPhotos;
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