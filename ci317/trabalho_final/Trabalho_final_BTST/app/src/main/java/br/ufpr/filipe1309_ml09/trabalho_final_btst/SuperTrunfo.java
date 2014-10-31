package br.ufpr.filipe1309_ml09.trabalho_final_btst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SuperTrunfo extends Activity {

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private BluetoothService mBTService = null;

    // Debugging
    private static final String TAG = "Super Trunfo";
    private static final boolean D = true;

    public class Card{
        int card_img;
        int duracao;
        int bilheteria;
        int oscar;
        int imdb;

        public Card(int card_img, int duracao, int bilheteria, int oscar, int imdb){
            this.card_img = card_img;
            this.duracao =duracao;
            this.bilheteria=bilheteria;
            this.oscar=oscar;
            this.imdb=imdb;
        }
    }

    Button button;
    int TAM_CARDS=20;
    int round=0;
    final Integer[] myCards = new Integer[20];
    ArrayList baralho = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_trunfo);

        setupBluetoothService();
        initCards();
        randomCards();
        configureButtons();
    }

    private void setupBluetoothService() {
        mBTService = Globals.myBTService;
        mBTService.setHandler(messageHandler);
    }

    private void configureButtons() {
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.test_image);
                Card card2=(Card) baralho.get(round % baralho.size());
                image.setImageResource(card2.card_img);
                sendBtMessage(String.valueOf(round));
                round++;
            }
        });
    }

    /*
        Reordena o vetor randomicamente, atribuindo a primeira metade para
        o player 1 e a segunda para o player2
    */
    private void randomCards() {
        Collections.shuffle(baralho);
    }

    private void initCards() {
        baralho.add(new Card(R.drawable.odisseia_no_espaco,1,2,3,4));
        baralho.add(new Card(R.drawable.avatar,1,2,3,4));
        baralho.add(new Card(R.drawable.a_vida_e_bela,1,2,3,4));
        baralho.add(new Card(R.drawable.batman_o_cavaleiro_das_trevas,1,2,3,4));
        baralho.add(new Card(R.drawable.circulo_de_fogo,1,2,3,4));
        baralho.add(new Card(R.drawable.coracao_valente,1,2,3,4));
        baralho.add(new Card(R.drawable.de_volta_para_o_futuro,1,2,3,4));
        baralho.add(new Card(R.drawable.discurso_do_rei,1,2,3,4));
        baralho.add(new Card(R.drawable.espera_de_um_milagre,1,2,3,4));
        baralho.add(new Card(R.drawable.gladiador,1,2,3,4));
        baralho.add(new Card(R.drawable.matrix,1,2,3,4));
        baralho.add(new Card(R.drawable.o_clube_da_luta,1,2,3,4));
        baralho.add(new Card(R.drawable.o_exterminador_do_futuro_dia_do_julgamento,1,2,3,4));
        baralho.add(new Card(R.drawable.o_lobo_de_wall_street,1,2,3,4));
        baralho.add(new Card(R.drawable.o_senhor_dos_aneis_o_retorno_do_rei,1,2,3,4));
        baralho.add(new Card(R.drawable.porderoso_chefao,1,2,3,4));
        baralho.add(new Card(R.drawable.pulp_fiction,1,2,3,4));
        baralho.add(new Card(R.drawable.rei_leao,1,2,3,4));
        baralho.add(new Card(R.drawable.star_wars_v_o_imperio_contra_ataca,1,2,3,4));
        baralho.add(new Card(R.drawable.toy_story,1,2,3,4));
//        myCards[0] = R.drawable.odisseia_no_espaco;
//        myCards[1] = R.drawable.avatar;
//        myCards[2] = R.drawable.a_vida_e_bela;
//        myCards[3] = R.drawable.batman_o_cavaleiro_das_trevas;
//        myCards[4] = R.drawable.circulo_de_fogo;
//        myCards[5] = R.drawable.coracao_valente;
//        myCards[6] = R.drawable.de_volta_para_o_futuro;
//        myCards[7] = R.drawable.discurso_do_rei;
//        myCards[8] = R.drawable.espera_de_um_milagre;
//        myCards[9] = R.drawable.gladiador;
//        myCards[10] = R.drawable.matrix;
//        myCards[11] = R.drawable.o_clube_da_luta;
//        myCards[12] = R.drawable.o_exterminador_do_futuro_dia_do_julgamento;
//        myCards[13] = R.drawable.o_lobo_de_wall_street;
//        myCards[14] = R.drawable.o_senhor_dos_aneis_o_retorno_do_rei;
//        myCards[15] = R.drawable.porderoso_chefao;
//        myCards[16] = R.drawable.pulp_fiction;
//        myCards[17] = R.drawable.rei_leao;
//        myCards[18] = R.drawable.star_wars_v_o_imperio_contra_ataca;
//        myCards[19] = R.drawable.toy_story;
    }

//    private void changePos(ArrayList card, int newPos, int i) {
//        int aux = card[newPos];
//        card[newPos] = card[i];
//        card[i] = aux;
//    }

    private void sendBtMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBTService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "Você não esta conectado a um dispositivo", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBTService.write(send);
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(getApplicationContext(),
                            "MSG writed: "+ writeMessage,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(getApplicationContext(),
                            "MSG received: "+ readMessage,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
