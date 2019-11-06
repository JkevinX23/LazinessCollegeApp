package com.example.lazinessincollege.Dominio.Repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lazinessincollege.Constantes;
import com.example.lazinessincollege.Dominio.Entidades.Diciplinas;

import java.util.ArrayList;
import java.util.List;

public class DiciplinasRepositorio {

    private SQLiteDatabase conexao;

    public DiciplinasRepositorio(SQLiteDatabase conexao) {
        this.conexao = conexao;
    }

    public void inserir(Diciplinas diciplinas) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", diciplinas.diciplina);
        contentValues.put("PROFESSOR", diciplinas.professor);
        contentValues.put("AULAS", diciplinas.aulas);
        contentValues.put("PERIODO", diciplinas.periodo);

        conexao.insertOrThrow("DICIPLINAS", null, contentValues);


    }

    public void excluir(int id) {
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(id);
        conexao.delete("DICIPLINAS", "ID = ?", parametros);


    }

    public void alterar(Diciplinas diciplinas) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", diciplinas.diciplina);
        contentValues.put("PROFESSOR", diciplinas.professor);
        contentValues.put("AULAS", diciplinas.aulas);
        contentValues.put("PERIODO", diciplinas.periodo);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(diciplinas.id);

        conexao.update("DICIPLINAS", contentValues, "ID = ?", parametros);

    }

    public List<Diciplinas> buscarTodos() {

        List<Diciplinas> diciplinas = new ArrayList<Diciplinas>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID, NOME, PROFESSOR, AULAS, PERIODO");
        sql.append(" FROM DICIPLINAS ");

        Cursor result = conexao.rawQuery(sql.toString(), null);
        if (result.getCount() > 0) {

            result.moveToFirst();

            do {
                Diciplinas dic = new Diciplinas();
                dic.id = result.getInt(result.getColumnIndexOrThrow(Constantes.ID_tabela_diciplinas));
                dic.aulas = result.getInt(result.getColumnIndexOrThrow(Constantes.aulas_tabela_diciplinas));
                dic.periodo = result.getInt(result.getColumnIndexOrThrow(Constantes.periodo_tabela_diciplinas));
                dic.diciplina = result.getString(result.getColumnIndexOrThrow(Constantes.diciplina_tabela_diciplinas));
                dic.professor = result.getString(result.getColumnIndexOrThrow(Constantes.professor_tabela_diciplinas));

                diciplinas.add(dic);

            } while (result.moveToNext());

        }


        return diciplinas;
    }

    public Diciplinas buscarDiciplina(int id) {
        Diciplinas diciplinas = new Diciplinas();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID, NOME, PROFESSOR, AULAS, PERIODO");
        sql.append(" FROM DICIPLINAS ");
        sql.append("WHERE ID = ? ");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(id);

        Cursor result = conexao.rawQuery(sql.toString(), parametros);

        if (result.getCount() > 0) {

            result.moveToFirst();
            diciplinas.id = result.getInt(result.getColumnIndexOrThrow(Constantes.ID_tabela_diciplinas));
            diciplinas.aulas = result.getInt(result.getColumnIndexOrThrow(Constantes.aulas_tabela_diciplinas));
            diciplinas.periodo = result.getInt(result.getColumnIndexOrThrow(Constantes.periodo_tabela_diciplinas));
            diciplinas.diciplina = result.getString(result.getColumnIndexOrThrow(Constantes.diciplina_tabela_diciplinas));
            diciplinas.professor = result.getString(result.getColumnIndexOrThrow(Constantes.professor_tabela_diciplinas));

            return diciplinas;
        }

        return null;
    }

}
