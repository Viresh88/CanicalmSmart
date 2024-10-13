package com.numaxes.canicomgps.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentshaaysoft.DogRepository
import com.example.assignmentshaaysoft.DogViewModel
import com.numaxes.canicomgps.repository.DeviceRepository

import java.util.concurrent.Executor

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private var dogRepository: DogRepository,
                       private var deviceRepository: DeviceRepository,
                       private var executor: Executor?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogViewModel::class.java)) {
            return DogViewModel(
                dogRepository,
                deviceRepository,
                executor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}