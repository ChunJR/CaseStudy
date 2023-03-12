package com.example.casestudy.ui.main;

import com.example.casestudy.repository.Repository;
import com.example.casestudy.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<MainViewState> _state = new MutableLiveData<>();
    public LiveData<MainViewState> getState() {
        return this._state;
    }
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Repository repository = new Repository();

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public void onSubmitClick(String comment) {
        _state.setValue(MainViewState.onLoading.INSTANCE);
        if (!StringUtils.isEmpty(comment)) {
            try {
                convertComment(comment);
            } catch (JSONException e) {
                _state.setValue(new MainViewState.onError(e.getMessage()));
            }
        }
    }

    private void convertComment(String comment) throws JSONException {
        JSONObject json = new JSONObject();

        ArrayList<String> mentions = getMentionsIfExist(comment);
        if (!mentions.isEmpty()) {
            JSONArray array = convertMentionsToJsonArray(mentions);
            json.put("mentions", array);
        }

        ArrayList<String> links = getLinksIfExist(comment);
        if (!links.isEmpty()) {
            continueToConvertWithLinks(links, json);
        } else {
            _state.setValue(new MainViewState.onSuccess(json.toString()));
        }
    }

    private ArrayList<String> getMentionsIfExist(String comment) {
        ArrayList<String> mentions = new ArrayList<>();
        String[] parts = comment.split(" ");

        for (String item : parts) {
            if (item.contains("@")) {
                mentions.add(removeSymbol(item));
            }
        }

        return mentions;
    }

    private String removeSymbol(String item) {
        return item.replaceAll("[^a-zA-Z0-9]", "");
    }

    private ArrayList<String> getLinksIfExist(String comment) {
        ArrayList<String> links = new ArrayList<>();
        String[] parts = comment.split("\\s+");

        for (String item : parts) {
            if (StringUtils.isValidURL(item)) {
                links.add(item);
            }
        }

        return links;
    }

    private JSONArray convertMentionsToJsonArray(ArrayList<String> mentions) {
        JSONArray array = new JSONArray();
        mentions.forEach(array::put);
        return array;
    }

    private void continueToConvertWithLinks(ArrayList<String> links, JSONObject jsonObject) {
        Disposable disposables = repository.getDataFromUrl(links, jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> _state.setValue(new MainViewState.onSuccess(result.toString())));
        compositeDisposable.add(disposables);
    }
}