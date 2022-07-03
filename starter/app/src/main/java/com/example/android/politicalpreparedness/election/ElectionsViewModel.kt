package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    private val _isLoading = MutableLiveData<Boolean>()
    val upcomingElections = electionsRepository.elections
    val savedElections = electionsRepository.savedElections

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                electionsRepository.refreshElections()
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
                return ElectionsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ElectionsViewModel")
        }
    }
}