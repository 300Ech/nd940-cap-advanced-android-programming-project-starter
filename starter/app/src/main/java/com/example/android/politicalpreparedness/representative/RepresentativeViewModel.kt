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

    private val _selectedState = MutableLiveData<String>()
    val selectedState: LiveData<String>
        get() = _selectedState

    val representatives = representativesRepository.representatives

    init {
        _address.value = Address("", "","","Alabama","")
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

    fun loadData(address: Address) {
        _selectedState.value = address.state
        _address.value = address
        loadData()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
                return RepresentativeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct RepresentativeViewModel")
        }
    }
}
