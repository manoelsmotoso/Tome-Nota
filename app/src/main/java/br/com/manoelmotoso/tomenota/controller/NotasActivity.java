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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.List;

import br.com.manoelmotoso.tomenota.R;
import br.com.manoelmotoso.tomenota.adapters.CardViewAdapter;
import br.com.manoelmotoso.tomenota.dao.NotaDAO;
import br.com.manoelmotoso.tomenota.model.Nota;

public class NotasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    private List<Nota> notas;
    private FloatingActionButton fabDeletaNota;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fabDeletaNota = (FloatingActionButton) findViewById(R.id.deletaNota);

        setupDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carregarLista();

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);getSupportActionBar().setHomeButtonEnabled(true);

        //Implementando animação e eventos  Swipe RecycleView
        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(mRecyclerView, new SwipeableRecyclerViewTouchListener.SwipeListener() {

            @Override
            public boolean canSwipeLeft(int position) {return true;}

            @Override
            public boolean canSwipeRight(int position) {return false;}

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (final int position : reverseSortedPositions) {
                    final NotaDAO dao = new NotaDAO(NotasActivity.this);
                    final Nota nota = notas.get(position);
                    dao.deletarNota(nota);
                    notas.remove(position);
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
                for (int position : reverseSortedPositions) {  }
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

        //Deleta notas marcadas na lista
        fabDeletaNota.setOnClickListener(new View.OnClickListener() {
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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer ,R.string.drawer_aberto, R.string.drawer_fechado){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toggle.syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toggle.setDrawerIndicatorEnabled(true);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_manage){
            Toast.makeText(NotasActivity.this, "NAV_MANAGE", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_send){
            Toast.makeText(NotasActivity.this, "NAV_SEND", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_share){
            Toast.makeText(NotasActivity.this, "NAV_SHARE", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    //Recarrega a RecycleView de notas.
    private void carregarLista() {
        NotaDAO dao = new NotaDAO(this);
        notas = dao.getNotas();
        dao.close();
        mAdapter = new CardViewAdapter(notas, this);
        fabDeletaNota.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}