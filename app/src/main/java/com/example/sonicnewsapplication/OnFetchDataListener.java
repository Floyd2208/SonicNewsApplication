package com.example.sonicnewsapplication;

import com.example.sonicnewsapplication.Models.NewsHeadlines;

import java.util.List;

//Interface created for fetching data from news Api
public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list,String message);
    void onError(String message);
}
