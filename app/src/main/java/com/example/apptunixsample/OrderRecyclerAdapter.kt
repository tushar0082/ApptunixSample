package com.example.apptunixsample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptunixsample.model.Orders
import com.example.apptunixsample.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderRecyclerAdapter(private var orderList:MutableList<Orders>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

        fun setOrders(orderList: MutableList<Orders>){
            this.orderList=orderList
            notifyDataSetChanged()
        }


    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Orders) {
            binding.orderId.text = "Order Id: ${order.orderId}"
            binding.orderQuantity.text = "Quantity: ${order.quantity}"
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            val date = Date(order.date)
            val formattedDate = dateFormat.format(date)
            binding.orderDate.text = "Date: $formattedDate"
            binding.orderTime.text = "Time: ${order.time}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size ?: 0
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        orderList[position].let { holder.bind(it) }
    }
}
