package com.example.rendemais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Usuario> usuarios = new ArrayList<>();
    Button btnCadastrar, btnLogin;
    final String colecao = "usuarios";
    EditText edtEmail, edtSenha;

    private void Logar() {

        if (edtEmail.getText().toString().isEmpty() || edtSenha.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Email e Senha Obrigatórios.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("email").equalTo(edtEmail.getText().toString()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebaseApp.initializeApp(MainActivity.this);
                FirebaseDatabase bd = FirebaseDatabase.getInstance();
                DatabaseReference bdRef = bd.getReference();

                if (dataSnapshot.exists()) {
                    usuarios = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Usuario usu = snapshot.getValue(Usuario.class);
                        usu.ID = snapshot.getKey();

                        if (usu.senha.equals(edtSenha.getText().toString())) {

                            Intent intent = new Intent(MainActivity.this, MainActivityHomeUsuario.class);
                            intent.putExtra("usuLogado",usu);
                            startActivity(intent);
                            return;

                        } else {
                            Toast.makeText(getApplicationContext(), "Usuário não encontrado.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Usuário não encontrado.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCadastrar = (Button) findViewById(R.id.cadastrar);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSenha = (EditText) findViewById(R.id.editTextSenha);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivityCadastrarUsuario.class);
                startActivity(intent);
            }
        });

        btnLogin = (Button) findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Logar();
            }
        });
    }
}
