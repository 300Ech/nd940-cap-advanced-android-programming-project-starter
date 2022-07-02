package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
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
}