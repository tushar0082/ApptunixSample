package com.example.apptunixsample.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apptunixsample.OrderRepository
import com.example.apptunixsample.model.Orders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivityViewModel(private val orderRepository: OrderRepository) : ViewModel() {


    fun insertData(entity: Orders) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.insertData(entity)
        }
    }

    suspend fun getData(): List<Orders> {
        return orderRepository.getAllData()
    }

}


class DashBoardActivityViewModelFactory(private val orderRepository: OrderRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardActivityViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

