package model;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class Photo implements Serializable {
    private String imageRef;
    private List<Tag> tags;

    public Photo(String imageFile) {
        this.imageRef = imageFile;
        this.tags = new ArrayList<Tag>();
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageFile) {
        this.imageRef = imageFile;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag t) {
        tags.add(t);
    }

    public void removeTag(Tag t) {
        tags.remove(t);
    }

    public boolean tagExists(Tag t) {
        for (Tag t1 : this.tags) {
            if (t1.equals(t)) {
                return true;
            }
        }

        return false;
    }
}
