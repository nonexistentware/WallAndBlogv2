package com.nonexistentware.wallandblogv2.Model;

public class WallpaperItem {
    private String imageLink;
    private String categoryId;
    private String artistId;
    private String artistName;
    private long viewCount;

    public WallpaperItem() {
    }

    public WallpaperItem(String imageLink, String artistId, String artistName, String categoryId) {
        this.imageLink = imageLink;
        this.categoryId = categoryId;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
