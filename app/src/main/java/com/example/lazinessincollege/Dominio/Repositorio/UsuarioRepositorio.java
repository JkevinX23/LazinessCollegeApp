package com.example.lazinessincollege.Dominio.Repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lazinessincollege.Constantes;
import com.example.lazinessincollege.Dominio.Entidades.Usuario;

public class UsuarioRepositorio {

    private SQLiteDatabase conexao;

    public UsuarioRepositorio(SQLiteDatabase conexao) {
        this.conexao = conexao;
    }

    public void inserir(Usuario usr){

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constantes.email_tabela_usuario,usr.email);
        contentValues.put(Constantes.nome_usuario_tabela_usuario,usr.nome);
        contentValues.put(Constantes.idade_tabela_usuario,usr.idade);
        contentValues.put(Constantes.curso_tabela_usuario,usr.curso);
        contentValues.put(Constantes.sexo_tabela_usuario,usr.sexo);

        conexao.insertOrThrow(Constantes.nome_tabela_usuario,null,contentValues);
    }

    public void alterar(Usuario usr){

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constantes.email_tabela_usuario,usr.email);
        contentValues.put(Constantes.nome_usuario_tabela_usuario,usr.nome);
        contentValues.put(Constantes.idade_tabela_usuario,usr.idade);
        contentValues.put(Constantes.curso_tabela_usuario,usr.curso);
        contentValues.put(Constantes.sexo_tabela_usuario,usr.sexo);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(usr.email);

        StringBuilder where  = new StringBuilder();
        where.append(Constantes.email_tabela_usuario).append(" = ? ");

        conexao.update(Constantes.nome_tabela_usuario, contentValues,where.toString(),parametros);
    }

    public Usuario buscarUsr(String email) {
        Usuario usr = new Usuario();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append(Constantes.nome_usuario_tabela_usuario)
                .append(", ").append(Constantes.email_tabela_usuario)
                .append(", ").append(Constantes.curso_tabela_usuario)
                .append(", ").append(Constantes.idade_tabela_usuario)
                .append(", ").append(Constantes.sexo_tabela_usuario)
                .append(" FROM ").append(Constantes.nome_tabela_usuario)
                .append("WHERE ").append(Constantes.email_tabela_usuario).append(" = ? ");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(email);

        Cursor result = conexao.rawQuery(sql.toString(), parametros);

        if (result.getCount() > 0) {

            result.moveToFirst();
            usr.email = result.getString(result.getColumnIndexOrThrow(Constantes.email_tabela_usuario));
            usr.curso = result.getString(result.getColumnIndexOrThrow(Constantes.curso_tabela_usuario));
            usr.nome = result.getString(result.getColumnIndexOrThrow(Constantes.nome_usuario_tabela_usuario));
            usr.sexo = result.getInt(result.getColumnIndexOrThrow(Constantes.sexo_tabela_usuario));
            usr.idade = result.getInt(result.getColumnIndexOrThrow(Constantes.idade_tabela_usuario));

            return usr;
        }

        return null;
    }

}
