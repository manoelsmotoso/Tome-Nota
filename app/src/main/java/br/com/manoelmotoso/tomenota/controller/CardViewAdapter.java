package br.com.manoelmotoso.tomenota.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder>{
    private static CheckBox checkBoxTitulo;
    private static TextView txDataDanota;
    private static CardView cardViewNota;
    private List<Nota> notas;
    private List<String> ids;
    private Activity act;


    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewAdapter(List<Nota> notas, Activity act) {
        this.notas = notas;
        this.act = act;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
            checkBoxTitulo = (CheckBox) v.findViewById(R.id.tituloDaNota);
            txDataDanota = (TextView) v.findViewById(R.id.dataDaNota);
            cardViewNota = (CardView) v.findViewById(R.id.cardview);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notas.size();
    }


    @Override
    public long getItemId(int position) {
        return notas.get(position).get_id();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Criando uma nova View
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nota_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        seta o titulo e data da anotação no cardview
        checkBoxTitulo.setText(notas.get(position).getTitulo());
        txDataDanota.setText(notas.get(position).getDataDeAlteracao());

        ids = new ArrayList<String>();
        checkBoxTitulo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    act.findViewById(R.id.deletaNota).setVisibility(View.VISIBLE);
                    ids.add(String.valueOf(getItemId(position)));
                }
                if(!isChecked){
                    String id =String.valueOf(getItemId(position));
                    if(ids.contains(id)){
                        ids.remove(id);
                    }
                    if(ids.isEmpty()) {
                        act.findViewById(R.id.deletaNota).setVisibility(View.GONE);
                    }
                }
            }
        });

        cardViewNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nota nota = notas.get(position);
                Intent intent = new Intent(act, NovaNota.class);
                intent.putExtra("nota", nota);
                act.startActivity(intent);
            }
        });

    }

    /**
     * Retorna uma lista de ids dos itens selecionados na lista
     * @return _IDs
     */
    public List<String> getIdsSelecionados(){
        return  this.ids;
    }
}
