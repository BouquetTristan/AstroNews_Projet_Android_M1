package com.astronews.core.model.News;

import java.util.Locale;

public class Article {
    private String id;
    private String url;
    private String title;
    private String description;
    private String body;
    private String datePublished;
    private String language;
    private String is_safe;
    private ArticleProvider provider;
    private ArticleThumbnail image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIs_safe() {
        return is_safe;
    }

    public void setIs_safe(String is_safe) {
        this.is_safe = is_safe;
    }

    public ArticleProvider getProvider() {
        return provider;
    }

    public void setProvider(ArticleProvider provider) {
        this.provider = provider;
    }

    public ArticleThumbnail getImage() {
        return image;
    }

    public void setImage(ArticleThumbnail image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return String.format(Locale.FRANCE, title + " by " + provider + " on " + datePublished);
    }
}
