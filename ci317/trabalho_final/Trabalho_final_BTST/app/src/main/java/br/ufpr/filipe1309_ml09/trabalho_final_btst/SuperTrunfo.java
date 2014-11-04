package br.ufpr.filipe1309_ml09.trabalho_final_btst;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SuperTrunfo extends Activity {

    public static Activity st;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;

    private BluetoothService mBTService = null;

    // Views
    Button rb_selected;
    RadioGroup rg_card;
    TextView duration;
    TextView box_office;
    TextView oscar;
    TextView imdb;
    ImageView card_image;
    TextView tv_round;
    TextView tv_my_score;
    TextView tv_opponent_score;

    // Local variables
    ArrayList<Card> myCards = new ArrayList<Card>();
    String clientIds;
    int round, myCardRound, my_score, opponent_score;
    Card selectedCard;
    ProgressDialog ringProgressDialog;
    boolean initialData, newGame;

    public class Card {
        int card_image;
        int duration;
        int box_office;
        int oscar;
        double imdb;

        public Card(int card_image, int duration, int box_office, int oscar, double imdb){
            this.card_image = card_image;
            this.duration = duration;
            this.box_office = box_office;
            this.oscar = oscar;
            this.imdb = imdb;
        }
    }

    // The Handler that gets information back from the BluetoothService
    public final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
//                    Toast.makeText(getApplicationContext(),
//                            "MSG writed: "+ writeMessage,
//                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    if(!Globals.server && !initialData) {
//                        Toast.makeText(getApplicationContext(),
//                                "MSG received: "+ readMessage,
//                                Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                        reorganizeClientCards(readMessage);
                        initialData = true;
                        // Block radio buttons for player2(client) in the first round
                        for(int i = 0; i < rg_card.getChildCount(); i++){
                            (rg_card.getChildAt(i)).setEnabled(false);
                        }
                    } else {
                        defineResult(Integer.parseInt(readMessage));
                        round++;
                        myCardRound ++;
                        updateRound();
                        if (round < (myCards.size()/2)) {
                            selectedCard = myCards.get(myCardRound);
                            updateCard(selectedCard);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.end_game),
                                    Toast.LENGTH_SHORT).show();
                            changeStateOfRoundCard(false);
                            newGame();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_trunfo);

        setupBluetoothService();
        initCards();
        reorganizeCards();
        configureViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!initialData) {
            if (Globals.server) {
                //sendBtMessage(clientIds);
                initialData = true;
                //Toast.makeText(getApplicationContext(), "Server",
                //        Toast.LENGTH_SHORT).show();
                openAlert();
            } else {
                //Toast.makeText(getApplicationContext(), "Client",
                //        Toast.LENGTH_SHORT).show();
                if (mBTService != null)
                    launchRingDialog();
            }
        }
    }

    private void changeStateOfRoundCard(boolean b) {
        for(int i = 0; i < rg_card.getChildCount(); i++){
            (rg_card.getChildAt(i)).setEnabled(b);
        }
        if (round < ((myCards.size()/2))) {
            if(b) {
                Toast.makeText(this, getResources().getString(R.string.you_win), Toast.LENGTH_SHORT)
                        .show();
                my_score++;
            } else {
                Toast.makeText(this, getResources().getString(R.string.you_lost), Toast.LENGTH_SHORT)
                        .show();
                opponent_score++;
            }
        }
    }


    private void openAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuperTrunfo.this);
        alertDialogBuilder.setTitle(this.getTitle());
        alertDialogBuilder.setMessage(getResources().getString(R.string.start_game));
        alertDialogBuilder.setCancelable(false);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                sendBtMessage(clientIds);
                selectedCard = myCards.get(myCardRound);
                updateCard(selectedCard);
                updateRound();
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    private void newGameAlert(String playerMessage) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuperTrunfo.this);
        alertDialogBuilder.setTitle(playerMessage);
        alertDialogBuilder.setMessage(getResources().getString(R.string.start_new_game));
        alertDialogBuilder.setCancelable(false);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                newGame = true;
                recreate();
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    public void launchRingDialog() {
        ringProgressDialog = ProgressDialog.show(SuperTrunfo.this, getResources().getString(R.string.please_wait),	getResources().getString(R.string.waiting_opponent), true);
        ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    //Thread.sleep(10000);
                } catch (Exception e) {

                }
                //ringProgressDialog.dismiss();
            }
        }).start();
    }

    private void setupBluetoothService() {
        initialData = false;
        st = this;
        mBTService = Globals.myBTService;
        if(mBTService != null)
            mBTService.setHandler(messageHandler);
    }

    private void nextCard(int radioButton) {
        rb_selected = (Button) findViewById(radioButton);
        rb_selected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBTService != null)
                    sendBtMessage(String.valueOf(rb_selected.getId()));
                round++;
                myCardRound++;
                updateRound();
                if(round < ((myCards.size()/2))) {
                    selectedCard = myCards.get(myCardRound);
                    updateCard(selectedCard);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.end_game),
                            Toast.LENGTH_SHORT).show();
                    changeStateOfRoundCard(false);
                    newGame();
                }
            }
        });
    }

    private void newGame() {
        String msg = "";
        if (my_score > opponent_score)
            msg = getResources().getString(R.string.you_win_the_game);
        else if (my_score < opponent_score)
            msg = getResources().getString(R.string.you_lost_the_game);
        else
            msg = getResources().getString(R.string.tied_play_again);
        newGameAlert(msg);
    }

    private void updateCard(Card selectedCard) {
        card_image.setImageResource(selectedCard.card_image);
        duration.setText(getResources().getString(R.string.duration) +"      "+String.valueOf(selectedCard.duration)+" Min");
        box_office.setText(getResources().getString(R.string.box_office) +" $ "+String.valueOf(selectedCard.box_office)+" Mi");
        oscar.setText(String.valueOf(getResources().getString(R.string.oscars)+"         " +selectedCard.oscar));
        imdb.setText(String.valueOf(getResources().getString(R.string.imdb_score)+ "            " +selectedCard.imdb));
    }

    private void configureViews() {
        card_image = (ImageView) findViewById(R.id.card_image);
        duration = (RadioButton)findViewById(R.id.rb_duration);
        box_office = (RadioButton)findViewById(R.id.rb_boxOffice);
        oscar = (RadioButton)findViewById(R.id.rb_oscar);
        imdb = (RadioButton)findViewById(R.id.rb_imdb);
        rg_card = (RadioGroup) findViewById(R.id.rg_card);
        tv_round = (TextView) findViewById(R.id.tv_round);
        tv_my_score = (TextView) findViewById(R.id.tv_my_score);
        tv_opponent_score = (TextView) findViewById(R.id.tv_opponent_score);

        rg_card.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rb_duration) {
                    nextRound(checkedId);
                } else if (checkedId == R.id.rb_boxOffice) {
                    nextRound(checkedId);
                } else if (checkedId == R.id.rb_oscar) {
                    nextRound(checkedId);
                } else if (checkedId == R.id.rb_imdb) {
                    nextRound(checkedId);
                }
            }
        });
    }

    private void nextRound(int checkedId) {
        defineResult(checkedId);
        nextCard(checkedId);
        rg_card.clearCheck();
    }

    private void defineResult(int choice) {
        Card firstCard = myCards.get(round);
        Card secondCard =  myCards.get(round+(myCards.size()/2));
        switch (choice) {
            case R.id.rb_duration:
                defineWinner(firstCard.duration, secondCard.duration);
                break;
            case R.id.rb_boxOffice:
                defineWinner(firstCard.box_office, secondCard.box_office);
                break;
            case R.id.rb_oscar:
                defineWinner(firstCard.oscar, secondCard.oscar);
                break;
            case R.id.rb_imdb:
                if (firstCard.imdb > secondCard.imdb) {
                    if (Globals.server) {
                        changeStateOfRoundCard(true);
                    } else {
                        changeStateOfRoundCard(false);
                    }
                } else if (firstCard.imdb < secondCard.imdb) {
                    if (Globals.server) {
                        changeStateOfRoundCard(false);
                    } else {
                        changeStateOfRoundCard(true);
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.tied), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void defineWinner(int value, int otherValue) {
        if (value > otherValue) {
            if (Globals.server) {
               changeStateOfRoundCard(true);
            } else {
               changeStateOfRoundCard(false);
            }
        } else if (value < otherValue) {
            if (Globals.server) {
               changeStateOfRoundCard(false);
            } else {
                changeStateOfRoundCard(true);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.tied), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /*
        Reordena o vetor randomicamente, atribuindo a primeira metade para
        o player 1 e a segunda para o player2
    */
    private void reorganizeCards() {
        /*Mistura o baralho e envia os ids da primeira metade para o player2(client)*/
        if (Globals.server) {
            Collections.shuffle(myCards);
            myCardRound = 0;
            clientIds = "";
            for (int i = 0; i < (myCards.size()); i++) {
               clientIds = clientIds.concat(myCards.get(i).card_image+",");
            }
        } else {
            myCardRound = (myCards.size()/2);
        }
    }

    private void reorganizeClientCards(String ids) {
        String[] split = ids.split(",");
        for (int i = 0; i < split.length; i++) {
            for (int j = i; j < myCards.size(); j++) {
                if (myCards.get(j).card_image == Integer.parseInt(split[i])) {
                    changeCardPos(i, j);
                }
            }
        }
        selectedCard = myCards.get(myCardRound);
        updateCard(selectedCard);
        updateRound();
    }

    private void changeCardPos(int newPos, int i) {
        Card aux = myCards.get(newPos);
        myCards.set(newPos, myCards.get(i));
        myCards.set(i, aux);
    }

    private void initCards() {
        round = my_score = opponent_score = 0;
        newGame = false;
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
            Toast.makeText(this, getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT)
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

    private void updateRound() {
        tv_round.setText(""+(round+1));
        tv_my_score.setText(""+my_score);
        tv_opponent_score.setText(""+opponent_score);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBTService.stop();
        Globals.myBTService = null;
        Globals.server = false;
        Bluetooth.bt.recreate();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!newGame) {
            mBTService.stop();
            Globals.myBTService = null;
            Globals.server = false;
        }
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
