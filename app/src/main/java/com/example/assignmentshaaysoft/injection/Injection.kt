package com.numaxes.canicomgps.injection

import android.content.Context
import com.example.assignmentshaaysoft.DogRepository
import com.example.assignmentshaaysoft.SaveDatabase

import com.numaxes.canicomgps.repository.DeviceRepository


import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class Injection {
    companion object {
        private fun provideDogData(context: Context?): DogRepository? {
            val database: SaveDatabase? = context?.let {
                SaveDatabase.getInstance(it)
            }

            return database?.dogDao()?.let { DogRepository(it) }
        }

        private fun provideDeviceData(context: Context?): DeviceRepository {
            val database: SaveDatabase? = context?.let {
                SaveDatabase.getInstance(it)
            }
            return DeviceRepository(database?.deviceDao())
        }








        private fun provideExecutor(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory(context: Context?): ViewModelFactory {
            val dogRepository : DogRepository = provideDogData(context)!!
            val bluetoothDeviceRepository: DeviceRepository = provideDeviceData(context)


            val executor = provideExecutor()
            return ViewModelFactory(dogRepository,bluetoothDeviceRepository, executor)
        }
    }
}