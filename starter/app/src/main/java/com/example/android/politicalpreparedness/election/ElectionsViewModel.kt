package com.example.android.politicalpreparedness.election

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    //TODO: Create live data val for upcoming elections
    private val _isLoading = MutableLiveData<Boolean>()
    val upcomingElections = electionsRepository.elections
    val savedElections = electionsRepository.savedElections

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    //TODO: Create live data val for saved elections


    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

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

    val progressBarVisible = Transformations.map(_isLoading) {
        if (it == true) View.VISIBLE else View.GONE
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