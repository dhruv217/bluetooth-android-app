package in.optimux.www.newremote;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private in.optimux.www.newremote.BleWrapper mBleWrapper = null;
    private ToggleButton toggleButton1, toggleButton2,toggleButton3,toggleButton4;
    private String m_address;
    private boolean mFoundDevice=false;
    private boolean ReadWriteComplete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton1=(ToggleButton)findViewById(R.id.toggleButton);
        toggleButton2=(ToggleButton)findViewById(R.id.toggleButton2);
        toggleButton3=(ToggleButton)findViewById(R.id.toggleButton3);
        toggleButton4=(ToggleButton)findViewById(R.id.toggleButton4);
    }

    void InitialiseBT()
    {
        if (mBleWrapper!=null) {
            mBleWrapper.diconnect();
            mBleWrapper.close();

            mBleWrapper=null;

        }

        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null() {


            @Override
            public void uiDeviceFound(final BluetoothDevice device,
                                      final int rssi,
                                      final byte[] record
            ) {
                String msg = "uiDeviceFound: " + device.getName() + ", " + rssi + ", "+rssi;
                //Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show());
                Context context = getApplicationContext();
                //CharSequence text = "Hello toast!";
                //device address
                m_address=device.getAddress();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();
                Log.d("DEBUG", "uiDeviceFound: " + msg);
                if (device.getName().equals("SimpleBLEPeripheral") == true) {
                    boolean status;
                    status = mBleWrapper.connect(device.getAddress().toString());
                    if (status == false) {
                        Log.d("DEBUG", "uiDeviceFound: Connection problem");
                    }
                    else {
                        Log.d("DEBUG", "uiDeviceFound: Connectected ..............."+device.getAddress().toString());
                        //this.
                        //prvCheck=200;
                        //sleep(1000);
                        toggleButton1.setEnabled(true);
                        toggleButton2.setEnabled(true);
                        toggleButton3.setEnabled(true);
                        toggleButton4.setEnabled(true);
                        /*
                        BTRead();
                        while (!ReadWriteComplete)
                            sleep(20);
                        //i= (byte) (ReadByte | (1<<6));
                        if(((byte)ReadByte&(1<<6))!=0) toggleButton1.setChecked(true);
                        if(((byte)ReadByte&(1<<5))!=0) toggleButton2.setChecked(true);
                        if(((byte)ReadByte&(1<<4))!=0) toggleButton3.setChecked(true);
                        if(((byte)ReadByte&(1<<3))!=0) toggleButton4.setChecked(true);
                        */
                        mFoundDevice=true;

                    }
                }
            }


            @Override
            public void uiAvailableServices(BluetoothGatt gatt,
                                            BluetoothDevice device,
                                            List<BluetoothGattService> services
            ) {
                for (BluetoothGattService service : services) {
                    String serviceName = BleNamesResolver.resolveUuid
                            (service.getUuid().toString());
                    Log.d("DEBUG", serviceName);
                    //MyText.setText("Device Found");


                }
            }

            @Override
            public void uiSuccessfulWrite( BluetoothGatt gatt,
                                           BluetoothDevice device,
                                           BluetoothGattService service,
                                           BluetoothGattCharacteristic ch,
                                           String description)
            {
                BluetoothGattCharacteristic c;
                super.uiSuccessfulWrite(gatt, device, service, ch, description);
                ReadWriteComplete=true;
               /*
                switch (mState)
                {
                    case ACC_ENABLE:
                        Log.d(LOGTAG, "uiSuccessfulWrite: Successfully enabled
                                accelerometer");
                        break;
                }
                */
            }
            @Override
            public void uiFailedWrite( BluetoothGatt gatt,
                                       BluetoothDevice device,
                                       BluetoothGattService service,
                                       BluetoothGattCharacteristic ch,
                                       String description) {
                super.uiFailedWrite(gatt, device, service, ch, description);

            }
            /*
            @Override
            public void uiNewRssiAvailable(final BluetoothGatt gatt, final BluetoothDevice device, final int rssi) {
                MyText.setText(rssi);
            }
            */
            @Override
            public void uiNewValueForCharacteristic(BluetoothGatt gatt,
                                                    BluetoothDevice device,
                                                    BluetoothGattService service,
                                                    BluetoothGattCharacteristic ch,
                                                    String strValue,
                                                    int intValue,
                                                    byte[] rawValue,
                                                    String timestamp)
            {
                super.uiNewValueForCharacteristic( gatt, device, service,
                        ch, strValue, intValue,
                        rawValue, timestamp);
                //Log.d("DEBUG", "uiNewValueForCharacteristic");
                /*
                myCheck=rawValue[0];
                prvCheck=0;
                ReadByte=rawValue[0];
                */
                ReadWriteComplete=true;
/*
                if(((byte)ReadByte&(1<<6))!=0) toggleButton1.setChecked(true);
                if(((byte)ReadByte&(1<<5))!=0) toggleButton2.setChecked(true);
                if(((byte)ReadByte&(1<<4))!=0) toggleButton3.setChecked(true);
                if(((byte)ReadByte&(1<<3))!=0) toggleButton4.setChecked(true);
*/
                //for (byte b:rawValue)
                {
                    //Log.d("DEBUG", "Val: " + b+ "     Time:"+timestamp);
                }

            }


        }


        );


        mBleWrapper.initialize();
        //mBleWrapper.readPeriodicalyRssiValue(true);
        if (mBleWrapper.checkBleHardwareAvailable() == false)
        {
            Toast.makeText(this, "No BLE-compatible hardware detected",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        /*
        //start the timer
        if (mTimer != null) // Cancel if already existed
        {
            mTimer.cancel();
            mTimer = null;
        }
        //else
        mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        */

    }

    void BTRead()
    {
        //public final static UUID MY_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
        //public final static UUID MY_CHAR = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
        ReadWriteComplete=false;
        gatt = mBleWrapper.getGatt();
        c = gatt.getService(MY_SERVICE).getCharacteristic(MY_CHAR);
        mBleWrapper.requestCharacteristicValue(c);

    }

    void BTWrite(byte value)
    {
        //public final static UUID MY_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
        //public final static UUID MY_CHAR = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
        byte [] b= new byte[1];
        b[0]=value;
        ReadWriteComplete=false;
        gatt = mBleWrapper.getGatt();
        c = gatt.getService(MY_SERVICE).getCharacteristic(MY_CHAR);
        mBleWrapper.writeDataToCharacteristic(c, b);
        ReadByte=value;

    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mBleWrapper.connect("C8:FD:19:43:67:4A");
            serviceStartHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Log.d("DEBUG", "Hello-------------------------------------------------------- ");
                    //if (mFoundDevice) {
/*
                        prvCheck++;
                        if (prvCheck>4) {


                            //InitialiseBT();
                            if(prvCheck>7) {
                                //while (!mBleWrapper.mConnected) {
                                mBleWrapper.connect(m_address);
                                prvCheck=7;
                                if(mBleWrapper.mConnected)
                                {
                                    prvCheck=0;
                                }
                            }
                            else
                            {
                                Log.d("DEBUG", "Connection Break-------------------------------------------------------- ");
                                //mFoundDevice=false;
                                mBleWrapper.close();
                                mBleWrapper.diconnect();
                                mBleWrapper.mConnected=false;

                            }

                            }
                        //
                        //Log.d("DEBUG", "Val: " + myCheck);
                        }
                */
                    // prvCheck = myCheck;
                    //mBleWrapper.connect("C8:FD:19:43:67:4A");
                    //mBleWrapper.initialize();
                    //BTDoExchange();
                    BTRead();
                    //MyText.setText(String.valueOf(ReadByte));
                    //MyText.setText(mBleWrapper.mBluetoothGatt.readRemoteRssi());
                    //CharSequence S=MyText.getText();
                    //S+= (byte)ReadByte;


                    Log.d("DEBUG", "Val: " + ReadByte);
                    if(((byte)ReadByte&(1<<6))!=0) toggleButton1.setChecked(true);
                    if(((byte)ReadByte&(1<<5))!=0) toggleButton2.setChecked(true);
                    if(((byte)ReadByte&(1<<4))!=0) toggleButton3.setChecked(true);
                    if(((byte)ReadByte&(1<<3))!=0) toggleButton4.setChecked(true);
                    //}
                }

            });

        }
    }

}


