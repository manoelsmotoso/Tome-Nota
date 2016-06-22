package br.com.manoelmotoso.tomenota.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.Serializable;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class NovaNota extends AppCompatActivity {
    private Button salvarNota;
    private Button btnCancelar;
    private EditText edTitulo;
    private EditText edDescricao;
    private  Nota nota;
    private NotaDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nova_nota);
        edTitulo = (EditText) findViewById(R.id.titulo);
        edDescricao = (EditText) findViewById(R.id.descricao);
        nota = new Nota();
        dao = new NotaDAO(this);

        //código para obter o bundle da activity anterior
        //se este não for nulo
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            nota = (Nota) bundle.get("nota");
            edTitulo.setText(nota.getTitulo());
            edDescricao.setText(nota.getDescricao());
        }

        //Ouvinte do botão Salvar da tela de nova anotação
        salvarNota = (Button) findViewById(R.id.btnNovaNota);
        salvarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = String.valueOf(edTitulo.getText());
                String descricao = String.valueOf(edDescricao.getText());
                if(titulo.isEmpty()){
                    Snackbar.make(v, "O tilulo e obrigatorio.", Snackbar.LENGTH_LONG).show();

                }else {

                    nota.setTitulo(titulo);
                    nota.setDescricao(descricao);
                    boolean isGravada = dao.gravarNota(NovaNota.this.nota);
                    if (isGravada == true) {
                        //Redireciona para a tela que lista as anotações
                        Intent intent = new Intent(NovaNota.this, RecicleViewActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar.make(v, "Erro inesperado ao tentar gravar.", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redireciona para a tela que lista as anotações
                Intent intent = new Intent(NovaNota.this, RecicleViewActivity.class);
                startActivity(intent);
            }
        });


    }

}
