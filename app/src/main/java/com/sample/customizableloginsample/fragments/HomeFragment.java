package com.sample.customizableloginsample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.adapters.HorizontalAdapter;
import com.sample.customizableloginsample.adapters.MainVerticalAdapter;
import com.sample.customizableloginsample.models.Sport;


import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private ArrayList<Sport> _sportsList = new ArrayList<>();
    private RecyclerView _horizontalRecyclerView, _mainRecyclerview;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _sportsList.add(new Sport("NBA", R.drawable.nba));
        _sportsList.add(new Sport("Tennis", R.drawable.tennis));
        _sportsList.add(new Sport("Football", R.drawable.football));
        _sportsList.add(new Sport("Rugby", R.drawable.rugby));
        _sportsList.add(new Sport("UFC", R.drawable.ufc));
        _sportsList.add(new Sport("Badminton", R.drawable.nba));
        _sportsList.add(new Sport("Table Tennis", R.drawable.tennis));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.homeview_fragment, container, false);

        _horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);
        _mainRecyclerview = view.findViewById(R.id.mainRecyclerView);

        /*for horizontal
        recycler view */

        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(getActivity(), _sportsList,getwidthonepercent());
        _horizontalRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        llm.setAutoMeasureEnabled(false);
        _horizontalRecyclerView.setLayoutManager(llm);

        _horizontalRecyclerView.getLayoutParams().height = (int) getHeightonepercent();


        _horizontalRecyclerView.setAdapter(horizontalAdapter);


        /* for vertical recyclerview */
       MainVerticalAdapter mainVerticalAdapter = new MainVerticalAdapter(getActivity(), getHeightForRecyclerView());
        _mainRecyclerview.setHasFixedSize(true);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        llm.setAutoMeasureEnabled(false);
        _mainRecyclerview.setLayoutManager(llm1);

        //_mainRecyclerview.getLayoutParams().height = (int) getHeightonepercent();


        _mainRecyclerview.setAdapter(mainVerticalAdapter);
        return view;


    }

    public float getwidthonepercent() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float onepercent = (dpWidth * 45) / 100;
        return onepercent;
    }

    public float getHeightonepercent() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float onepercent = (dpHeight * 45) / 100;
        return onepercent;
    }


    public float getHeightForRecyclerView() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float onepercent = (dpHeight * 70) / 100;
        return onepercent;
    }




}

