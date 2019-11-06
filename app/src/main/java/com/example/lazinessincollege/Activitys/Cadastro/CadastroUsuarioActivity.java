package com.example.lazinessincollege.Activitys.Cadastro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lazinessincollege.Activitys.LoginActivity;
import com.example.lazinessincollege.Databases.DataBaseOpenHelper;
import com.example.lazinessincollege.Dominio.Entidades.Usuario;
import com.example.lazinessincollege.Dominio.Repositorio.DiciplinasRepositorio;
import com.example.lazinessincollege.Dominio.Repositorio.UsuarioRepositorio;
import com.example.lazinessincollege.R;


public class CadastroUsuarioActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DataBaseOpenHelper dataBaseOpenHelper;
    private UsuarioRepositorio usuarioRepositorio;
    private Usuario usuario;

    public static String TAG = "MEU CadastroUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        criarConexao();

        Spinner spinner = findViewById(R.id.spin_sexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sexo));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    private void criarConexao() {

        try {
            dataBaseOpenHelper = new DataBaseOpenHelper(this);

            db = dataBaseOpenHelper.getWritableDatabase();

            usuarioRepositorio = new UsuarioRepositorio(db);

            Log.i(TAG, "CONEXAO CRIADA COM SUCESSO ");
        } catch (SQLException e) {
            Log.i(TAG, "SQL TA ERRADO EM");
            e.printStackTrace();
        }

    }

    public void continuarCadastro(View view) {
        Spinner spinner = findViewById(R.id.spin_sexo);
        EditText et_email = findViewById(R.id.et_email_cadastro_usuario);
        EditText et_nome = findViewById(R.id.et_nome_usuario_cadastro_usuario);
        EditText et_idade = findViewById(R.id.et_idade_cadastro_usuario);
        EditText et_curso = findViewById(R.id.et_curso_cadastro_usuario);

        final int[] sexo = {0};

        if (et_email.getText().toString().equals(""))
            et_email.requestFocus();
        else if (et_nome.getText().toString().equals(""))
            et_nome.requestFocus();
        else if (et_idade.getText().toString().equals(""))
            et_idade.requestFocus();
        else if (et_curso.getText().toString().equals(""))
            et_curso.requestFocus();
        else {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Selecionou: " + position);
                    sexo[0] = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String nome = et_nome.getText().toString();
            String email = et_email.getText().toString();
            String idade = et_idade.getText().toString();
            String curso = et_curso.getText().toString();

            usuario = new Usuario();
            usuario.nome = nome;
            usuario.email = email;
            usuario.curso = curso;
            usuario.sexo = sexo[0];
            usuario.idade = Integer.parseInt(idade);

            try{
                usuarioRepositorio.inserir(usuario);
                Log.i(TAG,"USUARIO CADASTRADO COM SUCESSO");
            }catch (SQLException ex){
                Log.i(TAG,"DEU RUIM AO CADASTRAR O USER VEI");
            }


            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Log.i(TAG, "USUARIO CADASTRADO, VOLTANDO PRO LOGIN");
        }
    }
}
