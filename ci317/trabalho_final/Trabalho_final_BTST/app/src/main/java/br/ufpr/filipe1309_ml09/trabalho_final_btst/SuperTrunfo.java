package br.ufpr.filipe1309_ml09.trabalho_final_btst;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;


public class SuperTrunfo extends Activity {

    Button button;
    int TAM_CARDS=20;
    int round=0;
    final Integer[] myCards = new Integer[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_trunfo);

        initCards();
        randomCards();
        configureButtons();
    }

    private void configureButtons() {
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.test_image);
                image.setImageResource(myCards[round%20]);
                round++;
            }
        });
    }

    /*
        Reordena o vetor randomicamente, atribuindo a primeira metade para
        o player 1 e a segunda para o player2
    */
    private void randomCards() {
        for (int i = 0; i < TAM_CARDS; i ++) {
            Random r = new Random();
            int newPos = r.nextInt((TAM_CARDS-1) - 0) + 0;
            changePos(myCards,newPos,i);
            Log.i("Cards", "In Mod card[" + i + "] -> [" + newPos + "]");
        }
    }

    private void initCards() {
        myCards[0] = R.drawable.odisseia_no_espaco;
        myCards[1] = R.drawable.avatar;
        myCards[2] = R.drawable.a_vida_e_bela;
        myCards[3] = R.drawable.batman_o_cavaleiro_das_trevas;
        myCards[4] = R.drawable.circulo_de_fogo;
        myCards[5] = R.drawable.coracao_valente;
        myCards[6] = R.drawable.de_volta_para_o_futuro;
        myCards[7] = R.drawable.discurso_do_rei;
        myCards[8] = R.drawable.espera_de_um_milagre;
        myCards[9] = R.drawable.gladiador;
        myCards[10] = R.drawable.matrix;
        myCards[11] = R.drawable.o_clube_da_luta;
        myCards[12] = R.drawable.o_exterminador_do_futuro_dia_do_julgamento;
        myCards[13] = R.drawable.o_lobo_de_wall_street;
        myCards[14] = R.drawable.o_senhor_dos_aneis_o_retorno_do_rei;
        myCards[15] = R.drawable.porderoso_chefao;
        myCards[16] = R.drawable.pulp_fiction;
        myCards[17] = R.drawable.rei_leao;
        myCards[18] = R.drawable.star_wars_v_o_imperio_contra_ataca;
        myCards[19] = R.drawable.toy_story;
    }

    private void changePos(Integer[] card, int newPos, int i) {
        int aux = card[newPos];
        card[newPos] = card[i];
        card[i] = aux;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.super_trunfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
