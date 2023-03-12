package com.example.casestudy.ui.main.ui.main;

import com.example.casestudy.repository.Repository;
import com.example.casestudy.ui.main.MainViewModel;
import com.example.casestudy.ui.main.MainViewState;
import com.example.casestudy.ui.main.util.LiveDataTestUtil;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MainViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel mainViewModel;
    private final Repository repository = mock(Repository.class);

    @Before
    public void setUp() {
        mainViewModel = new MainViewModel();
    }

    @Test
    public void onSubmitClick_whenCommentIsEmpty_thenStateIsLoading() {
        mainViewModel.onSubmitClick("");
        MainViewState actual = LiveDataTestUtil.getOrAwaitValue(mainViewModel.getState());
        assertEquals(MainViewState.onLoading.INSTANCE, actual);
    }

    @Test
    public void onSubmitClick_whenCommentHasMentioned_thenStateIsSuccess() {
        mainViewModel.onSubmitClick("do you know \"@billgates?");
        MainViewState actual = LiveDataTestUtil.getOrAwaitValue(mainViewModel.getState());

        String expected = "{\"mentions\":[\"billgates\"]}";
        assertEquals(expected, ((MainViewState.onSuccess) actual).getJsonString());
    }

    @Test
    public void onSubmitClick_whenCommentHasUrl_thenStateIsSuccess() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<String> links = new ArrayList<>();
        links.add("https://olympics.com/tokyo-2020/en/");
        String expected = "{\"links\":[{\"url\":\"https://olympics.com/tokyo-2020/en/\", \"title\":\"Tokyo 2020\"}]}";

        // Preparation: mock DummyService
        Mockito.doReturn(Single.just(expected)).when(repository).getDataFromUrl(links, jsonObject);

        TestObserver<JSONObject> testObserver =
                repository.getDataFromUrl(links, jsonObject).test();

        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(result -> result.toString().equals(expected));
//
//        mainViewModel.onSubmitClick("Olympics 2020 is happening; https://olympics.com/tokyo-2020/en/");
//        MainViewState actual = LiveDataTestUtil.getOrAwaitValue(mainViewModel.getState());
//
//        // Validation
//        assertEquals(expected, ((MainViewState.onSuccess) actual).getJsonString());
    }
}