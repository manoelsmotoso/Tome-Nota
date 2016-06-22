package br.com.manoelmotoso.tomenota.controller;


import android.content.*;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
	private FloatingActionButton fabNovaNota;
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
                for (final int position : reverseSortedPositions) {
                    final NotaDAO dao = new NotaDAO(TesteRecicleView.this);
                    final Nota nota = notas.get(position);
                    dao.deletarNota(nota);
                    notas.remove(position);
                    Snackbar snackbar = Snackbar
                            .make(recyclerView, "Anotação deletada", Snackbar.LENGTH_LONG)
                            .setAction("DESFAZER", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nota.set_id(0);
                                    dao.gravarNota(nota);
                                    carregarLista();
                                    Snackbar snackbar1 = Snackbar.make(view, "Anotação restaurada!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }
                            });

                    snackbar.show();
                    dao.close();

                    mAdapter.notifyItemRemoved(position);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {

					Toast.makeText(getApplicationContext(), notas.get(position).getTitulo() + "Abrir anotacão", Toast.LENGTH_SHORT).show();
                    Nota nota = notas.get(position);
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

		fabNovaNota = (FloatingActionButton) findViewById(R.id.adicionaNota);
        fabNovaNota.setOnClickListener(new View.OnClickListener() {
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

