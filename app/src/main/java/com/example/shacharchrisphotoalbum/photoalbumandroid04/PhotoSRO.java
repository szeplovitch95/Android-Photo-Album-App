package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Photo;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class PhotoSRO {
    private Photo photo;
    private Album album;
    public static List<String> allphotos = new ArrayList<String>();

    public PhotoSRO(Photo photo, Album album) {
        this.photo = photo;
        this.album = album;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
