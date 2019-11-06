package com.example.lazinessincollege.Activitys.Cadastro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.EthiopicCalendar;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lazinessincollege.Activitys.MainActivity;
import com.example.lazinessincollege.Databases.DataBaseOpenHelper;
import com.example.lazinessincollege.Dominio.Entidades.Diciplinas;
import com.example.lazinessincollege.Dominio.Repositorio.DiciplinasRepositorio;
import com.example.lazinessincollege.R;

import java.io.File;
import java.util.Objects;

public class CadastroDiciplinasActivity extends AppCompatActivity {

    private static String TAG = "MEU CADASTRO DICIPLINA";
    private SQLiteDatabase db;
    private DataBaseOpenHelper dataBaseOpenHelper;
    private DiciplinasRepositorio diciplinasRepositorio;
    private Diciplinas diciplinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_diciplinas);

        NumberPicker np = findViewById(R.id.np_periodo_letivo_cadastro_diciplina);
        np.setMinValue(1);
        np.setMaxValue(12);
        np.setWrapSelectorWheel(false);

        Toolbar tb = findViewById(R.id.tb_cadastro_diciplinas);
        setSupportActionBar(tb);
        criarConexao();

    }

    private void setSupportActionBar(Toolbar tb) {
        tb.setSubtitle("Cadastro de diciplinas");
        tb.setNavigationIcon(R.drawable.ic_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void criarDiciplina(View view) {

        EditText et_nome_dic = findViewById(R.id.et_nome_diciplina_cadastro_diciplina);
        EditText et_nome_pro = findViewById(R.id.et_nome_professor_cadastro_diciplina);
        NumberPicker np = findViewById(R.id.np_periodo_letivo_cadastro_diciplina);


        if (et_nome_dic.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "INSIRA O NOME DA DICIPLINA", Toast.LENGTH_LONG).show();
        else if (et_nome_pro.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "INSIRA O NOME DO PROFESSOR", Toast.LENGTH_LONG).show();
        else {
            final int[] periodoLetivo = {0};

            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Log.i(TAG, "valor selecionado :: " + newVal);
                    periodoLetivo[0] = newVal;
                }
            });

            String nomeDic = et_nome_dic.getText().toString();
            String nomeProf = et_nome_pro.getText().toString();
            String nomePasta = nomeDic + nomeProf + periodoLetivo[0];
            criarDiretorio(nomePasta);

            diciplinas = new Diciplinas();
            diciplinas.professor = nomeProf;
            diciplinas.periodo = 0;
            diciplinas.aulas = 0;
            diciplinas.diciplina = nomeDic;

            try {
                diciplinasRepositorio.inserir(diciplinas);
                Log.i(TAG, "BIIIIRRRRLLL, DEU BOM EM ");
            } catch (SQLException e) {
                Log.i(TAG, "NAO ESCREVEU NO BANCO DE DADOS");
            }

        }
    }

    private void criarDiretorio(String nomePasta) {
        File folder = new
                File(Environment.getExternalStorageDirectory()
                + File.separator
                + Environment.DIRECTORY_PICTURES
                + File.separator + nomePasta);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.i(TAG, "PASTA CRIADA");

            File tolist = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES);
            File[] files = tolist.listFiles();
            assert files != null;
            Log.d(TAG, "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    Log.i(TAG, "DIRETORIO: " + files[i].getName());
                }
            }
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            Log.i(TAG, "NOME JA CADASTRADO");
            Toast.makeText(getApplicationContext(), "DICIPLINA JÁ CADASTRADA ", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(getApplicationContext(), CadastroDiciplinasActivity.class);
            startActivity(i);
        }
    }

    private void criarConexao() {

        try {
            dataBaseOpenHelper = new DataBaseOpenHelper(this);

            db = dataBaseOpenHelper.getWritableDatabase();

            diciplinasRepositorio = new DiciplinasRepositorio(db);

            Log.i(TAG, "CONEXAO CRIADA COM SUCESSO ");
        } catch (SQLException e) {
            Log.i(TAG, "SQL TA ERRADO EM");
            e.printStackTrace();
        }

    }
}
