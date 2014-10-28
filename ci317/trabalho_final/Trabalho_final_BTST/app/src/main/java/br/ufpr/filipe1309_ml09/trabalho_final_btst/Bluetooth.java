package br.ufpr.filipe1309_ml09.trabalho_final_btst;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class Bluetooth extends Activity {

    BluetoothAdapter mBluetoothAdapter = null;
    ConnectedThread mConnectedThread;
    Button bt_find_stop;
    Button bt_list;
    Button bt_disc;
    Button bt_on;
    Button bt_off;
    Button bt_data;
    TextView tv_text;
    TextView tv_pared;
    int DISCOVERABLE_DURATION = 15;
    ArrayAdapter<String> mArrayAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<BluetoothDevice> devices;
    ListView listView;
    // Server
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ConnectThread mConnectThread;
    AcceptThread mAcceptThread;
    private int isConnected;
    // Status  for Handler
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    public static final int ANSWER = 2;
    public static final int DISCONNECTED = 0;
    public static final int CONNECTED = 1;
    public static final int CONNECTING = 2;

    // É um requestCode(qualquer inteiro > 0 único), que pode ser checado
    // com onActivityResult()
    private static final int REQUEST_ENABLE_BT = 1;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        setConfigs();

        // Verifica se o aparelho possui Bluetooth
        if(mBluetoothAdapter == null) {
            bt_find_stop.setEnabled(false);
            bt_disc.setEnabled(false);
            tv_text.setText("Status: not supported");
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            configButtons();
            setHandler();
            configList();
            registerBR();
        }
    }

    private void setHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS:
                        tv_text.setText("Status: Device connected");
                        mConnectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                        mConnectedThread.start();
                        Toast.makeText(getBaseContext(), "Device Connected", Toast.LENGTH_LONG).show();
                        //tv_text.setText("Status: Device Connected");
                        // Mensagem que será enviada ao dispositivo conectado
                        setSendMessage(mConnectedThread);
                        bt_data.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String s = "successfully connected - create";
                                mConnectedThread.write(s.getBytes());
                                Toast.makeText(getBaseContext(),"MSG Enviada", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case ANSWER:
                        byte[] readBuf = (byte[])msg.obj;
                        String string = new String(readBuf);
                        Toast.makeText(getApplicationContext(), "MSG Recebida: "+string, Toast.LENGTH_LONG).show();
                        //tv_text.setText("Status: Device connected - Message received");
                        //Toast.makeText(getApplicationContext(),"Device Connected, msg: " + (String) msg.obj, Toast.LENGTH_LONG).show();
                        tv_text.setText("Status: Device connected - msg: "+ (String) msg.obj);
                        break;
                    case FAIL:
                        Toast.makeText(getApplicationContext(), "Fail: " + (String) msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"default: " + (String) msg.obj, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    public int isConnected() {
        return isConnected;
    }
    public void setConnected(int isConnected) {

        this.isConnected = isConnected;
    }

    public void setSendMessage(ConnectedThread mConnectedThread) {
        bt_data = (Button) findViewById(R.id.bt_send_data);
        bt_data.setVisibility(View.VISIBLE);

        listView.setVisibility(View.INVISIBLE);
        String s = "successfully connected - sets";
        mConnectedThread.write(s.getBytes());
    }

    private void configList() {
        // Array para dispositivos pareados e descobertos
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView = (ListView) findViewById(R.id.lv_pared_devices);
        listView.setAdapter(mArrayAdapter);
    }

    private void configButtons() {
        if(!mBluetoothAdapter.isEnabled()) {
            bluetooth_on();
        }

        bt_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeviceVisible();
            }
        });
        // Descobrindo dispositivos
        bt_find_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find();
            }
        });
    }

    private void setConfigs() {
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_pared = (TextView) findViewById(R.id.tv_pared);
        bt_disc = (Button) findViewById(R.id.bt_disc);
        bt_find_stop = (Button) findViewById(R.id.bt_find_stop);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devices = new ArrayList<BluetoothDevice>();
    }

    public void registerBR() {
        // Registrando o BroadcastReceiver, desregistrar no onDestroy()
        IntentFilter filter = new IntentFilter();
        // Para identificar pelo Broadcast Receiver quando um dispositivo por encontrado
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // Para identificar o final do modo de descoberta
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // Para decobrir quando saiu/entrou do modo visível
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        // Para verificar se o bluetooth foi desativado
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver,filter);
        //registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    // Descobrindo dispositivos
    // Criando um BroadcatReceiver para ACTION_FOUND, para receber informações
    // sobre  cada dispositivo descoberto
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Quando 'discovery' encontrar um dispositivo
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // obtem o objeto BluetoothDevice (remoto) do intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
                boolean foundPared = false;
                // Se encontrar dispositivo já pareado
                /*for (BluetoothDevice paredDevice: pairedDevices)  {
                    if(device.getName().equals(paredDevice.getName())){
                        mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" +
                                "(Finded/Pared)");
                        foundPared = true;
                        break;
                    }
                }
                if(!foundPared)*/
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + "(Finded)");
                mArrayAdapter.notifyDataSetChanged();


            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(mBluetoothAdapter.isEnabled()) {
                    /*for (BluetoothDevice paredDevice: pairedDevices)  {
                        for (int j = 0; j < mArrayAdapter.getCount(); j++)
                        if(mArrayAdapter.getItem(j).contains(paredDevice.getName().toString())){
                            mArrayAdapter.getItem(j).concat("(Pared)");
                            break;
                        }
                    }*/
                    /*for (int i = 0; i < pairedDevices.size(); i++) {
                        for (int j = 0; j < mArrayAdapter.getCount(); j++)
                        if(mArrayAdapter.getItem(j).contains(pairedDevices.toArray()))
                    }*/
                    bt_find_stop.setEnabled(true);
                }
                bt_find_stop.setText(R.string.find_stop);
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                // Quando mudar modo visivel
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,0);
                //int scanModePrevious = intent.getIntExtra(
                //        BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0);

                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    bt_disc.setEnabled(false);
                } else {
                    bt_disc.setEnabled(true);
                }
            } else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Se o Bluetooth for desligado, solicitar reativação
                if(mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_OFF){
                    turnOnBT();
                } else if(mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_ON) {
                    tv_pared.setText("Selecione um dispositivo para conectar");
                    bluetooth_list_pared();
                    mAcceptThread = new AcceptThread();
                    mAcceptThread.start();
                }
            }
        }
    };

    private void turnOnBT() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public void setDeviceVisible() {
        // Se o Bluetooth não estiver ativo, então ele será
        // automaticamenta ativado
        Intent enablaBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enablaBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,DISCOVERABLE_DURATION);
        startActivityForResult(enablaBtIntent, REQUEST_ENABLE_BT);
        //bt_disc.setEnabled(false);
    }



    public void find() {
        // Se estiver buscando disposito então para de buscar
        if (mBluetoothAdapter.isEnabled())
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            } else { // inicia busca
                mArrayAdapter.clear();
                bt_find_stop.setEnabled(false);
                bt_find_stop.setText("Pesquisando...");
                mBluetoothAdapter.startDiscovery();
            }
    }

    public void bluetooth_on() {
        // Verifica se o Bluetooth está ativo
        if(!mBluetoothAdapter.isEnabled()) {
            // Solicita a ativação do Bluetooth
            turnOnBT();
        }
    }

    public void bluetooth_list_pared(){
        // Buscando na lista de aplicativos pareados
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        // Se existirem aparelhos pareados
        if(pairedDevices.size() > 0) {

            mArrayAdapter.clear();
            // loop na lista de aparelhos pareados
            for (BluetoothDevice device: pairedDevices) {
                // Adiciona o name e o MAC adress em um array adapter para mostrar em uma ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + "Pared");
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (mBluetoothAdapter.isDiscovering())
                        mBluetoothAdapter.cancelDiscovery();

                    //pairedDevices[i];
                    int pos = 0;
                    //Toast.makeText(getApplicationContext(),"Connecting to  "+mArrayAdapter.getItem(i),
                    //        Toast.LENGTH_SHORT).show();
                    for (BluetoothDevice device: pairedDevices) {
                        if(pos == i) {
                            //if (mArrayAdapter.getItem(i).contains("(Finded)(Pared)")) {
                            Toast.makeText(getApplicationContext(), "Connecting with  " + device.getName(),
                                    Toast.LENGTH_SHORT).show();

                            tv_text.setText("Status: Connecting with "+ device.getName());
                            setConnected(CONNECTING);
                            mConnectThread = new ConnectThread(device);
                            mConnectThread.start();
                            if(isConnected() == CONNECTED)
                                tv_text.setText("Status: Connected with "+ device.getName());
                            break; // Depois que encontrar pode sair do for
                            //}
                        }
                        pos++;
                    }

                    //mBluetoothAdapter.getRemoteDevice(adress);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),"Paired Devices not found",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT) {
            tv_text.setText("Status: Enable");
        } else {
            tv_text.setText("Status: Disable");
        }

        /*
        * se o usuário aceitar(YES) entrar em modo visivel(discoverable), então o result Code
        * será igual a duração em que o aparecho ficará neste modo, caso contrário,
        * (NO) ou se um erro ocorrer, o result Code será RESULT_CANCELED
        * */
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null)
            unregisterReceiver(mReceiver);
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }*/

    // Server
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(getName(), MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public BluetoothServerSocket getMmServerSocket() {
            return mmServerSocket;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    //manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                        break;
                    } catch (IOException e) {
                        //TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            setConnected(DISCONNECTED);
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }

    }

    //Client
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                mHandler.obtainMessage(FAIL, "ct" + e.getMessage()).sendToTarget();
                setConnected(DISCONNECTED);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                mHandler.obtainMessage(FAIL, "ct.run: "+ mmDevice.getName() + " "  + connectException.getMessage()).sendToTarget();
                //tv_text.setText("Status: Connection Error: " + connectException.toString());
                //Toast.makeText(getBaseContext()," Error to connect with " + mmDevice.getName(), Toast.LENGTH_LONG).show();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    mHandler.obtainMessage(FAIL, closeException.getMessage()).sendToTarget();
                    setConnected(DISCONNECTED);
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);

            // Handler para comunicação da thread com a MainActivity (a thread principal)
            mHandler.obtainMessage(SUCCESS, mmSocket).sendToTarget();
        }


        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                mHandler.obtainMessage(FAIL,"cted"+ e.getMessage()).sendToTarget();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            //String info = "";
            int bytes;
            setConnected(CONNECTED);
            while (true) {
                // Read from the InputStream
                byte buffer[] = new byte[2048];
                try {
                    bytes = mmInStream.read(buffer);
                    mHandler.obtainMessage(ANSWER, bytes, -1, buffer)
                            .sendToTarget();
                    //InputStreamReader dinput = new InputStreamReader(mmInStream);
                    //BufferedReader bufferedReader = new BufferedReader( dinput );
                    //info = bufferedReader.readLine();
                    //mHandler.obtainMessage(ANSWER, info).sendToTarget();
                    //info ="";

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                mHandler.obtainMessage(FAIL,"w.cted"+ e.getMessage()).sendToTarget();
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
