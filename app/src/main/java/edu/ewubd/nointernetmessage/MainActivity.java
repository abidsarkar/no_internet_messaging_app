package edu.ewubd.nointernetmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private Button sendMessageButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        sendMessageButton = findViewById(R.id.send_message_button);
        statusText = findViewById(R.id.status_text);

        // Get Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check Bluetooth availability
        if (bluetoothAdapter == null) {
            statusText.setText("Bluetooth not available");
        } else {
            updateStatusText();
        }

        // Set up button click listener
        sendMessageButton.setOnClickListener(v -> {
            sendEmergencyMessage();
        });
    }

    private void updateStatusText() {
        if (bluetoothAdapter.isEnabled()) {
            statusText.setText("Bluetooth enabled");
        } else {
            statusText.setText("Bluetooth disabled");
        }
    }

    private void sendEmergencyMessage() {
        // Check Bluetooth enabled
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            // Start discovery and broadcasting
            bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

            // Placeholder for broadcasting message
            statusText.setText("Broadcasting emergency message...");
            // Implement the logic to send the message to a discovered device here
            // For example, you can use BluetoothSocket and OutputStream
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Send message to discovered device (implement this based on your requirements)
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Bluetooth has been enabled, proceed with your logic
                // You might want to start discovery and broadcast the message here
                bluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
                statusText.setText("Broadcasting emergency message...");
            } else {
                // User declined to enable Bluetooth, handle accordingly
                statusText.setText("Bluetooth not enabled");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
