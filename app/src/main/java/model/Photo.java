package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private Integer imageRef;
    private List<Tag> tags;

    public Photo(Integer imageFile) {
        this.imageRef = imageFile;
        this.tags = new ArrayList<Tag>();
    }

    public Integer getImageRef() {
        return imageRef;
    }

    public void setImageRef(Integer imageFile) {
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
