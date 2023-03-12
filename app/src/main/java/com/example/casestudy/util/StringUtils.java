package com.example.casestudy.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.annotation.Nullable;

public class StringUtils {
    public static boolean isEmpty(@Nullable String in) {
        return in == null || in.isEmpty();
    }

    public static boolean isValidURL(String url){
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
