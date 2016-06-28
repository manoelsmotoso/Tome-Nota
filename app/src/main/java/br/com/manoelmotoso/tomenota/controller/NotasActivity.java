package br.com.manoelmotoso.tomenota.controller;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.adapters.RecyclerViewAdapter;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class NotasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<Nota> mNotas;
    private FloatingActionButton mFabDeletaNota;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mFabDeletaNota = (FloatingActionButton) findViewById(R.id.deletaNota);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carregarLista();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Implementando animação e eventos  Swipe RecycleView
        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(mRecyclerView, new SwipeableRecyclerViewTouchListener.SwipeListener() {

            @Override
            public boolean canSwipeLeft(int position) {
                return true;
            }//Ativar ou desativar swipe para esquerda

            @Override
            public boolean canSwipeRight(int position) {
                return false;
            }//Ativar ou desativar swipe para direita

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (final int position : reverseSortedPositions) {
                    final NotaDAO dao = new NotaDAO(NotasActivity.this);
                    final Nota nota = mNotas.get(position);
                    dao.deletarNota(nota);
                    mNotas.remove(position);
                    Snackbar snackbar = Snackbar.make(recyclerView, "Anotação deletada", Snackbar.LENGTH_LONG).setAction("DESFAZER", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nota.set_id(0);
                            dao.gravarNota(nota);
                            carregarLista();
                            Snackbar.make(view, "Anotação restaurada!", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                    dao.close();

                    mAdapter.notifyItemRemoved(position);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        //Abre a tela de nova anotação
        FloatingActionButton fabNovaNota = (FloatingActionButton) findViewById(R.id.adicionaNota);
        fabNovaNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NovaNotaActivity.class);
                startActivity(intent);

            }
        });

        //Deleta Notas selessionadas  na lista
        mFabDeletaNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotaDAO dao = new NotaDAO(getApplicationContext());
                dao.deletarNotas(mAdapter.getIdsSelecionados());
                carregarLista();
            }
        });
    }


    //Configuarando o Drawer Layout
    private void setupDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_aberto, R.string.drawer_fechado) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerToggle.syncState();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Manoel Silvs Motoso");
            intent.putExtra(Intent.EXTRA_TEXT,"https://github.com/manoelsmotoso/Tome-Nota");
            startActivity(Intent.createChooser(intent,"Codigo do APP no GitHub"));
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    //Context menu

	/*
     @Override
	 public void onCreateContextMenu(ContextMenu menu, View v,
	 ContextMenuInfo menuInfo) {
	 super.onCreateContextMenu(menu, v, menuInfo);
	 MenuInflater inflater = getMenuInflater();
	 inflater.inflate(R.menu.context_menu, menu);

	 }

	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	 case R.id.option_delete:

	 return true;
	 case R.id.option_enviar:

	 return true;
	 case R.id.option_abrir:

	 return true;
	 default:
	 return super.onContextItemSelected(item);
	 }
	 }
	 */


    //Recarrega a RecycleView de mNotas.
    private void carregarLista() {
        NotaDAO dao = new NotaDAO(this);
        mNotas = dao.getNotas();
        dao.close();
        mAdapter = new RecyclerViewAdapter(mNotas, this);
        mFabDeletaNota.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
