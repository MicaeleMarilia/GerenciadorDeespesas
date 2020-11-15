package com.example.rendemais;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivityHomeUsuario extends AppCompatActivity {

    Button btnRenda, btnCadastrarDespesas;
    EditText edtRenda;
    ListView listViewDespesa;
    ListView listViewSaldoFinal;
    ArrayList<DespesaUsuario> despesaUsuarios = new ArrayList<>();
    ArrayList<Renda> rendaUsuarios = new ArrayList<>();
    Usuario usuAlterar;

    private void cadastrarRenda() {


        if (edtRenda.getText().toString().isEmpty()) {
            Toast.makeText(MainActivityHomeUsuario.this, "Valor da Renda Obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference("renda_usuario").orderByChild("usuario").equalTo(usuAlterar.email).limitToFirst(1).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Renda rnd = snapshot.getValue(Renda.class);
                        rnd.ID = snapshot.getKey();
                        rnd.renda = Double.parseDouble(edtRenda.getText().toString());
                        rnd.saldo_final = Double.parseDouble(edtRenda.getText().toString());

                        FirebaseApp.initializeApp(MainActivityHomeUsuario.this);
                        FirebaseDatabase bd = FirebaseDatabase.getInstance();
                        DatabaseReference bdRef = bd.getReference();
                        bdRef.child("renda_usuario").child(rnd.ID).setValue(rnd);

                        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivityHomeUsuario.this);
                        builder.setMessage("Renda cadstrada com sucesso.")
                                .setTitle("Aviso");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                } else {
                    Renda renda = new Renda();
                    renda.usuario = usuAlterar.email;
                    renda.renda = Double.parseDouble(edtRenda.getText().toString());
                    renda.saldo_final = Double.parseDouble(edtRenda.getText().toString());
                    FirebaseApp.initializeApp(MainActivityHomeUsuario.this);
                    FirebaseDatabase bd = FirebaseDatabase.getInstance();
                    DatabaseReference bdRef = bd.getReference();
                    bdRef.child("renda_usuario").push().setValue(renda);

                    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivityHomeUsuario.this);
                    builder.setMessage("Renda cadstrado com sucesso")
                            .setTitle("Aviso");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

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
        setContentView(R.layout.activity_home_usuario);
        Intent intent = getIntent();

        btnRenda = (Button) findViewById(R.id.buttonRenda);
        btnCadastrarDespesas = (Button) findViewById(R.id.buttonNovaDespesa);
        edtRenda = (EditText) findViewById(R.id.editTextRenda);
        listViewDespesa = (ListView) findViewById(R.id.listViewDespesas);
        listViewSaldoFinal = (ListView) findViewById(R.id.listViewSaldoFinal);

        if(getIntent().getExtras()!= null) {
            this.usuAlterar = getIntent().getExtras().getParcelable("usuLogado");
        }


        //Despesas
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference("despesa_usuario").orderByChild("usuario").equalTo(usuAlterar.email).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    despesaUsuarios = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DespesaUsuario desUsu = snapshot.getValue(DespesaUsuario.class);
                        desUsu.ID = snapshot.getKey();
                        despesaUsuarios.add(desUsu);
                    }

                    DespesaUsuarioAdapter adapter = new DespesaUsuarioAdapter(MainActivityHomeUsuario.this, despesaUsuarios);
                    listViewDespesa.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        listViewDespesa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DespesaUsuario desUsu = despesaUsuarios.get(position);
                Intent intent = new Intent(MainActivityHomeUsuario.this, MainActivityCadastrarDespesas.class);
                intent.putExtra("despesa_usuario", desUsu);
                intent.putExtra("usuLogado",usuAlterar);
                startActivity(intent);
            }
        });

        //Saldo Final
        FirebaseDatabase.getInstance().getReference("renda_usuario").orderByChild("usuario").equalTo(usuAlterar.email).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    rendaUsuarios = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Renda rnd = snapshot.getValue(Renda.class);
                        rnd.ID = snapshot.getKey();
                        rendaUsuarios.add(rnd);
                    }

                    SaldoFinalAdapter adapter = new SaldoFinalAdapter(MainActivityHomeUsuario.this, rendaUsuarios);
                    listViewSaldoFinal.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        listViewSaldoFinal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Renda rnd = rendaUsuarios.get(position);
                Intent intent = new Intent(MainActivityHomeUsuario.this, MainActivityCadastrarDespesas.class);
                intent.putExtra("renda final", rnd);
                intent.putExtra("usuLogado",usuAlterar);
                startActivity(intent);
            }
        });

        btnRenda.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                cadastrarRenda();

            }
        });

        btnCadastrarDespesas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeUsuario.this, MainActivityCadastrarDespesas.class);
                intent.putExtra("usuLogado",usuAlterar);
                startActivity(intent);
            }
        });

    }
}
