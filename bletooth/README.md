#BleTooth - 中心设备开发 （central）

####扫描（scan）
 Android上蓝牙扫描有两种方式：
 * 在Android 4.3 ~ 5.0 以下，使用`BluetoothAdapter.startLeScan()#stopLeScan()`实现
 * 在Android 5.0 以上，使用`BluetoothLeScaner.startScan()#stopScan()` 实现

 在这里进行了兼容，判断系统类型，扫描使用不同方式实现，统一回调处理BleScanCallback;

 * BleScanCallBack 回调: 扫描进度（超时时间百分比）、状态、扫描结果、rssi 强度 、添加设备过滤（设备名称，mac）；

####连接（conn）
 Android 蓝牙连接使用的是统一操作：`BlueToothDevice.connectGatt()` 实现；
 * BleGattCallBack 回调：连接状态，操作相关；
 * 实现通过MAC地址连接

 注意： 本库仅仅实现BLE设备单连接，只有一个`BleGatttCallback` ；

####操作（operation）
 Android ble 中心设备上对周边设备的操作，通过`BluetoothGatt` 进行操作，本库实现包括：
 * write Characteristic
 * write Descriptor
 * read Characteristic
 * read Descriptor
 * read RemoteRSSI
 * notification Characteristic
 * notification Descriptor

####使用（use）
 本库提供了三种方式：
 1. BleTooth 类 进行开发

     上面描述的扫描、连接、操作 三个均在BleTooth 进行总控制实现，通过 `BleTooth.getInstance(getApplicationContext())` 获取BleTooth对象，
   这里注意使用Application 的 Context!

    **Example :** simple 工程 BleToothActivity 类实现

 2. Android Application + Service + BleTooth 开发

    通过后台服务来初始化蓝牙相关服务，通过继承 BaseBleToothService 实现；并在Application中初始化服务；
    获取到Service后获取BleTooth进行操作；
    * BaseBleToothService : 初始化BleTooth
    * Application  : 初始化Service

    **Example :** 本库 core/simple 包下
    *  SimpleBleToothService实现
    *  SimpleServiceBleApplication 实现 （可以直接继承使用）

 3. Android Application + BleTooth 开发

    直接在Application进行BleTooth 配置初始化方法，BleGattCallBack 可以单独写成类，并通过广播就行数据发送，
    继承 BaseBleToothApplication即可。
    * BaseBleToothApplication 初始化 Ble , 简单操作方法 ；

    **Example :** 本库 core/simple 包下 or SimpleCentral 工程实现
    * SimpleBleToothApplicaion

####注意事项
 1. 本库只可以Ble单设备连接
 2. 使用Service 和 Application 的时候，别忘记在AndroidManifests.xml 中配置;
 3. 扫描添加过滤的时候，设备名称全称;
 4. Gatt中 Service Characteristic Descriptor 过滤配置UUID，记得`startBuilderService()#stopBuilderServce()` , 见
 core/conn 下 SimpleBleToothBleFilterGattCallBack 类实现;
 5. 还是上条过滤，addCharacteristic(characterUUID,DescriptorUUID....) 第一个是Characteristic UUID ，后面全部是DescriptorUUID;




