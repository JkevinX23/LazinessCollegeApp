package com.example.lazinessincollege.Databases;

import android.util.Log;

public class TabelasSQL {

    public static String getCreateTableDiciplina() {
        StringBuilder sqlDic = new StringBuilder();


        sqlDic.append("    CREATE TABLE IF NOT EXISTS DICIPLINAS (");
        sqlDic.append("        ID               INTEGER      NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlDic.append("        NOME             VARCHAR (20) NOT NULL,");
        sqlDic.append("        PROFESSOR        VARCHAR (30) DEFAULT (''),");
        sqlDic.append("        AULAS            INTEGER (2) DEFAULT (0),");
        sqlDic.append("        PERIODO          INTEGER (2) DEFAULT (0) ) ");

        return sqlDic.toString();
    }

    public static String getCreateTableUsuario() {
        StringBuilder sqlUsr = new StringBuilder();

        sqlUsr.append("  CREATE TABLE IF NOT EXISTS USUARIO ( ");
        sqlUsr.append("      EMAIL  VARCHAR (30) NOT NULL UNIQUE  PRIMARY KEY, ");
        sqlUsr.append("      NOME   VARCHAR (50) NOT NULL,");
        sqlUsr.append("      IDADE  INTEGER (3)  NOT NULL,");
        sqlUsr.append("      CURSO  VARCHAR (50) NOT NULL,");
        sqlUsr.append("      SEXO   INTEGER (1)  NOT NULL ) ");
        Log.i("MEU","Table user create");
        return sqlUsr.toString();
    }

}
