package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class Perfil extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfil, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        Button btnEditar = view.findViewById(R.id.btnEditarPerfil);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        btnEditar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Editar perfil", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logout realizado", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}