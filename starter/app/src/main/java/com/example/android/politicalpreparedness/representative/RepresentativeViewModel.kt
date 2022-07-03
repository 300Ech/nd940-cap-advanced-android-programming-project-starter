package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativesRepository
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application): ViewModel() {

    private val representativesRepository = RepresentativesRepository()

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    val selectedStateIndex = MutableLiveData<Int>()

    private val _selectedState = MutableLiveData<String>()
    val selectedState: LiveData<String>
        get() = _selectedState

    val representatives = representativesRepository.representatives

    init {
        _address.value = Address("", "","","New York","")
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                address.value.let {
                    if (it != null) {
                        it.state = selectedState.value!!
                    }
                }

                representativesRepository.refreshRepresentatives(address.value?.toFormattedString() ?: "")
            } catch (e: Exception) {
                //TODO: handle error
            }
        }
    }

    fun selectState(selectedItem: String) {
        _selectedState.value = selectedItem
    }


    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
                return RepresentativeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct RepresentativeViewModel")
        }
    }
}
