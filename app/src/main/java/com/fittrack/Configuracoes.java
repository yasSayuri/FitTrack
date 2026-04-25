package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class Configuracoes extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuracoes, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        LinearLayout btnMudarEmail = view.findViewById(R.id.btnMudarEmail);
        LinearLayout btnMudarSenha = view.findViewById(R.id.btnMudarSenha);
        LinearLayout btnNotificacoes = view.findViewById(R.id.btnNotificacoes);
        LinearLayout btnTermos = view.findViewById(R.id.btnTermos);
        SwitchCompat switchModoEscuro = view.findViewById(R.id.switchModoEscuro);

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        btnMudarEmail.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Abrir tela de Email", Toast.LENGTH_SHORT).show();
        });

        btnMudarSenha.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Abrir tela de Senha", Toast.LENGTH_SHORT).show();
        });

        btnNotificacoes.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Configurar Notificações", Toast.LENGTH_SHORT).show();
        });

        btnTermos.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Lendo os Termos", Toast.LENGTH_SHORT).show();
        });

        switchModoEscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}