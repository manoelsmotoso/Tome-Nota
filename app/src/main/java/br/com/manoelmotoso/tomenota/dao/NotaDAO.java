package br.com.manoelmotoso.tomenota.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.manoelmotoso.tomenota.model.Nota;

/**
 * Classe responsavel pela persistencia de dados.
 *
 * @author Manoel Silva Motoso
 */
public class NotaDAO {
    private static final String TABLE_NOTAS = "notas";
    private final SqlOpenHelper helper;
    private final Context context;


    public NotaDAO(Context context) {
        this.context = context;
        helper = new SqlOpenHelper(context);
    }

    /**
     * Retorna uma lista de todos as Notas gravadas.
     *
     * @return ArrayList<Nota>
     */
    public ArrayList<Nota> getNotas() {
        ArrayList<Nota> notas = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] colunas = {"_id", "titulo", "descricao", "dataDeAlteracao"};
        String orderBy = "dataDeAlteracao DESC";

        Cursor cursor = db.query(TABLE_NOTAS, colunas, null, null, null, null, orderBy);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Nota nota = new Nota();
            nota.set_id(cursor.getInt(0));
            nota.setTitulo(cursor.getString(1));
            nota.setDescricao(cursor.getString(2));
            nota.setDataDeAlteracao(cursor.getString(3));

            notas.add(nota);
        }
        cursor.close();
        return notas;
    }

    /**
     * Grava ou atualiza uma  Nota
     *
     * @param nota
     * @return true se nota gravada ou false se não
     */
    public boolean gravarNota(Nota nota) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Date date = new Date();
        //Foramtando a data
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        String data = String.valueOf(simpleDateFormat.format(date));
        ContentValues values = new ContentValues();
        values.put("titulo", nota.getTitulo());
        values.put("descricao", nota.getDescricao());

        if(nota.getDataDeAlteracao()==null | nota.getDataDeAlteracao()=="") {values.put("dataDeAlteracao", data);
        }else{values.put("dataDeAlteracao", nota.getDataDeAlteracao());}

        long respOperacao;
        if (nota.get_id() != 0) {
            String[] ids = {String.valueOf(nota.get_id())};
            values.put("dataDeAlteracao", data);
            respOperacao = db.update("notas", values, "_id = ?", ids);
        } else {
            respOperacao = db.insert(TABLE_NOTAS, null, values);

        }

        return respOperacao != -1;
    }

    /**
     * Deleta Notas dados seus respectivos _IDs;
     *
     * @param listIds
     */
    public void deletarNotas(List<String> listIds) {
        SQLiteDatabase db = helper.getWritableDatabase();

        for (String id : listIds) {
            db.delete(TABLE_NOTAS, "_id = ?", new String[]{id});
        }
    }

    /**
     * Deleta uma nota dado um id
     *
     * @param nota
     */

    public void deletarNota(Nota nota) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NOTAS, "_id = ?", new String[]{(String.valueOf(nota.get_id()))});
    }

    //Fecha conexao com o banco de dados
    public void close() {
        helper.close();
    }

}
