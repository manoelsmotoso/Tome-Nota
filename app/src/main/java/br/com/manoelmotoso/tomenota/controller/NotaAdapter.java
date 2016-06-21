package br.com.manoelmotoso.tomenota.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

/**
 * Created by motos on 16/06/2016.
 */
public class NotaAdapter extends BaseAdapter {
    private List<Nota> notas;
    private Activity activity;
    private  List<String> ids;

    public NotaAdapter(List<Nota> notas, Activity activity) {
        this.notas = notas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int position) {
        return notas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notas.get(position).get_id();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = activity.getLayoutInflater()
                .inflate(R.layout.nota_adapter,parent,false);

        final Nota nota = notas.get(position);

        CheckBox checkboxTituloDaNota = (CheckBox) view.findViewById(R.id.tituloDaNota);
        TextView txDataDanota = (TextView) view.findViewById(R.id.dataDaNota);

        txDataDanota.setText(nota.getDataDeAlteracao());
        checkboxTituloDaNota.setText(nota.getTitulo());
        ids = new ArrayList<String>();
        checkboxTituloDaNota.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ids.add(String.valueOf(getItemId(position)));
                }
                if(!isChecked){
                    String id =String.valueOf(getItemId(position));
                    if(ids.contains(id)){
                        ids.remove(id);
                    }
                }
            }
        });

        //Redireciona para a tela de exibição
        final CardView cardView = (CardView) view.findViewById(R.id.cardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Nota  nota = (Nota) getItem(position);
                Intent intent = new Intent(activity,NovaNota.class);
                intent.putExtra("nota", nota);
				
                activity.startActivity(intent);
            }
        });

        return view;
    }
    public List<String> getIdsParaDeletar(){
        return  this.ids;
    }
}
