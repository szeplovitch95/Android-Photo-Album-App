package model;


import java.io.Serializable;

public class Tag implements Serializable {
    private int _id;
    private String tagType;
    private String tagValue;

    public Tag(String tagType, String tagValue) {
        this.tagType = tagType;
        this.tagValue = tagValue;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public boolean equals(Tag t) {
        if (t == null) {
            return false;
        }

        return t.getTagType().equals(this.tagType) && t.getTagValue().equals(this.tagValue);
    }
}
