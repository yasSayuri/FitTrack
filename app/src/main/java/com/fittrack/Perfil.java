package com.fittrack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Perfil extends Fragment {

    private TextView txtNome, txtUsername, txtBio;
    private ImageView imgProfile;
    private User usuarioAtual;
    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            imgProfile.setImageURI(selectedImage);
                            salvarFotoNoBanco(selectedImage.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfil, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        Button btnEditar = view.findViewById(R.id.btnEditarPerfil);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        CardView btnAddPhoto = view.findViewById(R.id.btnAddPhoto);

        txtNome = view.findViewById(R.id.txtNome);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtBio = view.findViewById(R.id.txtBio);
        imgProfile = view.findViewById(R.id.imgProfile);

        carregarDadosUsuario();

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerLauncher.launch(intent);
        });

        btnEditar.setOnClickListener(v -> {
            if (usuarioAtual != null) {
                mostrarPopUpEdicao();
            }
        });

        btnLogout.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), MainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void carregarDadosUsuario() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            usuarioAtual = db.userDao().getLastUser();

            if (usuarioAtual != null && getActivity() != null) {
                getActivity().runOnUiThread(this::atualizarTextosDaTela);
            }
        }).start();
    }

    private void atualizarTextosDaTela() {
        String primeiroNome = usuarioAtual.nome != null ? usuarioAtual.nome.split(" ")[0] : "Usuário";
        String usernameFormatado = (usuarioAtual.usuario != null && !usuarioAtual.usuario.isEmpty()) ? "@" + usuarioAtual.usuario.replace("@", "") : "@usuario";
        String idade = (usuarioAtual.idade != null && !usuarioAtual.idade.isEmpty()) ? usuarioAtual.idade : "--";
        String peso = (usuarioAtual.peso != null && !usuarioAtual.peso.isEmpty()) ? usuarioAtual.peso : "--";
        String altura = (usuarioAtual.altura != null && !usuarioAtual.altura.isEmpty()) ? usuarioAtual.altura : "--";

        String bioText = "\"Idade: " + idade + " Peso: " + peso + " Altura: " + altura + "\"";

        txtNome.setText(primeiroNome);
        txtUsername.setText(usernameFormatado);
        txtBio.setText(bioText);

        if (usuarioAtual.fotoPerfil != null && !usuarioAtual.fotoPerfil.isEmpty()) {
            imgProfile.setImageURI(Uri.parse(usuarioAtual.fotoPerfil));
        }
    }

    private void salvarFotoNoBanco(String uriFoto) {
        new Thread(() -> {
            if (usuarioAtual != null && getContext() != null) {
                usuarioAtual.fotoPerfil = uriFoto;
                AppDatabase.getInstance(getContext()).userDao().update(usuarioAtual);
            }
        }).start();
    }

    private void mostrarPopUpEdicao() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtNome = dialog.findViewById(R.id.edtEditNome);
        EditText edtUsuario = dialog.findViewById(R.id.edtEditUsuario);
        EditText edtIdade = dialog.findViewById(R.id.edtEditIdade);
        EditText edtPeso = dialog.findViewById(R.id.edtEditPeso);
        EditText edtAltura = dialog.findViewById(R.id.edtEditAltura);

        Button btnCancelar = dialog.findViewById(R.id.btnCancelarEdicao);
        Button btnAtualizar = dialog.findViewById(R.id.btnAtualizarPerfil);

        edtNome.setText(usuarioAtual.nome);
        edtUsuario.setText(usuarioAtual.usuario);
        edtIdade.setText(usuarioAtual.idade);
        edtPeso.setText(usuarioAtual.peso);
        edtAltura.setText(usuarioAtual.altura);

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnAtualizar.setOnClickListener(v -> {
            usuarioAtual.nome = edtNome.getText().toString().trim();
            usuarioAtual.usuario = edtUsuario.getText().toString().trim();
            usuarioAtual.idade = edtIdade.getText().toString().trim();
            usuarioAtual.peso = edtPeso.getText().toString().trim();
            usuarioAtual.altura = edtAltura.getText().toString().trim();

            new Thread(() -> {
                AppDatabase.getInstance(getContext()).userDao().update(usuarioAtual);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        atualizarTextosDaTela();
                        Toast.makeText(getContext(), "Perfil atualizado!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }
            }).start();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}