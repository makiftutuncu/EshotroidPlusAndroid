package com.mehmetakiftutuncu.eshotroid.fragments;

import android.support.v4.app.Fragment;

import com.mehmetakiftutuncu.eshotroid.models.ContentState;

public abstract class FragmentWithContentState extends Fragment implements ContentState.HasContentState {
    protected ContentState contentState;
}
