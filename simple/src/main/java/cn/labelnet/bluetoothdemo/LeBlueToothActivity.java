package cn.labelnet.bluetoothdemo;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.peripheral.LeBlueToothTest;
import cn.labelnet.bletooth.core.scan.le.LeScanResultCallBack;
import cn.labelnet.bletooth.util.LogUtil;
import cn.labelnet.bluetoothdemo.callback.ConnCallBack;

public class LeBlueToothActivity extends AppCompatActivity {


    private static TextView tv;
    private LeBlueToothTest leBlueTooth;
    private LeScanSimpleCallBack leScanSimpleCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_blue_tooth);

        leBlueTooth = new LeBlueToothTest(getApplicationContext());
        leScanSimpleCallBack = new LeScanSimpleCallBack(5000);
        tv = (TextView) findViewById(R.id.tv_data);

        findViewById(R.id.btn_scan_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leBlueTooth.startScan(leScanSimpleCallBack);
            }
        });

        findViewById(R.id.btn_scan_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leBlueTooth.stopScan(leScanSimpleCallBack);
            }
        });

        findViewById(R.id.btn_conn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leBlueTooth.connect(leScanSimpleCallBack.getBleDevice(), false, new ConnCallBack());
            }
        });

        findViewById(R.id.btn_conn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leBlueTooth.disconnect();
            }
        });

        initPerlal();

    }

    private void initPerlal() {
        if (leBlueTooth.isCheckSupportPerpheral()) {

            Toast.makeText(this, "手机支持外围设备", Toast.LENGTH_SHORT).show();
            leBlueTooth.initGATTServer();

        } else {
            Toast.makeText(this, "该手机不支持外围设备", Toast.LENGTH_SHORT).show();
        }
    }


    private static class LeScanSimpleCallBack extends LeScanResultCallBack {


        private List<BleDevice> bleDevices;

        public BleDevice getBleDevice() {
            if (bleDevices != null && bleDevices.size() > 0) {
                return bleDevices.get(0);
            } else {
                return null;
            }
        }


        public LeScanSimpleCallBack(long timeOutMill) {
            super(timeOutMill);
        }

        @Override
        protected void onScanDevicesData(List<BleDevice> bleDevices) {
            this.bleDevices = bleDevices;
            updateInfo();
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
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtil.v("DeviceName : " + result.getDevice().getName() + " DeviceMac: " + result.getDevice().getAddress());
        }

        @Override
        protected void bleToothScanProcess(float process) {
            //进度
            LogUtil.v("Scan Process : " + process);
        }

        @Override
        protected void onScanStatus(int errorCode) {
            //状态
            printStatus(errorCode);
        }

        @Override
        public List<ScanFilter> getScanFilters() {
            //过滤
            //CC2650 SensorTag
            //SimpleBLEPeripheral
            List<ScanFilter> scanFilters = new ArrayList<>();
            ScanFilter filter1 = new ScanFilter
                    .Builder()
                    .build();
            scanFilters.add(filter1);
            return scanFilters;
        }

        @Override
        public ScanSettings getScanSettings(ScanSettings.Builder builder) {
            //配置
            return super.getScanSettings(builder);
        }

        private void updateInfo() {
            if (bleDevices.size() > 0) {
                tv.setText(bleDevices.toString());
            } else {
                tv.setText("没有设备");
            }
        }

        private void printStatus(int code) {
            String msg = "来自ScanCallBack";
            switch (code) {
                case 100:
                    msg = "scan start";
                    break;
                case 101:
                    msg = "scan end";
                    break;
                case 102:
                    msg = "scan stop";
                    break;
                case 103:
                    msg = "scan timeout";
                    break;
            }
            LogUtil.v("Scan Status ： " + msg);
        }
    }


}
