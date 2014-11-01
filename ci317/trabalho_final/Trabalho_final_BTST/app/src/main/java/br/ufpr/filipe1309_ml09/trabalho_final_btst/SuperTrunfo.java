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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SuperTrunfo extends Activity {

    static final int CLOSE_ST_ACTIVITY_REQUEST = 0;
    public static Activity st;

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

    // Views
    Button rb_selected;
    RadioGroup rg_card;
    TextView duracao;
    TextView bilheteria;
    TextView oscar;
    TextView imdb;
    ImageView card_image;

    ArrayList myCards = new ArrayList();
    int round=0;
    Card selectedCard;

    public class Card {
        int card_image;
        int duracao;
        int bilheteria;
        int oscar;
        double imdb;

        public Card(int card_image, int duracao, int bilheteria, int oscar, double imdb){
            this.card_image = card_image;
            this.duracao =duracao;
            this.bilheteria=bilheteria;
            this.oscar=oscar;
            this.imdb=imdb;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_trunfo);

        setupBluetoothService();
        initCards();
        randomCards();
        configureViews();
    }

    private void setupBluetoothService() {
        st = this;
        mBTService = Globals.myBTService;
        if(mBTService != null)
            mBTService.setHandler(messageHandler);
    }

    private void nextCard(int radioButton) {
        rb_selected = (Button) findViewById(radioButton);
        rb_selected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedCard=(Card) myCards.get(round % myCards.size());
                updateCard(selectedCard);
                if(mBTService != null)
                    sendBtMessage(String.valueOf(round));
                round++;
            }
        });
    }

    private void updateCard(Card selectedCard) {
        card_image.setImageResource(selectedCard.card_image);
        duracao.setText(String.valueOf(selectedCard.duracao)+" Min");
        bilheteria.setText("$ "+String.valueOf(selectedCard.bilheteria)+" M");
        oscar.setText(String.valueOf(selectedCard.oscar));
        imdb.setText(String.valueOf(selectedCard.imdb));
    }


    private void configureViews() {
        card_image = (ImageView) findViewById(R.id.card_image);
        duracao = (TextView)findViewById(R.id.tv_duration);
        bilheteria = (TextView)findViewById(R.id.tv_boxOffice);
        oscar = (TextView)findViewById(R.id.tv_oscar);
        imdb = (TextView)findViewById(R.id.tv_imdb);
        rg_card = (RadioGroup) findViewById(R.id.rg_card);

        rg_card.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rb_duration) {
                    Toast.makeText(getApplicationContext(), "choice: Duração",
                            Toast.LENGTH_SHORT).show();
                    nextCard(checkedId);
                    rg_card.clearCheck();
                } else if (checkedId == R.id.rb_boxOffice) {
                    Toast.makeText(getApplicationContext(), "choice: Bilheteria",
                            Toast.LENGTH_SHORT).show();
                    nextCard(checkedId);
                    rg_card.clearCheck();
                } else if (checkedId == R.id.rb_orcar) {
                    Toast.makeText(getApplicationContext(), "choice: Oscar",
                            Toast.LENGTH_SHORT).show();
                    nextCard(checkedId);
                    rg_card.clearCheck();
                } else if (checkedId == R.id.rb_imdb) {
                    Toast.makeText(getApplicationContext(), "choice: ImDB",
                            Toast.LENGTH_SHORT).show();
                    nextCard(checkedId);
                    rg_card.clearCheck();
                }
            }
        });
    }

    /*
        Reordena o vetor randomicamente, atribuindo a primeira metade para
        o player 1 e a segunda para o player2
    */
    private void randomCards() {
        Collections.shuffle(myCards);
    }

    private void initCards() {
        myCards.add(new Card(R.drawable.odisseia_no_espaco,160,190,1,8.3));
        myCards.add(new Card(R.drawable.avatar,162,2787,3,7.9));
        myCards.add(new Card(R.drawable.a_vida_e_bela,116,229,3,8.6));
        myCards.add(new Card(R.drawable.batman_o_cavaleiro_das_trevas,152,1004,2,9.0));
        myCards.add(new Card(R.drawable.circulo_de_fogo,132,411,0,7.1));
        myCards.add(new Card(R.drawable.coracao_valente,177,210,5,8.4));
        myCards.add(new Card(R.drawable.de_volta_para_o_futuro,116,381,1,8.5));
        myCards.add(new Card(R.drawable.discurso_do_rei,118,430,4,8.1));
        myCards.add(new Card(R.drawable.espera_de_um_milagre,189,286,0,8.5));
        myCards.add(new Card(R.drawable.gladiador,155,457,5,8.5));
        myCards.add(new Card(R.drawable.matrix,136,463,4,8.7));
        myCards.add(new Card(R.drawable.o_clube_da_luta,139,100,0,8.9));
        myCards.add(new Card(R.drawable.o_exterminador_do_futuro_dia_do_julgamento,137,519,4,8.5));
        myCards.add(new Card(R.drawable.o_lobo_de_wall_street,180,392,0,8.3));
        myCards.add(new Card(R.drawable.o_senhor_dos_aneis_o_retorno_do_rei,201,1119,11,8.9));
        myCards.add(new Card(R.drawable.porderoso_chefao,175,245,3,9.2));
        myCards.add(new Card(R.drawable.pulp_fiction,154,213,1,9.0));
        myCards.add(new Card(R.drawable.rei_leao,89,977,2,8.5));
        myCards.add(new Card(R.drawable.star_wars_v_o_imperio_contra_ataca,124,538,1,8.8));
        myCards.add(new Card(R.drawable.toy_story,81,360,0,8.3));
    }

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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(CLOSE_ST_ACTIVITY_REQUEST);
        mBTService.stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Intent intent = new Intent(getBaseContext(), Bluetooth.class);
        // flag para criar uma nova e destruir a que ja esta rodando (Bluetooth.java)
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
