package br.com.manoelmotoso.tomenota.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.model.Nota;

import static android.support.v7.widget.RecyclerView.*;

public class CardViewNotasAdapter extends RecyclerView.Adapter<CardViewNotasAdapter.ViewHolder> {
    private List<Nota> notas;

    public static class ViewHolder extends RecyclerView.ViewHolder {
      
		public CheckBox checkBoxTitulo;

        public ViewHolder(View v) {
            super(v);
            checkBoxTitulo = (CheckBox) v.findViewById(R.id.tituloDaNota);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewNotasAdapter(List<Nota> notas) {
        this.notas = notas;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewNotasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nota_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.checkBoxTitulo.setText(notas.get(position).getTitulo());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notas.size();
    }
}
