package cn.labelnet.simpleperipheral;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.labelnet.bletooth_peripheral.BlePeripheral;
import cn.labelnet.bletooth_peripheral.callback.AdCallBack;
import cn.labelnet.bletooth_peripheral.config.data.AdData;
import cn.labelnet.bletooth_peripheral.config.setting.AdSettings;

public class MainActivity extends AppCompatActivity {


    private BlePeripheral mBlePeripheral;
    private AdPeripheralCallBack mAdPeripheralCallBack;
    private static TextView status;
    private static StringBuilder builder = new StringBuilder("ble peripheral status : ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.status);

        mAdPeripheralCallBack = new AdPeripheralCallBack();
        mBlePeripheral = new BlePeripheral(getApplicationContext(), true, mAdPeripheralCallBack);

        findViewById(R.id.btn_check_peripheral).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlePeripheral.isCheckSupportPerpheral()) {
                    setMsg("当前设备支持：Ble Peripheral");
                } else {
                    setMsg("当前设备不支持：Ble Peripheral");
                }
            }
        });

        findViewById(R.id.btn_start_peripheral).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlePeripheral.startPeripheral(new AdSettings(), new AdData());
            }
        });

        findViewById(R.id.btn_stop_peripheral).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlePeripheral.stopPeripheral();
            }
        });

        findViewById(R.id.btn_add_gatt_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add service
            }
        });

    }

    public static void setMsg(String msg) {
        builder.append("\r  " + msg);
        status.setText(builder.toString());
    }


    private static class AdPeripheralCallBack extends AdCallBack {
        @Override
        public void showMsg(String msg) {
            super.showMsg(msg);
            setMsg(msg);
        }

        @Override
        public void onStartSuccess() {
            super.onStartSuccess();
            setMsg("ble peripheral start success");
        }

        @Override
        public void onConnectSuccess() {
            super.onConnectSuccess();
            setMsg("device conn success");
        }

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onServiceAddSuccess() {
            super.onServiceAddSuccess();
            setMsg("gatt service add success");
        }

        @Override
        public void onServiceAddFail(BluetoothGattService service) {
            super.onServiceAddFail(service);
            setMsg("gatt service add fail");
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(mBluetoothGattServer, device, requestId, offset, characteristic);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(mBluetoothGattServer, device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(mBluetoothGattServer, device, requestId, offset, descriptor);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(mBluetoothGattServer, device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onExecuteWrite(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(mBluetoothGattServer, device, requestId, execute);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            super.onMtuChanged(device, mtu);
        }
    }

}
