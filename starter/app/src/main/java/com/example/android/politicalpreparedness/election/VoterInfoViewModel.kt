package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.repository.VoterInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class VoterInfoViewModel(application: Application): ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val voterInfoRepository = VoterInfoRepository(database)
    private val electionsRepository = ElectionsRepository(database)

    private var _electionId : Int = 0
    private var _division : Division? = null

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing : MutableLiveData<Boolean>
        get() = _isFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    private val _showError = MutableLiveData<Boolean>()
    val showError: MutableLiveData<Boolean>
        get() = _showError

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: MutableLiveData<Boolean>
        get() = _isDataAvailable

    val voterInfo = voterInfoRepository.voterInfo

    private val _navigationUrl = MutableLiveData<String>()
    val navigationUrl: MutableLiveData<String>
        get() = _navigationUrl

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    fun loadData(electionId: Int, division: Division) {
        _electionId = electionId
        _division = division

        viewModelScope.launch {
            try {
                _isDataAvailable.value = false
                val state = division.state.ifEmpty { "ny" }
                val address = "${state},${division.country}"

                updateFollowState()
                _isLoading.postValue(true)
                voterInfoRepository.refreshVoterInfo(electionId, address)
                _showError.value = false
                _isDataAvailable.value = voterInfo.value != null
            } catch (e: Exception) {
                _showError.value = true
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun followElection() {
        CoroutineScope(Dispatchers.IO).launch {
            electionsRepository.followElection(_electionId)
            updateFollowState()
        }
    }

    private fun updateFollowState() {
        CoroutineScope(Dispatchers.IO).launch {
            _isFollowing.postValue(electionsRepository.isFollowingElection(_electionId))
        }
    }

    fun navigateToVote() { _navigationUrl.postValue(voterInfo.value?.votingUrl) }

    fun navigateToBallots() { _navigationUrl.postValue(voterInfo.value?.ballotUrl) }

    fun setNavigationHandled() { _navigationUrl.value = null }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
                return VoterInfoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct VoterInfoViewModel")
        }
    }
}