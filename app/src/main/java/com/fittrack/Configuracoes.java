package com.fittrack;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.fittrack.conexao.AppDatabase;
import com.fittrack.entidades.User;

public class Configuracoes extends Fragment {

    private User usuarioAtual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuracoes, container, false);

        ImageView btnVoltar = view.findViewById(R.id.btnVoltar);
        LinearLayout btnMudarEmail = view.findViewById(R.id.btnMudarEmail);
        LinearLayout btnMudarSenha = view.findViewById(R.id.btnMudarSenha);
        LinearLayout btnTermos = view.findViewById(R.id.btnTermos);
        LinearLayout btnDeletarConta = view.findViewById(R.id.btnDeletarConta);
        SwitchCompat switchModoEscuro = view.findViewById(R.id.switchModoEscuro);
        SwitchCompat switchNotificacoes = view.findViewById(R.id.switchNotificacoes);

        carregarUsuario();

        btnVoltar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().findViewById(R.id.nav_home).performClick();
            }
        });

        btnMudarEmail.setOnClickListener(v -> {
            if (usuarioAtual != null) mostrarDialogEmail();
        });

        btnMudarSenha.setOnClickListener(v -> {
            if (usuarioAtual != null) mostrarDialogSenha();
        });

        btnTermos.setOnClickListener(v -> mostrarDialogTermos());

        btnDeletarConta.setOnClickListener(v -> {
            if (usuarioAtual != null) mostrarDialogDeletarConta();
        });

        switchModoEscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
            }
        });

        switchNotificacoes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Notificações Ativadas", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Notificações Desativadas", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void carregarUsuario() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            usuarioAtual = db.userDao().getLastUser();
        }).start();
    }

    private void mostrarDialogEmail() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_mudar_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtNovoEmail = dialog.findViewById(R.id.edtNovoEmail);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelarEmail);
        Button btnAtualizar = dialog.findViewById(R.id.btnAtualizarEmail);

        if (usuarioAtual.email != null) {
            edtNovoEmail.setText(usuarioAtual.email);
        }

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnAtualizar.setOnClickListener(v -> {
            String novoEmail = edtNovoEmail.getText().toString().trim();

            if (novoEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(novoEmail).matches()) {
                Toast.makeText(getContext(), "Digite um e-mail válido!", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioAtual.email = novoEmail;
            atualizarBanco(dialog, "E-mail atualizado com sucesso!");
        });

        dialog.show();
        ajustarTamanhoDialog(dialog);
    }

    private void mostrarDialogSenha() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_mudar_senha);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtSenhaAtual = dialog.findViewById(R.id.edtSenhaAtual);
        EditText edtNovaSenha = dialog.findViewById(R.id.edtNovaSenha);
        EditText edtConfirmarSenha = dialog.findViewById(R.id.edtConfirmarSenha);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelarSenha);
        Button btnAtualizar = dialog.findViewById(R.id.btnAtualizarSenha);

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnAtualizar.setOnClickListener(v -> {
            String senhaAtual = edtSenhaAtual.getText().toString().trim();
            String novaSenha = edtNovaSenha.getText().toString().trim();
            String confirmarSenha = edtConfirmarSenha.getText().toString().trim();

            if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senhaAtual.equals(usuarioAtual.senha)) {
                Toast.makeText(getContext(), "A senha atual está incorreta!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!novaSenha.equals(confirmarSenha)) {
                Toast.makeText(getContext(), "A nova senha e a confirmação não batem!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (novaSenha.length() < 6) {
                Toast.makeText(getContext(), "A nova senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioAtual.senha = novaSenha;
            atualizarBanco(dialog, "Senha atualizada com sucesso!");
        });

        dialog.show();
        ajustarTamanhoDialog(dialog);
    }

    private void mostrarDialogTermos() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_termos);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btnEntendido = dialog.findViewById(R.id.btnEntendidoTermos);

        btnEntendido.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        ajustarTamanhoDialog(dialog);
    }

    private void mostrarDialogDeletarConta() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_deletar_conta);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btnCancelar = dialog.findViewById(R.id.btnCancelarDeletar);
        Button btnConfirmar = dialog.findViewById(R.id.btnConfirmarDeletar);

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase.getInstance(getContext()).userDao().delete(usuarioAtual);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Conta deletada com sucesso.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), MainPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
                }
            }).start();
        });

        dialog.show();
        ajustarTamanhoDialog(dialog);
    }

    private void atualizarBanco(Dialog dialog, String mensagemSucesso) {
        new Thread(() -> {
            AppDatabase.getInstance(getContext()).userDao().update(usuarioAtual);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), mensagemSucesso, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        }).start();
    }

    private void ajustarTamanhoDialog(Dialog dialog) {
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}