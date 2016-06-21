package br.com.manoelmotoso.tomenota.controller;


import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import br.com.manoelmotoso.tomenota.*;
import br.com.manoelmotoso.tomenota.dao.*;
import br.com.manoelmotoso.tomenota.model.*;
import com.github.brnunes.swipeablerecyclerview.*;
import java.util.*;

import br.com.manoelmotoso.tomenota.R;

public class TesteRecicleView extends AppCompatActivity {
    private RecyclerView mRecyclerView;
	private ImageButton imgBtnNovaNota;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private   List<Nota> notas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_recicle_view);
        carregarLista();


        SwipeableRecyclerViewTouchListener.SwipeListener swipeListener = new SwipeableRecyclerViewTouchListener.SwipeListener(){

            @Override
            public boolean canSwipeLeft(int position) {
                return true;
            }

            @Override
            public boolean canSwipeRight(int position) {
                return true;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    NotaDAO dao = new NotaDAO(TesteRecicleView.this);
                    dao.deletarNota(notas.get(position));
                    notas.remove(position);

                    mAdapter.notifyItemRemoved(position);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {

					Toast.makeText(getApplicationContext(), notas.get(position).getTitulo() + "Abrir anotacão", Toast.LENGTH_SHORT).show();
                    Nota nota = notas.get(position);
					notas.remove(position);
					Intent intent = new Intent(getApplicationContext(), NovaNota.class);
					intent.putExtra("nota", nota);
					startActivity(intent);
					mAdapter.notifyItemRemoved(position);
                }
                mAdapter.notifyDataSetChanged();
            }
        };


        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(mRecyclerView,swipeListener);

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

		imgBtnNovaNota = (ImageButton) findViewById(R.id.adicionaNota);
		imgBtnNovaNota.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), NovaNota.class);
					startActivity(intent);

				}
			});

    }



	public void carregarLista(){
		NotaDAO dao = new NotaDAO(this);

        notas = dao.getNotas();
        dao.close();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardViewNotasAdapter(notas);
        mRecyclerView.setAdapter(mAdapter);


	}


}

