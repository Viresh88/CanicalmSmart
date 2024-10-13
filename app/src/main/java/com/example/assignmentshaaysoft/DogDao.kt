package com.example.assignmentshaaysoft

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DogDao {

    // Insert a new Dog into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDog(dog: Dog)

    // Get all dogs from the database
    @Query("SELECT * FROM dogs")
    fun getAllDogs(): LiveData<List<Dog>>



        @Query("UPDATE dogs SET name = :name WHERE idn = :idn")
        suspend fun updateDogName(idn: Int, name: String)


    // Delete all dogs in the database
    @Query("DELETE FROM dogs")
    suspend fun deleteAllDogs()

    // Delete a specific Dog by its ID
    @Query("DELETE FROM dogs WHERE idn = :idn")
    suspend fun deleteDogById(idn: Int)

    // Update a specific Dog
    @Update
    suspend fun updateDog(dog: Dog)
}
