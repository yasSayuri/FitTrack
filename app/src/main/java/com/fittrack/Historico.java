package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

public class Historico extends Fragment {

    private int lastScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.historico, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        NestedScrollView scroll = view.findViewById(R.id.scrollHistorico);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (getActivity() instanceof Home) {
                View bottomBar = getActivity().findViewById(R.id.bottomBarContainer);

                if (scrollY > lastScrollY) {
                    bottomBar.animate().translationY(bottomBar.getHeight()).setDuration(200);
                } else if (scrollY < lastScrollY) {
                    bottomBar.animate().translationY(0).setDuration(200);
                }

                lastScrollY = scrollY;
            }
        });

        return view;
    }
}