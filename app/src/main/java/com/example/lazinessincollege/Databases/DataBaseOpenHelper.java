package com.example.lazinessincollege.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
    public DataBaseOpenHelper(@Nullable Context context) {
        super(context, "DADOS", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TabelasSQL.getCreateTableDiciplina());
        db.execSQL(TabelasSQL.getCreateTableUsuario());
        Log.i("MEU", "TABELA CRIADA?");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
