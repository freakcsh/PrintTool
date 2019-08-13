package com.freak.printtool.hardware.module.label;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.freak.printtool.R;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.printer.sdk.usb.USBPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Activity appears as a dialog. It lists any paired devices and devices
 * detected in the area after discovery. When a device is chosen by the user,
 * the MAC address of the device is sent back to the parent Activity in the
 * result Intent.
 *
 * @param <V>
 * @author Freak
 * @date 2019/8/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)

public class UsbDeviceList<V> extends Activity {

    private ArrayAdapter<String> deviceArrayAdapter;
    private ListView mFoundDevicesListView;
    private Button scanButton;
    private Button backButton;
    private List<UsbDevice> deviceList;

    private BroadcastReceiver receiver;

    private static boolean isDeviceConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.label_device_list);

        setResult(Activity.RESULT_CANCELED);

        scanButton = (Button) findViewById(R.id.button_scan);

        scanButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.shortShow("开始扫描");

                doDiscovery();
            }
        });

        backButton = (Button) findViewById(R.id.button_bace);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

        deviceArrayAdapter = new ArrayAdapter<String>(this, R.layout.label_device_item);

        mFoundDevicesListView = (ListView) findViewById(R.id.paired_devices);
        mFoundDevicesListView.setAdapter(deviceArrayAdapter);
        mFoundDevicesListView.setOnItemClickListener(mDeviceClickListener);

        doDiscovery();

        /*<----------------------------------------------------->*/
        if (receiver == null) {

            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    switch (action) {

                        case UsbManager.ACTION_USB_DEVICE_ATTACHED:

                            doDiscovery();
                            break;

                        case UsbManager.ACTION_USB_DEVICE_DETACHED:

                            isDeviceConnected = false;
                            break;

                        case MyUsbManager.ACTION_USB_STATE:

                            boolean connected = intent.getBooleanExtra(MyUsbManager.USB_CONNECTED, false);
                            break;

                        default:
                            break;
                    }
                }
            };

        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(MyUsbManager.ACTION_USB_STATE);
        registerReceiver(receiver, intentFilter);
    }


    private void doDiscovery() {
        deviceArrayAdapter.clear();
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        deviceList = new ArrayList<UsbDevice>();
        for (UsbDevice device : devices.values()) {

            if (USBPort.isUsbPrinter(device)) {

                if (device.getProductId() == 22304) {
                    deviceArrayAdapter.add(device.getDeviceName() + "\nvid: "
                            + device.getVendorId() + " pid: "
                            + device.getProductId());
                    deviceList.add(device);
                }
            }
        }

        isDeviceConnected = true;
    }

    private void returnToPreviousActivity(UsbDevice device) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(UsbManager.EXTRA_DEVICE, device);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {

            if (isDeviceConnected) {
                returnToPreviousActivity(deviceList.get(position));
            } else {
                ToastUtil.shortShow("该设备未连接，请重新搜索");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class MyUsbManager {

        public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";

        /**
         * Boolean extra indicating whether USB is connected or disconnected.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
         */
        public static final String USB_CONNECTED = "connected";

        /**
         * Boolean extra indicating whether USB is configured.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
         */
        public static final String USB_CONFIGURED = "configured";

        /**
         * Boolean extra indicating whether confidential user data, such as photos, should be
         * made available on the USB connection. This variable will only be set when the user
         * has explicitly asked for this data to be unlocked.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
         */
        public static final String USB_DATA_UNLOCKED = "unlocked";

        /**
         * A placeholder indicating that no USB function is being specified.
         * Used to distinguish between selecting no function vs.
         */
        // the default function in {@link #setCurrentFunction(String)}.
        public static final String USB_FUNCTION_NONE = "none";

        /**
         * Name of the adb USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_ADB = "adb";

        /**
         * Name of the RNDIS ethernet USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_RNDIS = "rndis";

        /**
         * Name of the MTP USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_MTP = "mtp";

        /**
         * Name of the PTP USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_PTP = "ptp";

        /**
         * Name of the audio source USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_AUDIO_SOURCE = "audio_source";

        /**
         * Name of the MIDI USB function.
         * Used in extras for the {@link #ACTION_USB_STATE} broadcast
         */
        public static final String USB_FUNCTION_MIDI = "midi";
    }
}
