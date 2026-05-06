package com.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Perfil extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfil, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        Button btnEditar = view.findViewById(R.id.btnEditarPerfil);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        TextView txtNome = view.findViewById(R.id.txtNome);
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        TextView txtBio = view.findViewById(R.id.txtBio);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            User user = db.userDao().getLastUser();

            if (user != null) {
                String primeiroNome = user.nome != null ? user.nome.split(" ")[0] : "Usuário";
                String usernameFormatado = (user.usuario != null && !user.usuario.isEmpty()) ? "@" + user.usuario.replace("@", "") : "@usuario";
                String idade = (user.idade != null && !user.idade.isEmpty()) ? user.idade : "--";
                String peso = (user.peso != null && !user.peso.isEmpty()) ? user.peso : "--";
                String altura = (user.altura != null && !user.altura.isEmpty()) ? user.altura : "--";

                String bioText = "\"Idade: " + idade + " Peso: " + peso + " Altura: " + altura + "\"";

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (txtNome != null) txtNome.setText(primeiroNome);
                        if (txtUsername != null) txtUsername.setText(usernameFormatado);
                        if (txtBio != null) txtBio.setText(bioText);
                    });
                }
            }
        }).start();

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