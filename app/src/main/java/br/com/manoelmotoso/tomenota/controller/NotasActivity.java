package br.com.manoelmotoso.tomenota.controller;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.adapters.RecyclerViewAdapter;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class NotasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<Nota> mNotas;
    private FloatingActionButton mFabDeletaNota;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        mFabDeletaNota = (FloatingActionButton) findViewById(R.id.deletaNota);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new LinearLayoutManager(this);
        }else{
            mLayoutManager = new GridLayoutManager(this,2);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NotaDAO dao = new NotaDAO(this);
        this.mNotas = dao.getNotas();
        dao.close();
        carregarLista(mNotas);

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
                    final NotaDAO dao = new NotaDAO(getApplicationContext());
                    final Nota notaRemovida = mNotas.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    dao.deletarNota(notaRemovida);
                    carregarLista(dao.getNotas());
                    Snackbar snackbar = Snackbar.make(recyclerView, "Anotação deletada", Snackbar.LENGTH_LONG)
                            .setAction("DESFAZER", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notaRemovida.set_id(0);
                            dao.gravarNota(notaRemovida);
                            carregarLista(dao.getNotas());

                        }
                    });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                    dao.close();
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
                carregarLista(dao.getNotas());
                dao.close();
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
            intent.putExtra(Intent.EXTRA_SUBJECT,"Manoel Silvsa Motoso");
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



    //Recarrega a RecycleView de mNotas.
    private void carregarLista(ArrayList<Nota> mNotas) {
        this.mNotas = mNotas;
        mAdapter = new RecyclerViewAdapter(this.mNotas, this);
        mFabDeletaNota.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista(new NotaDAO(this).getNotas());
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
