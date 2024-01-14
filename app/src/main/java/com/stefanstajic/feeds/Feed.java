package com.stefanstajic.feeds;

public class Feed {
    private String imageUrl;
    private String caption;

    private long timestamp;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "imageUrl='" + imageUrl + '\'' +
                ", caption='" + caption + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
