package com.example.apptunixsample

import com.example.apptunixsample.model.Orders

class OrderRepository(private val yourDao: OrdersDao) {
    suspend fun getAllData(): List<Orders> {
        return yourDao.getOrders()
    }

    suspend fun insertData(entity: Orders) {
        yourDao.insertOrder(entity)
    }
}


