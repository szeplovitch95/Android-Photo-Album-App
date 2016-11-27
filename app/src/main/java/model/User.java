package model;

import java.util.ArrayList;
import java.util.List;

public class User {
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
}
