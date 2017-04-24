package cn.labelnet.bluetoothdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.core.scan.BleBlueToothTest;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.scan.ble.BleScanResultCallback;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;
import cn.labelnet.bletooth.core.scan.ble.BleScanFilter;
import cn.labelnet.bletooth.util.LogUtil;
import cn.labelnet.bluetoothdemo.callback.ConnCallBack;

public class MainActivity extends AppCompatActivity {

    private BleBlueToothTest bleTooth;
    private ScanCallBack callBack;
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleTooth = new BleBlueToothTest(getApplicationContext());
        callBack = new ScanCallBack(5000);
        textView = (TextView) findViewById(R.id.textView);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.startScan(callBack);
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.stopScan(callBack);
            }
        });

        findViewById(R.id.btn_conn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleDevice bleDevice = callBack.getBleDevice();
                if (bleDevice == null) {
                    LogUtil.v("没有设备");
                    return;
                }
                bleTooth.connect(bleDevice, false, new ConnCallBack());
            }
        });

        findViewById(R.id.btn_conn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.disconnect();
            }
        });

        findViewById(R.id.btn_scan_and_conn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.scanAndConnect("05:04:03:02:01:00", false, new ConnCallBack());
            }
        });

        findViewById(R.id.btn_le_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LeBlueToothActivity.class));
            }
        });

        findViewById(R.id.btn_bletooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BleToothActivity.class));
            }
        });
    }

    private static class ScanCallBack extends BleScanResultCallback {

        private List<BleDevice> bleDevices;

        public ScanCallBack(long timeOutMillis) {
            super(timeOutMillis);
        }

        public BleDevice getBleDevice() {
            if (bleDevices != null && bleDevices.size() > 0) {
                return bleDevices.get(0);
            } else {
                return null;
            }
        }

        @Override
        protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
            LogUtil.v("----------------------------------------------------Position : " + position + " Rssi : " + rssi);
            if (position > -1) {
                BleDevice bleDevice = bleDevices.get(position);
                bleDevice.setRssi(rssi);
                bleDevices.set(position, bleDevice);
                updateInfo();
            }
        }

        @Override
        protected void onScanDevicesData(List<BleDevice> bleDevices) {
            this.bleDevices = bleDevices;
            updateInfo();
            LogUtil.v("=================================================== BleDevices : " + bleDevices);
        }

        @Override
        public void setBleToothScanStatus(BleScanStatus status) {
            LogUtil.v("当前状态 ： " + status);
        }

        @Override
        protected void bleToothScanProcess(float process) {
            LogUtil.v("Process : " + process);
        }

        @Override
        protected List<BleScanFilter> getScanFilter() {

            //CC2650 SensorTag
            //SimpleBLEPeripheral
            //小米手机 ： 77:97:A5:75:40:98
            List<BleScanFilter> filters = new ArrayList<>();
            filters.add(new BleScanFilter.Builder()
                    .setDeviceMac("77:97:A5:75:40:98")
                    .build());
            return filters;
        }

        private void updateInfo() {
            if (bleDevices.size() > 0) {
                textView.setText(bleDevices.toString());
            } else {
                textView.setText("没有设备");
            }
        }
    }


}
