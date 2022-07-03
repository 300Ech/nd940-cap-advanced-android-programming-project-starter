package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.VoterInfoObject
import com.example.android.politicalpreparedness.network.models.toVoterInfoObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepository {
    private val _voterInfo = MutableLiveData<VoterInfoObject>()
    val voterInfo: LiveData<VoterInfoObject>
        get() = _voterInfo

    suspend fun refreshVoterInfo(electionId: Int, address: String) {
        withContext(Dispatchers.IO) {
            val jsonResponse = CivicsApi.retrofitService.getVoterInfo(address, electionId)
            _voterInfo.postValue(jsonResponse.toVoterInfoObject())
        }
    }
}