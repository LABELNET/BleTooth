package cn.labelnet.bletooth.data.filter;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.labelnet.bletooth.core.bean.BleCharacteristic;
import cn.labelnet.bletooth.core.bean.BleCharacteristicUUID;
import cn.labelnet.bletooth.core.bean.BleDescriptor;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.bean.BleServiceUUID;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:59 PM 2/9/2017
 * @Desc Desc
 * <p>
 * (1) filter uuid
 * (2) getBleFilterUUID() is null ? parserAll Service,Characteristic,Descriptor : parserFilter Service,Characteristic,Descriptor
 */

public abstract class BaseBleBluetoothGattFilter implements Runnable {

    private List<BleService> currBleServices;
    protected BluetoothGatt mGatt;

    public BaseBleBluetoothGattFilter() {
        currBleServices = new ArrayList<>();
    }

    public BaseBleBluetoothGattFilter(BluetoothGatt gatt) {
        this.mGatt = gatt;
    }

    @Override
    public void run() {
        if (mGatt == null) {
            throw new IllegalArgumentException("BleDataCenter init BluetoothGatt is null");
        }
        initGattService(mGatt);
    }

    protected List<BleService> getCurrBleServices() {
        return currBleServices;
    }

    /**
     * filter gatt service
     *
     * @param gatt Bluetooth Gatt
     */
    private void initGattService(BluetoothGatt gatt) {
        List<BluetoothGattService> services = gatt.getServices();
        List<BleServiceUUID> serviceUUIDs = getBleFilterUUID();

        if (services == null || services.size() == 0) {
            String DeviceInfo = gatt.getDevice().getName() + " : " + gatt.getDevice().getAddress();
            onBleDataStatus(BleBluetoothGattStatus.NotAllService, "Device　：" + DeviceInfo + " NotAllService");
            return;
        }

        if (serviceUUIDs == null || serviceUUIDs.size() == 0) {

            //no filter
            for (BluetoothGattService gattService : gatt.getServices()) {
                if (gattService != null) {
                    String uuid = gattService.getUuid().toString();
                    BleService.Builder builder = BleService.newBuilder()
                            .setUUID(uuid)
                            .setService(gattService);
                    currBleServices.add(parserGattCharacteristic(builder, gattService, null));
                }
            }
            onBleDataStatus(BleBluetoothGattStatus.InitAllSuccess, "ALL Data : " + currBleServices.toString());
            return;
        }
        //filter
        for (BleServiceUUID serviceUUID : serviceUUIDs) {
            String uuid = serviceUUID.getServiceUUID();
            BluetoothGattService service = gatt.getService(UUID.fromString(uuid));
            if (service != null) {
                BleService.Builder builder = BleService.newBuilder()
                        .setUUID(uuid)
                        .setService(service);
                currBleServices.add(parserGattCharacteristic(builder, service, serviceUUID.getBleCharacteristicUUIDs()));
            } else {
                LogUtil.e("BluetoothGattService is't found UUID : " + serviceUUID);
                onBleDataStatus(BleBluetoothGattStatus.NotFoundService, uuid);
            }
        }
        onBleDataStatus(BleBluetoothGattStatus.InitFilterSuccess, "Filter Data : " + currBleServices.toString());
    }

    /**
     * filter gatt Characteristic
     *
     * @param builder                bleservice.builder
     * @param service                gatt service
     * @param bleCharacteristicUUIDs filter uuids
     */
    private synchronized BleService parserGattCharacteristic(BleService.Builder builder, BluetoothGattService service, List<BleCharacteristicUUID> bleCharacteristicUUIDs) {

        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        List<BleCharacteristic> characteristicList = new ArrayList<>();

        if (characteristics == null || characteristics.size() == 0) {
            //service no characteristic
            onBleDataStatus(BleBluetoothGattStatus.NotAllCharacteristic, "Service UUID : " + service.getUuid().toString());
            return builder.build();
        }

        if (bleCharacteristicUUIDs == null || bleCharacteristicUUIDs.size() == 0) {
            //character no filter
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                if (characteristic != null) {
                    String uuid = characteristic.getUuid().toString();
                    BleCharacteristic.Builder charaBuilder = BleCharacteristic.newBuilder()
                            .setUUID(uuid)
                            .setCharacteristic(characteristic);
                    characteristicList.add(parserGattDescriptor(charaBuilder, characteristic, null));
                }
            }
            builder.setCharacteristics(characteristicList);
            return builder.build();
        }

        // has Characteristic UUID  filter
        for (BleCharacteristicUUID characteristicUUID : bleCharacteristicUUIDs) {
            String uuid = characteristicUUID.getCharacteristicUUID().trim();
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(uuid));
            if (characteristic != null) {
                BleCharacteristic.Builder charaBuilder = BleCharacteristic.newBuilder()
                        .setUUID(uuid)
                        .setCharacteristic(characteristic);
                characteristicList.add(parserGattDescriptor(charaBuilder, characteristic, characteristicUUID.getDescriptorUUIDs()));
            } else {
                LogUtil.e("BluetoothGattCharacteristic is't found UUID : " + uuid);
                onBleDataStatus(BleBluetoothGattStatus.NotFoundCharacteristic, uuid);
            }
        }
        builder.setCharacteristics(characteristicList);
        return builder.build();
    }

    /**
     * filter gatt descriptor
     *
     * @param charaBuilder    characteristic builder
     * @param characteristic  characteristic
     * @param descriptorUUIDs filter uuids
     */
    private synchronized BleCharacteristic parserGattDescriptor(BleCharacteristic.Builder charaBuilder, BluetoothGattCharacteristic characteristic, List<String> descriptorUUIDs) {
        List<BluetoothGattDescriptor> bluetoothGattDescriptors = characteristic.getDescriptors();
        List<BleDescriptor> bleDescriptors = new ArrayList<>();

        if (bluetoothGattDescriptors == null || bluetoothGattDescriptors.size() == 0) {
            onBleDataStatus(BleBluetoothGattStatus.NotAllDescriptor, "Characteristic UUIID : " + characteristic.getUuid().toString());
            //no descriptor
            return charaBuilder.build();
        }

        if (descriptorUUIDs == null || descriptorUUIDs.size() == 0) {
            //no filter return all
            for (BluetoothGattDescriptor descriptor : bluetoothGattDescriptors) {
                if (descriptor != null) {
                    String uuid = descriptor.getUuid().toString();
                    BleDescriptor bleDescriptor = new BleDescriptor(uuid, descriptor);
                    bleDescriptors.add(bleDescriptor);
                }
            }
            charaBuilder.setDescriptors(bleDescriptors);
            return charaBuilder.build();
        }

        // has filter
        for (String descUUID : descriptorUUIDs) {
            descUUID = descUUID.trim();
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(descUUID));
            if (descriptor != null) {
                BleDescriptor bleDescriptor = new BleDescriptor(descUUID, descriptor);
                bleDescriptors.add(bleDescriptor);
            } else {
                LogUtil.e("BluetoothGattDescriptor is't found UUID : " + descUUID);
                onBleDataStatus(BleBluetoothGattStatus.NotFoundDescriptor, descUUID);
            }
        }
        charaBuilder.setDescriptors(bleDescriptors);
        return charaBuilder.build();
    }

    /**
     * filter service status
     *
     * @param status status
     * @param msg    msg
     */
    protected abstract void onBleDataStatus(BleBluetoothGattStatus status, String msg);


    protected abstract List<BleServiceUUID> getBleFilterUUID();


}
