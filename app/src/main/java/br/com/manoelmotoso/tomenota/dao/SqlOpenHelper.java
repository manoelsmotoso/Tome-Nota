package br.com.manoelmotoso.tomenota.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by motos on 17/06/2016.
 */
public class SqlOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String NAME = "tomenota" ;
    private  static final int VERSION = 2;
    private static final String TABLE_NOTAS ="notas";

    public SqlOpenHelper(Context context) {
        super(context, NAME,null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE "+ TABLE_NOTAS +" (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT," +
                "descricao TEXT," +
                "dataDeAlteracao TEXT" +
                ");";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDrop = "DROP TABLE "+TABLE_NOTAS+";";
        db.execSQL(sqlDrop);
        onCreate(db);
        
    }
}
