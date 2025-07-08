package com.poly.customerdomain.model.entity.valueobject;

import java.util.Objects;

public class ImageUrl {

    private final String url;

    public ImageUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageUrl)) return false;
        ImageUrl imageUrl = (ImageUrl) o;
        return Objects.equals(url, imageUrl.url);
    }

    public static ImageUrl from(String imageUrl){
        return new ImageUrl(imageUrl);
    }
}
