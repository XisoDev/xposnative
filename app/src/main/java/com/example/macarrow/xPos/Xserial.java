package com.example.macarrow.xPos;

import java.util.HashMap;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Xserial extends Activity {

    public static String TAG = "Xserial";
    public static String DEVICE_NAME = "/dev/bus/usb/001/006";
    public static int END_POINT_SEND = 1;
    public static int END_POINT_RECEIVE = 0;
    private static final int TIMEOUT = 3500;

    private boolean forceClaim = true;
    public byte[] receivedData;

    UsbManager manager;
    UsbDevice device;
    UsbDeviceConnection connection;
    UsbInterface intface;
    UsbEndpoint endpoint;
    PendingIntent pIntent;
    Button btn;
    Button send;
    Button receive;
    Button disconnect;
    TextView receivedView;

    boolean connected = false, mPermissionRequestPending = false, isOpen = false;;
    private static final String ACTION_USB_PERMISSION = "com.example.usbconnectiontest.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.xserial);

        pIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);

        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        btn = (Button)findViewById(R.id.button1);
        send = (Button)findViewById(R.id.button2);
        receive = (Button)findViewById(R.id.button3);
        disconnect = (Button)findViewById(R.id.button4);
        receivedView = (TextView)findViewById(R.id.textView1);

        btn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                setUpDevice();

            }
        });

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isOpen){
                    byte[] buffer = hexStringToByteArray("02000604FD4344039EA6");

                    new DataTransferThread(buffer, END_POINT_SEND).start();
                }

            }
        });

        receive.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                if(isOpen) {

                    new DataTransferThread(new byte[30], END_POINT_RECEIVE).start();
                    receivedView.setText(receivedData.toString());

                }
            }
        });

        disconnect.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                closeDevice();
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mUsbReceiver);
    }

    @Override
    public void onResume() {

        super.onResume();

        device = null;
        connection = null;

    }

    private void setUpDevice(){

        pIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        if (connected) return;
        if (manager.getDeviceList() == null) {

            Toast.makeText(getApplicationContext(), "No devices detected!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No list of devices!");
            return;

        }

        // load Device List
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Log.i(TAG, deviceList.toString());
        device = deviceList.get(DEVICE_NAME);

        if(device == null){

            Toast.makeText(getApplicationContext(), "No device detected!", Toast.LENGTH_SHORT).show();
            return;

        }

        manager.requestPermission(device, pIntent);

        ProgressDialog dialog = ProgressDialog.show(this, "", "Connecting...", true);

        while (!manager.hasPermission(device)) {

            synchronized (manager) {

                if (!mPermissionRequestPending) {
                    manager.requestPermission(device, pIntent);
                    mPermissionRequestPending = true;

                }
            }
        }

        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
        (new OpenDeviceThread()).start();

    }

    private void openDevice(UsbDevice device) {

        this.device = device;

        intface = device.getInterface(0);
        connection = manager.openDevice(device);
        connection.claimInterface(intface, forceClaim);
        isOpen = true;

		/*

		dc = manager.openDevice(device);

		if (dc != null) {

			FileDescriptor mFileDescriptor = dc.getFileDescriptor();
			mOutputStream = new FileOutputStream(mFileDescriptor);
			mInputStream = new FileInputStream(mFileDescriptor);
			Log.d(TAG, "device opened");
			connected = true;

		} else {

			Log.d(TAG, "device open fail");

		}

		*/

    }

    private void closeDevice() {

        if (connection != null) {

            connection.releaseInterface(intface);
            connection.close();

        }

        connected = false;
        isOpen = false;
        intface = null;
        connection = null;
        device = null;

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            UsbDevice device =(UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

            if (ACTION_USB_PERMISSION.equals(action)) {

                synchronized (this) {

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                        if (device != null) {

                            (new OpenDeviceThread()).start();

                            connected = true;

                        }

                    } else {

                        Log.d(TAG, "permission denied for device " + device);

                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                UsbDevice mDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if (device != null && mDevice.equals(device)) closeDevice();

            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }

    class OpenDeviceThread extends Thread{

        @Override
        public void start(){

            openDevice(device);

        }
    }

    class DataTransferThread extends Thread{

        private byte[] buffer;
        private int endpointIndex;

        public DataTransferThread(byte[] bytes, int endpointIndex){

            this.buffer = bytes;
            this.endpointIndex = endpointIndex;

        }

        @Override
        public void start(){

            endpoint = intface.getEndpoint(endpointIndex);
            Log.i(TAG, "direction: " + endpoint.getDirection());
            connection.bulkTransfer(endpoint, buffer, buffer.length, TIMEOUT);
            Log.d(TAG, "bytes transferred. - " + buffer);
            if(endpointIndex == 0) receivedData = buffer;

        }
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}