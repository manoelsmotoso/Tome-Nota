package br.com.manoelmotoso.tomenota.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.controller.NovaNotaActivity;
import br.com.manoelmotoso.tomenota.model.Nota;
import android.view.*;
import android.view.ContextMenu.*;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private static CheckBox checkBoxTitulo;
    private static TextView txDataDanota;
    private static CardView cardViewNota;
    private  final List<Nota> notas;
    private final Activity act;
    private List<String> ids;

	private int position;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewAdapter(List<Nota> notas, Activity act){
        this.notas = notas;
        this.act = act;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount(){
        return notas.size();
    }
	public Nota getItem(int position){
		return notas.get(position);
	}

    @Override
    public final long getItemId(int position){
        return notas.get(position).get_id();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,                   int viewType)
	{
        // Criando uma nova View
        View v = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.caedview_nota_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }
	public final int getPosition()
	{
		return this.position;
	}
	public final void setPosition(int position){
		this.position = position;
	}

	
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					setPosition(holder.getPosition());
					return false;
				}
			});



		//        seta o titulo e data da anotação no cardview
        checkBoxTitulo.setText(notas.get(position).getTitulo());
        txDataDanota.setText(notas.get(position).getDataDeAlteracao());
        ids = new ArrayList<>();
        checkBoxTitulo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						act.findViewById(R.id.deletaNota).setVisibility(View.VISIBLE);
						ids.add(String.valueOf(getItemId(position)));
					}
					if (!isChecked)
					{
						String id = String.valueOf(getItemId(position));
						if (ids.contains(id))
						{
							ids.remove(id);
						}
						if (ids.isEmpty())
						{
							act.findViewById(R.id.deletaNota).setVisibility(View.GONE);
						}
					}
				}
			});

        cardViewNota.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Nota nota = notas.get(position);
					Intent intent = new Intent(act, NovaNotaActivity.class);
					intent.putExtra("nota", nota);
					act.startActivity(intent);
				}
			});

    }

    /**
     * Retorna uma lista de ids dos itens selecionados na lista
     *
     * @return _IDs
     */
    public List<String> getIdsSelecionados()
	{
        return this.ids;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
	{ 


		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
										ContextMenuInfo menuInfo)
		{
			String titulo = "Menu de Contexto "+getPosition();
			menu.setHeaderTitle(titulo);

		}




        public ViewHolder(View v)
		{
            super(v);
            checkBoxTitulo = (CheckBox) v.findViewById(R.id.tituloDaNota);
            txDataDanota = (TextView) v.findViewById(R.id.dataDaNota);
            cardViewNota = (CardView) v.findViewById(R.id.cardview);
			v.setOnCreateContextMenuListener(this);
        }

    }
}
