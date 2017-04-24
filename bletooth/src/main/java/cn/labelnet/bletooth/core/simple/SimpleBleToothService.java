package cn.labelnet.bletooth.core.simple;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

import cn.labelnet.bletooth.core.BaseBleToothService;
import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.data.filter.BleBluetoothGattStatus;
import cn.labelnet.bletooth.data.filter.BleBluetoothUUIDFilter;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.core.simple
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 8:12 PM 2/10/2017
 * @Desc Desc
 * BaseBletoothService example
 */

public class SimpleBleToothService extends BaseBleToothService {

    public static final String SIMPLE_EXTRA_ACTION = "simple_extra_action_key";

    public static final String SIMPLE_EXTRA_UUID_KEY = "simple_extra_uuid_key";
    public static final String SIMPLE_EXTRA_VALUE_KEY = "simple_extra_value_key";

    //conn
    public static final String SIMPLE_BLE_CONN_SUCCESS = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.conn.success";
    public static final String SIMPLE_BLE_CONN_FAIL = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.conn.fail";
    //filter
    public static final String SIMPLE_BLE_FILTER_SUCCESS = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.filter.success";
    //read
    public static final String SIMPLE_BLE_CHARACTERISTIC_READ = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.characteristic.read";
    //write
    public static final String SIMPLE_BLE_CHARACTERISTIC_WRITE = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.characteristic.write";
    //notification
    public static final String SIMPLE_BLE_CHARACTERISTIC_notify = "cn.labelnet.bletooth.core.simple.SimpleBleToothService.ble.characteristic.notify";


    //Service UUID
    private static final String SIMPLE_SREVICE_UUID = "f000aa80-0451-4000-b000-000000000000";
    //Data UUID
    private static final String SIMPLE_CHARACTERISTIC_DATA_UUID = "f000aa81-0451-4000-b000-000000000000";
    private static final String SIMPLE_DATA_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    //Config UUID
    private static final String SIMPLE_CHARACTERISTIC_CONFIG_UUID = "f000aa82-0451-4000-b000-000000000000";
    //Period UUID
    private static final String SIMPLE_CHARACTERISTIC_PERID_UUID = "f000aa82-0451-4000-b000-000000000000";

    /**
     * (1) conn callback
     * (2) operation callback
     */
    private BleGattCallback mBleGattCallback = new BleGattCallback() {

        private List<BleService> bleServices;

        @Override
        public void setBleConnStatus(BleConnStatus status) {
            switch (status) {
                case success:
                    sendBroadCastUpdate(SIMPLE_BLE_CONN_SUCCESS);
                    break;
                case fail:
                    sendBroadCastUpdate(SIMPLE_BLE_CONN_FAIL);
                    break;
            }
        }

        @Override
        protected void onFilterBluetoothGattStatus(BleBluetoothGattStatus status, String msg) {
            switch (status) {
                case InitFilterSuccess:
                case InitAllSuccess:
                    sendBroadCastUpdate(SIMPLE_BLE_FILTER_SUCCESS);
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            printServices(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            sendBroadCastUpdate(SIMPLE_BLE_CHARACTERISTIC_READ, characteristic.getUuid().toString(), characteristic.getValue());
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            //无处理
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            sendBroadCastUpdate(SIMPLE_BLE_CHARACTERISTIC_WRITE, characteristic.getUuid().toString(), characteristic.getValue());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (SIMPLE_CHARACTERISTIC_DATA_UUID.equals(characteristic.getUuid().toString())) {
                //filter
                sendBroadCastUpdate(SIMPLE_BLE_CHARACTERISTIC_notify, SIMPLE_CHARACTERISTIC_DATA_UUID, characteristic.getValue());
            }
        }

        @Override
        protected BleBluetoothUUIDFilter onFilterBluetoothGattService(BleBluetoothUUIDFilter.Builder builder) {
            //过滤Service，仅仅获取使用的Service!

            builder.startBuilderService() // must be startBuilderService!!!
                    .addService(SIMPLE_SREVICE_UUID)
                    .addCharacteristic(SIMPLE_CHARACTERISTIC_DATA_UUID, SIMPLE_DATA_DESCRIPTOR_UUID)
                    .addCharacteristic(SIMPLE_CHARACTERISTIC_CONFIG_UUID)
                    .addCharacteristic(SIMPLE_CHARACTERISTIC_PERID_UUID)
                    .endBuilderService();//must be endBuilderService!!!!

            return builder.build();
        }

        @Override
        protected void onFilterBluetoothGattResult(List<BleService> bleServices) {
            super.onFilterBluetoothGattResult(bleServices);
            this.bleServices = bleServices;
        }

        public void printServices(BluetoothGatt gatt) {
            if (gatt != null) {
                for (BluetoothGattService service : gatt.getServices()) {
                    LogUtil.e("service: " + service.getUuid());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        LogUtil.e("  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                            LogUtil.e("     descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                        }
                    }
                }
            }
        }

    };


    /**
     * 发送广播
     *
     * @param action action
     */
    private void sendBroadCastUpdate(String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void sendBroadCastUpdate(String action, String uuid, byte[] values) {
        final Intent intent = new Intent(action);
        intent.putExtra(SIMPLE_EXTRA_UUID_KEY, uuid);
        intent.putExtra(SIMPLE_EXTRA_VALUE_KEY, values);
        sendBroadcast(intent);
    }

    @Override
    protected BleGattCallback getBleGattCallback() {
        return mBleGattCallback;
    }

    @Override
    protected BaseBleToothService getBaseBleToothService() {
        return SimpleBleToothService.this;
    }
}
