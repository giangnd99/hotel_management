package com.poly.customerdomain.model.entity.valueobject;

public class ImageUrl {

    private final String url;

    public ImageUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static ImageUrl from(String imageUrl){
        return new ImageUrl(imageUrl);
    }
}
