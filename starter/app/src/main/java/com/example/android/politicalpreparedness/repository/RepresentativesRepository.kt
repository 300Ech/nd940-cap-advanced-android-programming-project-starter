package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.VoterInfoObject
import com.example.android.politicalpreparedness.network.models.toVoterInfoObject
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativesRepository {
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    suspend fun refreshRepresentatives(address: String) {
        withContext(Dispatchers.IO) {
            val jsonResponse = CivicsApi.retrofitService.getRepresentatives(address)
            val representativesItems =
                jsonResponse.offices.flatMap { it.getRepresentatives(jsonResponse.officials) }
            _representatives.postValue(representativesItems)
        }
    }
}