package com.example.assignmentshaaysoft

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository(private val dogDao: DogDao) {

    // Get all dogs from the database
    fun getDog(): LiveData<List<Dog>> {
        return dogDao.getAllDogs()
    }

    // Insert a new Dog into the database
    suspend fun insertDog(dog: Dog) {
        withContext(Dispatchers.IO) {
            dogDao.insertDog(dog)
        }
    }

    suspend fun updateDogName(idn: Int, name: String) {
        dogDao.updateDogName(idn, name)
    }


    // Delete all dogs in the database
    suspend fun deleteAllDogsInDataBase() {
        withContext(Dispatchers.IO) {
            dogDao.deleteAllDogs()
        }
    }

    // Delete a specific Dog by ID
    suspend fun deleteDogById(idn: Int) {
        withContext(Dispatchers.IO) {
            dogDao.deleteDogById(idn)
        }
    }

    // Update an existing Dog in the database
    suspend fun updateDog(dog: Dog) {
        withContext(Dispatchers.IO) {
            dogDao.updateDog(dog)
        }
    }
}
