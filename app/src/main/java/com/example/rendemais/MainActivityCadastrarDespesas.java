package com.example.rendemais;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivityCadastrarDespesas extends AppCompatActivity {

    Button btnReceita, btnDespesa;
    EditText edtNome, edtValor;
    Renda rendaAlterar;

    Usuario usuAlterar;
    Renda rendaAlt;
    ArrayList<Renda> renda_final = new ArrayList<>();

    private void obterRendaUsuario() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference("renda_usuario").orderByChild("usuario").equalTo(usuAlterar.email).limitToFirst(1).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    renda_final = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Renda rnd = snapshot.getValue(Renda.class);
                        String e = rnd.usuario;
                        rnd.ID = snapshot.getKey();
                        rendaAlterar = rnd;
                        return;
                    }

                } else {
                    Toast.makeText(MainActivityCadastrarDespesas.this, "Renda não cadastrada.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void despesa(){

        obterRendaUsuario();

        FirebaseApp.initializeApp(MainActivityCadastrarDespesas.this);

        //Validação do preenchimento dos campos
        if (edtNome.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarDespesas.this, "Nome Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }
        if (edtValor.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarDespesas.this, "Valor Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }


        FirebaseApp.initializeApp(MainActivityCadastrarDespesas.this);
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference bdRef = bd.getReference();

        DespesaUsuario despesa = new DespesaUsuario();
        despesa.usuario = usuAlterar.email;
        despesa.nome = edtNome.getText().toString();
        despesa.tipo = "Despesa";
        despesa.valor = Double.parseDouble(edtValor.getText().toString());
        bdRef.child("despesa_usuario").push().setValue(despesa);

        rendaAlterar.saldo_final -= Double.parseDouble(edtValor.getText().toString());

        bdRef.child("renda_usuario").child(rendaAlterar.ID).setValue(rendaAlterar);

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivityCadastrarDespesas.this);
        builder.setMessage("Despesa cadastrada com sucesso.")
                .setTitle("Aviso");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivityCadastrarDespesas.this, MainActivityHomeUsuario.class);
                intent.putExtra("usuLogado",usuAlterar);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void receita() {

        obterRendaUsuario();

        //Validação do preenchimento dos campos
        if (edtNome.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarDespesas.this, "Nome Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }
        if (edtValor.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityCadastrarDespesas.this, "Valor Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseApp.initializeApp(MainActivityCadastrarDespesas.this);
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference bdRef = bd.getReference();

        DespesaUsuario despesa = new DespesaUsuario();
        despesa.usuario = usuAlterar.email;
        despesa.nome = edtNome.getText().toString();
        despesa.tipo = "Receita";
        despesa.valor = Double.parseDouble(edtValor.getText().toString());
        bdRef.child("despesa_usuario").push().setValue(despesa);

        rendaAlterar.saldo_final += Double.parseDouble(edtValor.getText().toString());

        bdRef.child("renda_usuario").child(rendaAlterar.ID).setValue(rendaAlterar);

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivityCadastrarDespesas.this);
        builder.setMessage("Receita cadastrada com sucesso.")
                .setTitle("Aviso");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivityCadastrarDespesas.this, MainActivityHomeUsuario.class);
                intent.putExtra("usuLogado",usuAlterar);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_despesas);
        Intent intent = getIntent();

        if (getIntent().getExtras() != null) {
            this.usuAlterar = getIntent().getExtras().getParcelable("usuLogado");
        }

        obterRendaUsuario();

        btnReceita = (Button) findViewById(R.id.bt_receita);
        btnDespesa = (Button) findViewById(R.id.bt_despesa);

        edtNome = (EditText) findViewById(R.id.editTextNome);
        edtValor = (EditText) findViewById(R.id.editTextValor);


        btnReceita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                receita();
            }
        });

        btnDespesa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                despesa();
            }
        });
    }
}
