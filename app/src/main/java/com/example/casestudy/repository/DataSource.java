package com.example.casestudy.repository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public interface DataSource {
    Single<JSONObject> getDataFromUrl(ArrayList<String> links, JSONObject jsonObject);
}
