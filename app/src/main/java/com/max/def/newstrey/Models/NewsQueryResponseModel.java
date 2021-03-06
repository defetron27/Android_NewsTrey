package com.max.def.newstrey.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsQueryResponseModel
{
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<NewsQueryArticlesModel> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsQueryArticlesModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsQueryArticlesModel> articles) {
        this.articles = articles;
    }
}
