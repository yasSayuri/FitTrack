package com.fittrack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.fittrack.entidades.Treino;
import com.fittrack.entidades.User;
import java.util.List;

public class Perfil extends Fragment {

    private TextView txtNome, txtUsername, txtBio, txtCountTreinos, txtTotalDuracao, txtTotalCalorias;
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

        Button btnEditar = view.findViewById(R.id.btnEditarPerfil);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        CardView btnAddPhoto = view.findViewById(R.id.btnAddPhoto);

        txtNome = view.findViewById(R.id.txtNome);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtBio = view.findViewById(R.id.txtBio);
        imgProfile = view.findViewById(R.id.imgProfile);

        txtCountTreinos = view.findViewById(R.id.txtCountTreinos);
        txtTotalDuracao = view.findViewById(R.id.txtTotalDuracao);
        txtTotalCalorias = view.findViewById(R.id.txtTotalCalorias);

        carregarDadosUsuario();

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerLauncher.launch(intent);
        });

        btnEditar.setOnClickListener(v -> {
            if (usuarioAtual != null) mostrarPopUpEdicao();
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

    @Override
    public void onResume() {
        super.onResume();
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        final Context context = getContext();
        final Activity activity = getActivity();
        if (context == null || activity == null) return;

        new Thread(() -> {
            SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);
            if (userIdLogado == -1) return;

            try {
                AppDatabase db = AppDatabase.getInstance(context);
                List<Treino> lista = db.treinoDao().getTreinosByUserId(userIdLogado);

                int totalTreinos = lista.size();
                int totalMinutos = 0;
                int totalCalorias = 0;

                for (Treino t : lista) {
                    try {
                        if (t.duracao != null && t.duracao.contains(":")) {
                            String[] partes = t.duracao.split(":");
                            totalMinutos += (Integer.parseInt(partes[0]) * 60) + Integer.parseInt(partes[1]);
                        }
                    } catch (Exception ignored) {}

                    try {
                        if (t.calorias != null) {
                            String calNumeros = t.calorias.replaceAll("[^0-9]", "");
                            if (!calNumeros.isEmpty()) totalCalorias += Integer.parseInt(calNumeros);
                        }
                    } catch (Exception ignored) {}
                }

                int horas = totalMinutos / 60;
                int minutos = totalMinutos % 60;
                final String txtHorasFinal = horas + "h " + minutos + "m";

                final int finalTotalTreinos = totalTreinos;
                final int finalTotalCalorias = totalCalorias;

                activity.runOnUiThread(() -> {
                    txtCountTreinos.setText(String.valueOf(finalTotalTreinos));
                    txtTotalDuracao.setText(txtHorasFinal);
                    txtTotalCalorias.setText(finalTotalCalorias + "k");
                });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void carregarDadosUsuario() {
        final Context context = getContext();
        final Activity activity = getActivity();
        if (context == null || activity == null) return;

        new Thread(() -> {
            SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userIdLogado = prefs.getInt("userId", -1);
            if (userIdLogado == -1) return;

            try {
                AppDatabase db = AppDatabase.getInstance(context);
                usuarioAtual = db.userDao().getUserById(userIdLogado);

                if (usuarioAtual != null) activity.runOnUiThread(this::atualizarTextosDaTela);
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void atualizarTextosDaTela() {
        String primeiroNome = usuarioAtual.nome != null ? usuarioAtual.nome.split(" ")[0] : "Usuário";
        String username = (usuarioAtual.usuario != null && !usuarioAtual.usuario.isEmpty()) ? "@" + usuarioAtual.usuario.replace("@", "") : "@usuario";
        String bio = "\"Idade: " + (usuarioAtual.idade != null ? usuarioAtual.idade : "--") +
                " Peso: " + (usuarioAtual.peso != null ? usuarioAtual.peso : "--") +
                " Altura: " + (usuarioAtual.altura != null ? usuarioAtual.altura : "--") + "\"";

        txtNome.setText(primeiroNome);
        txtUsername.setText(username);
        txtBio.setText(bio);

        if (usuarioAtual.fotoPerfil != null && !usuarioAtual.fotoPerfil.isEmpty()) {
            imgProfile.setImageURI(Uri.parse(usuarioAtual.fotoPerfil));
        }
    }

    private void salvarFotoNoBanco(String uriFoto) {
        final Context context = getContext();
        if (context == null) return;
        new Thread(() -> {
            if (usuarioAtual != null) {
                usuarioAtual.fotoPerfil = uriFoto;
                AppDatabase.getInstance(context).userDao().update(usuarioAtual);
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

        edtNome.setText(usuarioAtual.nome);
        edtUsuario.setText(usuarioAtual.usuario);
        edtIdade.setText(usuarioAtual.idade);
        edtPeso.setText(usuarioAtual.peso);
        edtAltura.setText(usuarioAtual.altura);

        dialog.findViewById(R.id.btnCancelarEdicao).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnAtualizarPerfil).setOnClickListener(v -> {
            usuarioAtual.nome = edtNome.getText().toString().trim();
            usuarioAtual.usuario = edtUsuario.getText().toString().trim();
            usuarioAtual.idade = edtIdade.getText().toString().trim();
            usuarioAtual.peso = edtPeso.getText().toString().trim();
            usuarioAtual.altura = edtAltura.getText().toString().trim();

            final Context context = getContext();
            final Activity activity = getActivity();
            if (context == null || activity == null) return;

            new Thread(() -> {
                AppDatabase.getInstance(context).userDao().update(usuarioAtual);
                activity.runOnUiThread(() -> {
                    atualizarTextosDaTela();
                    Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }).start();
        });
        dialog.show();

        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}