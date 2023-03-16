package com.example.casestudy.repository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RepositoryImpl implements Repository {

    @Override
    public Single<JSONObject> getDataFromUrl(ArrayList<String> links, JSONObject jsonObject) {
        return Observable.fromIterable(links)
                .map(urlString -> {
                    JSONObject jsonItem = new JSONObject();
                    Document doc = Jsoup.connect(urlString).get();
                    jsonItem.put("url", urlString);
                    jsonItem.put("title", doc.title());
                    return jsonItem;
                })
                .toList()
                .map(listJson -> {
                    JSONArray array = new JSONArray();
                    listJson.forEach(array::put);
                    jsonObject.put("links", array);
                    return jsonObject;
                });
    }
}
