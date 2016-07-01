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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private CheckBox mTitulo;
    private TextView mData;
    private CardView mCardView;
    private List<Nota> mNotas;
    private Activity mAct;
    private List<String> ids;

    public RecyclerViewAdapter(List<Nota> notas, Activity act) {
        this.mNotas = notas;
        this.mAct = act;
    }

    // Retorna a quantidade de items na lista
    @Override
    public int getItemCount() {
        return mNotas.size();
    }
    //Retorna o id do item selecionado na lista
    @Override
    public long getItemId(int position) {
        return mNotas.get(position).get_id();
    }

    public void restaurarItem(Nota nota, int position){
        this.mNotas.add(position,nota);
    }

    // Criando nova view (invocada pelo layout manager
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Criando uma nova View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.caedview_nota_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //Seta o titulo e data da anotação no cardview
        mTitulo.setText(mNotas.get(position).getTitulo());
        mData.setText(mNotas.get(position).getDataDeAlteracao());
        //Adiciona Ids dos itens selecionado a uma lista;
        ids = new ArrayList<>();
        mTitulo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAct.findViewById(R.id.deletaNota).setVisibility(View.VISIBLE);
                    ids.add(String.valueOf(getItemId(position)));
                }
                if (!isChecked) {
                    String id = String.valueOf(getItemId(position));
                    if (ids.contains(id)) {
                        ids.remove(id);
                    }
                    if (ids.isEmpty()) {
                        mAct.findViewById(R.id.deletaNota).setVisibility(View.GONE);
                    }
                }
            }
        });

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nota nota = mNotas.get(position);
                Intent intent = new Intent(mAct, NovaNotaActivity.class);
                intent.putExtra("nota", nota);
                mAct.startActivity(intent);
            }
        });

    }

    /**
     * Retorna uma lista de ids dos itens selecionados na lista
     *
     * @return _IDs
     */
    public List<String> getIdsSelecionados() {
        return this.ids;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            mTitulo = (CheckBox) v.findViewById(R.id.tituloDaNota);
            mData = (TextView) v.findViewById(R.id.dataDaNota);
            mCardView = (CardView) v.findViewById(R.id.cardview);
        }

    }
}
