package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.SavedElection

@Dao
interface SavedElectionDao {

    // Add insert query
    @Insert
    fun insertSaved(savedElection: SavedElection)

    @Insert
    fun insertSavedAll(savedElections: List<SavedElection>)

    // Add select all election query
    @Query("SELECT * FROM saved_election_table")
    fun getAllSavedElections(): LiveData<List<SavedElection>>

    // Add select single election query
    @Query("SELECT * FROM saved_election_table WHERE id = :id")
    fun getSavedElectionById(id: Int): LiveData<SavedElection>

    @Query("SELECT * FROM saved_election_table WHERE id = :id")
    suspend fun getSavedElectionByIdSync(id: Int): SavedElection?

    // Add delete query
    @Query("DELETE FROM saved_election_table WHERE id = :id")
    fun deleteSavedElectionById(id: Int)

    // Add clear query
    @Query("DELETE FROM saved_election_table")
    fun deleteAllSavedElections()
}