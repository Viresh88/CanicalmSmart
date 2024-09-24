package com.example.assignmentshaaysoft.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.assignmentshaaysoft.Dog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Calendar
import java.util.Date
import java.util.LinkedList
import java.util.Queue
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@SuppressLint("MissingPermission", "StaticFieldLeak")
object BluetoothManagerClass {

    // Command Queue
    private val commandQueue: Queue<Runnable> = LinkedList()
    private var commandQueueBusy: Boolean = false

    var animalAssociationDuration: Long = 30000
    private var aboiementRunning = false
    private val serviceUUID = UUID.fromString("4E756D27-4178-6573-0000-000000000000")
    private val characteristicUUIDNotify = UUID.fromString("4E756D27-4178-6573-0001-000000000004")
    private val serviceUuid = UUID.fromString("4E756D27-4178-6573-0000-000000000000")
    private val charUuidTime = UUID.fromString("00002A2B-0000-1000-8000-00805F9B34FB")
    private val charUuidBattery = UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB")
    private val charUuidMode = UUID.fromString("4E756D27-4178-6573-0001-000000000000")
    private val charUuidSensibility = UUID.fromString("4E756D27-4178-6573-0001-000000000001")
    private val charUuidProg1 = UUID.fromString("4E756D27-4178-6573-0001-000000000002")
    private val charUuidProg2 = UUID.fromString("4E756D27-4178-6573-0001-000000000003")
    private val charUuidAboiement = UUID.fromString("4E756D27-4178-6573-0001-000000000005") // Replace with the correct UUID


    var scanning: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var context: Context
    private var bleDevice: BluetoothDevice? = null
    private val deviceGattMap = ConcurrentHashMap<BluetoothDevice, BluetoothGatt>()
    private var characteristicNotify: BluetoothGattCharacteristic? = null
    private var characteristicWrite: BluetoothGattCharacteristic? = null
    val dogs = mutableListOf<Dog>()

    private var listener: BluetoothEventCallback? = null

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    fun setBluetoothManagerListener(listener: BluetoothEventCallback) {
        this.listener = listener
    }

    fun initializeBluetooth(context: Context): Boolean {
        this.context = context
        return true
    }

    // Add command to the queue and process if not busy
    private fun enqueueCommand(command: Runnable) {
        commandQueue.offer(command)
        if (!commandQueueBusy) {
            processNextCommand()
        }
    }

    // Process the next command from the queue
    private fun processNextCommand() {
        if (commandQueue.isNotEmpty()) {
            commandQueueBusy = true
            val command = commandQueue.poll()
            command?.run()
        } else {
            commandQueueBusy = false
        }
    }

    // Call this after each command finishes
    private fun commandCompleted() {
        commandQueueBusy = false
        processNextCommand()
    }

    // Example of enqueuing a descriptor write command
    private fun startNotifications(gatt: BluetoothGatt) {
        val service = gatt.getService(serviceUuid)
        service?.characteristics?.forEach { characteristic ->
            if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                enqueueCommand(Runnable {
                    gatt.setCharacteristicNotification(characteristic, true)
                    val descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                })
            }
        }
    }


    fun disconnect() {
        val gatt = bleDevice?.let { deviceGattMap[it] }

        if (gatt != null) {
            gatt.disconnect()  // Request to disconnect from the remote device
            gatt.close()  // Close the GATT connection and release resources
            deviceGattMap.remove(bleDevice)  // Remove the device from the map
            bleDevice = null  // Clear the reference to the BLE device
        }
    }


    fun connect(mac: String) {
        disconnect()  // Disconnect any existing connection before establishing a new one
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac)  // Get the BLE device by MAC address
        bleDevice = bluetoothDevice

        val gattCallback = createGattCallback()  // Create the GATT callback

        val gatt: BluetoothGatt? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android 6.0 (Marshmallow) and above, use the transport LE flag
            bluetoothDevice?.connectGatt(context, true, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } else {
            // For older versions, just use the connectGatt method without the transport LE flag
            bluetoothDevice?.connectGatt(context, true, gattCallback)
        }

        if (gatt != null) {
            // Store the BluetoothGatt object in the map so you can access it later
            deviceGattMap[bluetoothDevice] = gatt
        }
    }


    private fun createGattCallback(): BluetoothGattCallback {
        return object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                status: Int,
                newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt?.discoverServices()
                    listener?.onConnectSuccess(gatt?.device)
                    Toast.makeText(context, "STATE_CONNECTED", Toast.LENGTH_LONG).show()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Toast.makeText(context, "STATE_DISCONNECTED", Toast.LENGTH_LONG).show()
                    disconnect()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    gatt?.let { startNotifications(it) }
                } else {
                    disconnect()
                }
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt?,
                descriptor: BluetoothGattDescriptor?,
                status: Int
            ) {
                super.onDescriptorWrite(gatt, descriptor, status)
                commandCompleted()  // Mark command as completed and proceed to the next one
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int
            ) {
                when (characteristic.uuid) {
                    charUuidBattery -> handleBattery(characteristic)
                    charUuidMode -> handleMode(characteristic)
                    charUuidSensibility -> handleSensibility(characteristic)
                    charUuidProg1 -> handleProgram1(characteristic)
                    charUuidProg2 -> handleProgram2(characteristic)
                    charUuidAboiement -> handleAboiementNotification(characteristic)

                }

            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                when (characteristic?.uuid) {
                    charUuidTime -> characteristic?.let { handleTime(it) }
                    charUuidBattery -> characteristic?.let { handleBattery(it) }
                    charUuidMode -> characteristic?.let { handleMode(it) }
                    charUuidSensibility -> characteristic?.let { handleSensibility(it) }
                    charUuidProg1 -> characteristic?.let { handleProgram1(it) }
                    charUuidProg2 -> characteristic?.let { handleProgram2(it) }
                    charUuidAboiement -> characteristic?.let { handleAboiementNotification(it) }
                    
                }
            }
        }
    }




    private fun handleAboiementNotification(characteristic: BluetoothGattCharacteristic) {
        val data = characteristic.value
        println("Aboiement notification received: ${data.contentToString()}")

        // Trigger the logic for barking notifications
        dataStartNotifAboiement(Calendar.getInstance())

    }

    fun dataStartNotifAboiement(date: Calendar?) {
        //updateConnectionStatusIcon()

        if (aboiementRunning) return

        aboiementRunning = true
        println("notif aboiement ")

        val currentDate = date ?: Date(Date.UTC(2018, 0, 1, 0, 0, 0))
        val timestamp = createFAT32Timestamp(currentDate)

        GlobalScope.launch {
            delay(10000)
            writeDate(timestamp)
        }
    }

    private fun writeDate(timestamp: ByteArray) {
        val bluetoothGatt = bleDevice?.let { deviceGattMap[it] }
        val service: BluetoothGattService? = bluetoothGatt!!.getService(serviceUUID)
        val characteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(
            charUuidAboiement)
        characteristic?.value = timestamp
        val success = bluetoothGatt.writeCharacteristic(characteristic)

        if (success) {
            println("CYRIL write 00005 ok : ${timestamp.joinToString(" ")}")
            startNotification()
        } else {
            println("CYRIL write 00005 failed")
            aboiementRunning = false
            //dataStartNotifAboiement(null)
        }
    }

    private fun startNotification() {
        val bluetoothGatt = bleDevice?.let { deviceGattMap[it] }
        GlobalScope.launch {
            delay(10000)
            val characteristic: BluetoothGattCharacteristic? = bluetoothGatt!!.getService(serviceUUID)?.getCharacteristic(characteristicUUIDNotify)
            if (bluetoothGatt!!.setCharacteristicNotification(characteristic, true)) {
                // Handle success
            } else {
                println("startnotif aboie failed")
                aboiementRunning = false
                dataStartNotifAboiement(null)
            }
        }
    }

    private fun createFAT32Timestamp(date: Serializable): ByteArray {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = date as Date
        }
        val sec = calendar.get(Calendar.SECOND) ushr 1
        val min = calendar.get(Calendar.MINUTE)
        val hr = calendar.get(Calendar.HOUR_OF_DAY)
        val d = calendar.get(Calendar.DATE)
        val m = calendar.get(Calendar.MONTH) + 1
        val y = calendar.get(Calendar.YEAR) - 1980

        val timestamp = (sec or (min shl 5) or (hr shl 11) or (d shl 16) or (m shl 21) or (y shl 25)).toLong()
        return byteArrayOf(
            (timestamp shr 24).toByte(),
            (timestamp shr 16).toByte(),
            (timestamp shr 8).toByte(),
            timestamp.toByte()
        )
    }


    // Example of handling a time characteristic read operation
    private fun handleTime(characteristic: BluetoothGattCharacteristic) {
        val data = characteristic.value
        if (data.size < 10) {
            println("Data size is too small")
            return
        }

        val bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)
        val year = bb.short.toInt() and 0xffff
        val month = bb.get() - 1
        val day = bb.get()
        val hour = bb.get()
        val minute = bb.get()
        val second = bb.get()

        val deviceDate = Calendar.getInstance().apply {
            set(year, month, day.toInt(), hour.toInt(), minute.toInt(), second.toInt())
            set(Calendar.MILLISECOND, 0)
        }

        val currentDate = Calendar.getInstance()
        println("Device date: ${deviceDate.time}, Current date: ${currentDate.time}")

        if (Math.abs(currentDate.timeInMillis - deviceDate.timeInMillis) > 15000) {
            println("Time drift detected. Updating device time.")
            writeTimeToDevice(currentDate)  // Update device time
        } else {
            println("No significant time drift detected.")
        }
    }

    private fun writeTimeToDevice(currentDate: Calendar) {
        val bb = ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN)
        bb.putShort(currentDate.get(Calendar.YEAR).toShort())
        bb.put((currentDate.get(Calendar.MONTH) + 1).toByte())
        bb.put(currentDate.get(Calendar.DAY_OF_MONTH).toByte())
        bb.put(currentDate.get(Calendar.HOUR_OF_DAY).toByte())
        bb.put(currentDate.get(Calendar.MINUTE).toByte())
        bb.put(currentDate.get(Calendar.SECOND).toByte())
        bb.put(currentDate.get(Calendar.DAY_OF_WEEK).toByte())
        bb.put(0.toByte())
        bb.put(0.toByte())

        enqueueCommand(Runnable {
            writeCharacteristicData(serviceUuid, charUuidTime, bb.array())
        })
    }





                // Example of handling a time characteristic read operation

    private fun writeCharacteristicData(serviceUuid: UUID, charUuid: UUID, data: ByteArray) {
        val gatt = bleDevice?.let { deviceGattMap[it] }
        val gattService = gatt?.getService(serviceUuid)
        val characteristic = gattService?.getCharacteristic(charUuid)
        characteristic?.value = data
        gatt?.writeCharacteristic(characteristic)
        println("Writing data to characteristic")
    }





    private fun handleBattery(characteristic: BluetoothGattCharacteristic) {
        enqueueCommand(Runnable {
            // Get the battery level from the characteristic (FORMAT_UINT8 means an 8-bit integer)
            val batteryLevel = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)

            // Log or display the battery level
            Log.d("BLE", "Battery level: $batteryLevel%")

            // Notify UI or listeners about the battery level
            //listener?.onBatteryLevelUpdated(batteryLevel)

            // Signal that the command is complete
            commandCompleted()
        })

    }

    private fun handleMode(characteristic: BluetoothGattCharacteristic) {
        enqueueCommand(Runnable {
            // Get the mode value from the characteristic (assuming it's an 8-bit integer)
            val modeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)

            // Log or display the mode
            Log.d("BLE", "Device mode: $modeValue")

            // Notify UI or listeners about the mode change
           // listener?.onModeUpdated(modeValue)

            // Handle mode-specific logic
            when (modeValue) {
                0 -> {
                    Log.d("BLE", "Device is in normal mode.")
                }
                1 -> {
                    Log.d("BLE", "Device is in training mode.")
                }
                2 -> {
                    Log.d("BLE", "Device is in sleep mode.")
                }
                else -> {
                    Log.d("BLE", "Unknown device mode: $modeValue")
                }
            }

            // Signal that the command is complete
            commandCompleted()
        })
    }

    private fun handleSensibility(characteristic: BluetoothGattCharacteristic) {

            enqueueCommand(Runnable {
                val detectionLevel = characteristic.value

                // Log the detection level
                Log.d("BLE", "Detection level: ${detectionLevel[0].toInt()}")

                // Example: Notify UI or update listener with the detection level
                //listener?.onSensibilityUpdated(detectionLevel[0].toInt())

                // Signal that the command is complete
                commandCompleted()
            })
        }



    private fun handleProgram1(characteristic: BluetoothGattCharacteristic) {
        enqueueCommand(Runnable {
            val data = characteristic.value
            if (data.size < 16) {
                Log.e("BLE", "Program1 data size is too small")
                commandCompleted()
                return@Runnable
            }

            // Extract the schedule for Monday, Tuesday, Wednesday, and Thursday
            val mondayData = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
            val tuesdayData = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
            val wednesdayData = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).int
            val thursdayData = ByteBuffer.wrap(data, 12, 4).order(ByteOrder.LITTLE_ENDIAN).int

            // Update the UI or data model with the schedule for each day
            updateDogSchedule(mondayData, "monday")
            updateDogSchedule(tuesdayData, "tuesday")
            updateDogSchedule(wednesdayData, "wednesday")
            updateDogSchedule(thursdayData, "thursday")

            // Signal that the command is complete
            commandCompleted()
        })
    }

    private fun updateDogSchedule(dayData: Int, dayName: String) {
        val schedule = BooleanArray(24)  // Assuming 24 hours in a day
        for (i in 0 until 24) {
            schedule[i] = dayData and (1 shl i) != 0
        }

        // Log the schedule or update UI
        Log.d("BLE", "$dayName schedule: ${schedule.joinToString()}")
    }


    fun associationSearchDeviceCanicalm() {
        // Set scanning flag to true
        scanning = true

        // Start BLE scanning using a timed scan function
        enqueueCommand(Runnable {
            associationTimingScan()  // Start the scanning process
            commandCompleted()  // Mark the scan command as completed
        })
    }


    private fun associationTimingScan() {
        if (scanning) {
            // Stop any previous BLE scans
            bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)

            // Start a new BLE scan with a small delay (for example, 100 ms)
            handler.postDelayed({
                bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallback)

                // Stop the scan after 2 seconds and restart if needed
                handler.postDelayed({
                    associationTimingScan()  // Restart scanning if still scanning
                }, 2000)  // Adjust the time as per your requirement (in milliseconds)
            }, 100)
        }
    }

    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            Log.d("BLE", "Device found: ${device.name} (${device.address})")

            if (deviceIsCanicalm(device)) {
                Log.d("BLE", "CANICALM-SMART device found: ${device.name}")

                // Stop scanning
                scanning = false
                bluetoothAdapter.bluetoothLeScanner?.stopScan(this)

                // Notify listener or take an action (e.g., show a device association dialog)
                listener?.showDeviceAssociationDialog(device)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BLE", "BLE scan failed with error: $errorCode")
            associationStartBLEScanFailed()
        }
    }

    fun associationStartBLEScanFailed() {
        bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)

        // Retry scanning after 5 seconds (5000 ms)
        handler.postDelayed({
            associationTimingScan()
        }, 5000)
    }


    fun deviceIsCanicalm(device: BluetoothDevice): Boolean {
        return device.name == "CANICALM-SMART"
    }







    private fun handleProgram2(characteristic: BluetoothGattCharacteristic) {
        enqueueCommand(Runnable {
            val data = characteristic.value
            if (data.size < 12) {
                Log.e("BLE", "Program2 data size is too small")
                commandCompleted()
                return@Runnable
            }

            // Extract the schedule for Friday, Saturday, and Sunday
            val fridayData = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
            val saturdayData = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
            val sundayData = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).int

            // Update the UI or data model with the schedule for each day
            updateDogSchedule(fridayData, "friday")
            updateDogSchedule(saturdayData, "saturday")
            updateDogSchedule(sundayData, "sunday")

            // Signal that the command is complete
            commandCompleted()
        })
    }



}





