package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private File imageFile;
    private List<Tag> tags;

    public Photo(File imageFile) {
        this.imageFile = imageFile;
        this.tags = new ArrayList<Tag>();
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
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
