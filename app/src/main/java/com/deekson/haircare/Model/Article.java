package com.deekson.haircare.Model;

public class Article {
    String article,title;

    public Article(){

    }
    public Article(String article, String title) {
        this.article = article;
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public String getTitle() {
        return title;
    }
}
