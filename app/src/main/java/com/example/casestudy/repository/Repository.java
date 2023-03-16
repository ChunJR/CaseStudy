package com.example.casestudy.repository;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Single;

public interface Repository {
    Single<JSONObject> getDataFromUrl(ArrayList<String> links, JSONObject jsonObject);
}
