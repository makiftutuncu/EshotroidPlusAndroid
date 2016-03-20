package com.mehmetakiftutuncu.eshotroid.activities;

public interface Searchable {
    void initializeSearch();
    boolean isSearching();
    void toggleSearch(boolean isSearching);
    void onSearchQueryChanged(String query);
}
