package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert
    fun insert(election: Election)

    @Insert
    fun insertAll(elections: List<Election>)

    // Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElectionById(id: Int): LiveData<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionByIdSync(id: Int): Election?

    // Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    fun deleteElectionById(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    fun deleteAllElections()
}