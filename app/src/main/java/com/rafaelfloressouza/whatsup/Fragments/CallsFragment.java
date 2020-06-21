package com.rafaelfloressouza.whatsup.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rafaelfloressouza.whatsup.R;


public class CallsFragment extends Fragment {

    View viewToInflate;

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewToInflate = inflater.inflate(R.layout.calls_fragment, container, false);
        return viewToInflate;
    }

}
