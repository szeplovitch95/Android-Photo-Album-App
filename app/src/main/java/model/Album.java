package model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Album implements Serializable {
    private String albumName;
    private List<Photo> photos = new ArrayList<Photo>();
    private int size =  0;

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Photo getPhotoByRef(String ref) {
        for(Photo p: photos) {
            if(p.getImageRef().equals(ref)) {
                return p;
            }
        }

        return null;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        size++;
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        size--;
    }

    public boolean photoExists(Photo photo) {
        return photos.contains(photo);
    }

    public void removePhotoByRef(Uri ref) {
        for(Photo p : photos) {
            if(p.getImageRef().equals(ref)) {
                removePhoto(p);
                return;
            }
        }
    }

    public Photo previousPhoto(Photo p) {
        if (p == null) {
            return null;
        }

        if (size < 2) {
            return p;
        }

        for (Photo photo : photos) {
            if (p.getImageRef().equals(photo.getImageRef())) {
                if (photos.indexOf(photo) != 0) {
                    return photos.get(photos.indexOf(photo) - 1);
                }
            }
        }

        return p;
    }

    public Photo nextPhoto(Photo p) {
        if (p == null) {
            return null;
        }

        if (size < 2) {
            return p;
        }

        for (Photo photo : photos) {
            if (p.getImageRef().equals(photo.getImageRef())) {
                if (photos.indexOf(photo) != photos.size() - 1) {
                    return photos.get(photos.indexOf(photo) + 1);
                }
            }
        }

        return p;
    }
}


