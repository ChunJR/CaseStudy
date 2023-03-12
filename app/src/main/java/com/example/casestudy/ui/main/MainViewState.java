package com.example.casestudy.ui.main;

public abstract class MainViewState {
    public static final class onLoading extends MainViewState {
        public static final onLoading INSTANCE = new onLoading();
        private onLoading() {}
    }
    public static final class onSuccess extends MainViewState {
        private final String jsonString;
        public onSuccess(String jsonString) {
            this.jsonString = jsonString;
        }
        public String getJsonString() {
            return this.jsonString;
        }
    }
    public static final class onError extends MainViewState {
        private final String message;
        public onError(String message) {
            this.message = message;
        }
        public String getMessage() {
            return this.message;
        }
    }
}