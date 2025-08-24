package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.InvalidCustomerImageException;

public class ImageUrl {

    private final String url;

    public ImageUrl(String url) {
        if (url == null || url.isEmpty()) {
            empty();
        }
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static ImageUrl from(String imageUrl){
        return new ImageUrl(imageUrl);
    }

    public static ImageUrl empty(){
        return new ImageUrl("https://res.cloudinary.com/dhbjvvn87/image/upload/v1751976713/ldbper0tbr5zituky1zi.webp");
    }
}
