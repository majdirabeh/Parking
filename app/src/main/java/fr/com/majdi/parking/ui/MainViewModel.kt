package fr.com.majdi.parking.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.com.majdi.parking.model.ResponseParking
import fr.com.majdi.parking.repository.ParkingRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
class MainViewModel(private val repository: ParkingRepository) : ViewModel() {

    private val _parkingList = MutableLiveData<ResponseParking>()
    val listParking = _parkingList

    init {
        fetchParking()
    }

    private fun fetchParking() {
        viewModelScope.launch {
            repository.getListParking().collect {
                listParking.value = it
            }
        }
    }

}