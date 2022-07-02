package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.network.models.toSavedElectionObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {
    val elections: LiveData<List<Election>> = database.electionDao.getAllElections()
    val savedElections: LiveData<List<SavedElection>> = database.savedElectionDao.getAllSavedElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val jsonResponse = CivicsApi.retrofitService.getElections()
            database.electionDao.insertAll(jsonResponse.elections)
        }
    }

    suspend fun followElection(electionId: Int) {
        // if the election exists in the database, remove it
        val savedElection = database.savedElectionDao.getSavedElectionByIdSync(electionId)
        savedElection?.let {
            database.savedElectionDao.deleteSavedElectionById(electionId)
        } ?: run {
            // if the election does not exist in the database, add it
            database.electionDao.getElectionByIdSync(electionId)?.let {
                database.savedElectionDao.insertSaved(it.toSavedElectionObject())
            }
        }
    }

    suspend fun isFollowingElection(electionId: Int): Boolean {
        val savedElection = database.savedElectionDao.getSavedElectionByIdSync(electionId)
        return savedElection != null
    }
}