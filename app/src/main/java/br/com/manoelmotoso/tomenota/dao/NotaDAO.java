package br.com.manoelmotoso.tomenota.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.manoelmotoso.tomenota.model.Nota;

/**
 *Classe responsavel pela persistencia de dados.
 *
 * @author Manoel Silva Motoso
 */
public class NotaDAO {
    private static final String TABLE_NOTAS = "notas" ;
    private SqlOpenHelper helper;
    private Context context;
    private SQLiteDatabase db;


    public NotaDAO(Context context) {
    this.context =context;
        helper = new SqlOpenHelper(context);
    }

    /**
     * Retorna uma lista de todos as Notas gravadas.
     * @return ArrayList<Nota>
     */
    public List<Nota> getNotas(){
        List<Nota> notas = new ArrayList<Nota>();
        db = helper.getReadableDatabase();
        String[] colunas ={"_id", "titulo", "descricao", "dataDeAlteracao"};
        String orderBy = "dataDeAlteracao DESC";

        Cursor cursor = db.query(TABLE_NOTAS,colunas,null,null,null,null,orderBy);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
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
     * @param nota
     * @return true se nota gravada ou false se não
     */
    public boolean gravarNota(Nota nota) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Date date = new Date();
        //Foramtando a data
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

        ContentValues values = new ContentValues();
        values.put("titulo", nota.getTitulo());
        values.put("descricao", nota.getDescricao());
        values.put("dataDeAlteracao", String.valueOf(simpleDateFormat.format(date)));

        long respOperacao = -1;
        if (nota.get_id() != 0) {
            String[] ids = {String.valueOf(nota.get_id())};
            respOperacao = db.update("notas", values, "_id = ?", ids);
        } else {
            respOperacao = db.insert(TABLE_NOTAS, null, values);
        }

        if (respOperacao != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deleta Notas dados seus respectivos _IDs;
     * @param listIds
     */
    public void deletarNotas(List<String> listIds) {
        SQLiteDatabase db = helper.getWritableDatabase();

        for(String id:listIds){
            db.delete(TABLE_NOTAS,"_id = ?",new String[]{id});
        }
    }

    /**
     * Deleta uma nota dado um id
     * @param nota
     */

    public  void deletarNota(Nota nota){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NOTAS,"_id = ?",new String[]{(String.valueOf(nota.get_id()))});
        Toast.makeText(context, nota.getTitulo()+" DELETADA", Toast.LENGTH_SHORT).show();
    }
    //Fecha conexao com o banco de dados
    public void close(){
        helper.close();
    }

}
