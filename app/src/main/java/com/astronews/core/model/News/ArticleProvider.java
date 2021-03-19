package com.astronews.core.model.News;

import java.util.Locale;

public class ArticleProvider {
    private String name;
    private String favIconBase64Encoding;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavIconBase64Encoding() {
        return favIconBase64Encoding;
    }

    public void setFavIconBase64Encoding(String favIconBase64Encoding) {
        this.favIconBase64Encoding = favIconBase64Encoding;
    }

    @Override
    public String toString() {
        return String.format(Locale.FRANCE, name);
    }
}
