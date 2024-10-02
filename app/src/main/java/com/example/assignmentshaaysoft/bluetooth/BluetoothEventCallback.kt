package com.example.assignmentshaaysoft.bluetooth

import android.bluetooth.BluetoothDevice


interface BluetoothEventCallback {
    fun onScanning(bluetoothDevice: BluetoothDevice)
    fun onScanStarted()
    fun onScanFinished()
    fun onStartConnect(mac: String)
    fun onConnectSuccess(bleDevice: BluetoothDevice?)
    fun onConnectDeviceSuccess(bleDevice: BluetoothDevice?)
    fun onPasswordIncorrect()
    fun onConnectFail(bleDevice: BluetoothDevice?) {}
    fun onDisconnected(isActiveDisConnected: Boolean, device: BluetoothDevice?) {}

    fun onNotify(bytes: ByteArray) {}

    fun onScanFailed(errorCode: Int)

    fun insertDataIntoDB(timestampMillis : Long, sanction: Int)

    fun showDeviceAssociationDialog(device: BluetoothDevice?)

}