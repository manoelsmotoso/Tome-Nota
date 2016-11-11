package br.com.manoelmotoso.tomenota.model;


import java.io.Serializable;

/**
 * Created by Manoel Silva Motoso on 16/06/2016.
 */
public class Nota implements Serializable {
    private int _id;
    private String titulo;
    private String descricao;
    private String dataDeAlteracao;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataDeAlteracao() {
        return dataDeAlteracao;
    }

    public void setDataDeAlteracao(String dataDeAlteracao) {
        this.dataDeAlteracao = dataDeAlteracao;
    }

}
