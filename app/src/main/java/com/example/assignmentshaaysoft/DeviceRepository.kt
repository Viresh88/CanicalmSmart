package com.numaxes.canicomgps.repository
import com.example.assignmentshaaysoft.Device
import com.numaxes.canicomgps.database.dao.DeviceDao
import kotlinx.coroutines.flow.Flow

class DeviceRepository(private var deviceDao: DeviceDao?) {

    private val allDevices : Flow<List<Device>>? = deviceDao?.allDevices

    suspend fun createDevice(device: Device) {
        deviceDao?.insertDevice(device)
    }

    fun getDevices(): Flow<List<Device>>? {
        return allDevices
    }

    suspend fun updateDevice(device: Device) {
        deviceDao?.updateDevice(device)
    }

    suspend fun getDevicesWithLastConnectedTrue(): List<Device>? {
        return deviceDao?.getDevicesWithLastConnectedTrue()
    }

    suspend fun updateLastConnectedDevices(connectedAddress: String){
        deviceDao?.updateLastConnectedDevice(connectedAddress)
    }

    suspend fun renameLastConnectedDevices(newDeviceName: String){
        deviceDao?.renameLastConnectedDevices(newDeviceName)
    }


    suspend fun getStatusByMacAddress(macAddress: String): Boolean? {
        return deviceDao?.getStatusByMacAddress(macAddress)
    }



}