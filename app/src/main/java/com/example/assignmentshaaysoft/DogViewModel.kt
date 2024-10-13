package com.example.assignmentshaaysoft

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.numaxes.canicomgps.repository.DeviceRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

class DogViewModel(
    private val dogRepository: DogRepository?,
    private val deviceRepository: DeviceRepository?,
    private val executor: Executor?
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var listDog = mutableListOf<Dog>()
    private var updatedDogList = mutableListOf<Dog>()
    private val allDevices = deviceRepository?.getDevices()?.asLiveData()
    private val allDogs = dogRepository?.getDog()

    // Function to get all dogs from the repository
    fun getAllDogs(): LiveData<List<Dog>>? {
        return allDogs
    }

    // Function to delete all dogs from the repository
    fun deleteAllDogs() {
        viewModelScope.launch(Dispatchers.IO) {
            dogRepository?.deleteAllDogsInDataBase()
        }
    }


    // Function to save a Dog with all parameters into the database
    fun saveDog(name: String, batt: Int, id: String, detectionLevel: Int, schedule: Map<String, List<Int>>) {
        val newDog = Dog(
            idn = 1,
            name = name ?: "Unknown",
            batt = batt,
            id = "Unknown",  // Default value for ID
            detectionLevel = detectionLevel,  // Default detection level
            mode = 0,  // Default mode value
            schedule = Schedule( // Initialize schedule with default values
                monday = Array(24) { false },
                tuesday = Array(24) { false },
                wednesday = Array(24) { false },
                thursday = Array(24) { false },
                friday = Array(24) { false },
                saturday = Array(24) { false },
                sunday = Array(24) { false }
            )
        )

        coroutineScope.launch {
            dogRepository?.insertDog(newDog) // Insert dog into the database
        }
    }

    suspend fun updateDogName(idn: Int, name: String) {
        dogRepository!!.updateDogName(idn, name)
    }




    // Function to create a new device in the repository
    fun createNewDevice(device: Device) {
        coroutineScope.launch {
            deviceRepository?.createDevice(device)
        }
    }

    // Function to get all devices from the repository
    fun getAllDevices(): LiveData<List<Device>>? {
        return allDevices
    }

    // Function to get the last connected device from the repository
    suspend fun getLastConnectedDevice(): List<Device>? {
        return withContext(Dispatchers.IO) {
            deviceRepository?.getDevicesWithLastConnectedTrue()
        }
    }

    // Function to get the status of a device by its MAC address
    fun getStatusByMacAddress(macAddress: String, onStatusReceived: (Boolean?) -> Unit) {
        viewModelScope.launch {
            val status = deviceRepository?.getStatusByMacAddress(macAddress)
            onStatusReceived(status)
        }
    }

    // Function to update a device in the repository
    fun updateDevice(device: Device) {
        coroutineScope.launch {
            deviceRepository?.updateDevice(device)
        }
    }





    // Function to update the last connected device by its address
    fun updateLastConnectedDevice(address: String) {
        coroutineScope.launch {
            deviceRepository?.updateLastConnectedDevices(address)
        }
    }
}
