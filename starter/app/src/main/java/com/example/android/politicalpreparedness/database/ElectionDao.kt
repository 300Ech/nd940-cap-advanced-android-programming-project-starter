package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert
    suspend fun insert(election: Election)

    // Add select all election query
    @Query("SELECT * FROM election_table")
    suspend fun getAllElections(): List<Election>

    // Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election

    // Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElectionById(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()
}