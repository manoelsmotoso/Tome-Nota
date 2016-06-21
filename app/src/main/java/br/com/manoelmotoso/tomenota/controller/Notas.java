package br.com.manoelmotoso.tomenota.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class Notas extends AppCompatActivity {
    private ListView listaDeNotas;
    private ImageButton imgBtnNovaNota;
    private ImageButton btnDeletar;
    private NotaDAO dao;
    private List<Nota> notas;
    private NotaAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        carregarLista();



        imgBtnNovaNota = (ImageButton) findViewById(R.id.adicionaNota);
        imgBtnNovaNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notas.this, NovaNota.class);
                startActivity(intent);

            }
        });


        btnDeletar = (ImageButton) findViewById(R.id.botaoDeletar);
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> idsParaDeletar = adapter.getIdsParaDeletar();
                if(!idsParaDeletar.isEmpty()) {
                    dao = new NotaDAO(Notas.this);
                    dao.deletarNotas(idsParaDeletar);
                    dao.close();
                    carregarLista();
                }else{
                    Snackbar.make(v, "Nenhum item selecionado", Snackbar.LENGTH_LONG).show();
                }

            }
        });


    }

    public  void irParaRecicleView(View v){
        Intent intent = new Intent(Notas.this,TesteRecicleView.class);
        startActivity(intent);
    }

    private void carregarLista() {
        dao = new NotaDAO(this);
        notas = dao.getNotas();
        dao.close();
        adapter = new NotaAdapter(notas,this);
        listaDeNotas = (ListView) findViewById(R.id.listaDeNotas);

        listaDeNotas.setAdapter(adapter);
    }


}
