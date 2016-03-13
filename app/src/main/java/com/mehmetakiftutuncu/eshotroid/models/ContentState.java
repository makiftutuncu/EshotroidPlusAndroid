package com.mehmetakiftutuncu.eshotroid.models;

public enum ContentState {
    DATA,
    NO_DATA,
    LOADING,
    ERROR;

    public interface HasContentState {
        void initializeState();
        void changeStateTo(ContentState newState);
    }
}
