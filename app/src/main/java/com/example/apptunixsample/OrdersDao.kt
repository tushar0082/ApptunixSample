package com.example.apptunixsample

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apptunixsample.model.Orders

@Dao
interface OrdersDao {

    @Insert
    suspend fun insertOrder(orders: Orders)
    @Delete
    suspend fun delete(orders: Orders)
    @Update
    suspend fun update(orders: Orders)
    @Query("select * from Orders")
    fun getOrders():List<Orders>
}