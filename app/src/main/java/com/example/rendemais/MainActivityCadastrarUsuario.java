package com.example.rendemais;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivityCadastrarUsuario extends AppCompatActivity {

    Button btnCadastrar;
    EditText edtNome, edtEmail, edtSenha, edtConfirmarSenha;

    private void cadastrarUsuario() {

        //Validação do preenchimento dos campos
        if (edtNome.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarUsuario.this, "Nome Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }
        if (edtEmail.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarUsuario.this, "Email Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }
        if (edtSenha.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarUsuario.this, "Senha Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(edtSenha.getText().toString().equals(edtConfirmarSenha.getText().toString()))) {
            Toast.makeText(MainActivityCadastrarUsuario.this, "Senhas não coincidem, tente novamente.", Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("email").equalTo(edtEmail.getText().toString()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(MainActivityCadastrarUsuario.this, "Email já cadastrado.", Toast.LENGTH_LONG).show();
                } else {
                    Usuario usu = new Usuario();
                    usu.nome = edtNome.getText().toString();
                    usu.email = edtEmail.getText().toString();
                    usu.senha = edtSenha.getText().toString();

                    FirebaseApp.initializeApp(MainActivityCadastrarUsuario.this);
                    FirebaseDatabase bd2 = FirebaseDatabase.getInstance();
                    DatabaseReference bdRef2 = bd2.getReference();
                    bdRef2.child("usuarios").push().setValue(usu);

                    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivityCadastrarUsuario.this);
                    builder.setMessage("Usuario cadstrado com sucesso.")
                            .setTitle("Aviso");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        Intent intent = getIntent();

        btnCadastrar = (Button) findViewById(R.id.cad_cadastrar);
        edtNome = (EditText) findViewById(R.id.editTextNome);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSenha = (EditText) findViewById(R.id.editTextSenha);
        edtConfirmarSenha = (EditText) findViewById(R.id.editTextConfirmeSenha);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //cadastrar um novo usuário
                cadastrarUsuario();
            }
        });
    }


}
