package com.example.apptunixsample.ui


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.apptunixsample.OrderDatabase
import com.example.apptunixsample.OrderRecyclerAdapter
import com.example.apptunixsample.model.Orders
import com.example.apptunixsample.databinding.ActivityViewOrdersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ViewOrders : AppCompatActivity() {
    private lateinit var binding: ActivityViewOrdersBinding
    private lateinit var orderAdapter: OrderRecyclerAdapter
    private var orderList: MutableList<Orders> = mutableListOf()
    private  var startDate=""
    private var endDate=""
    private lateinit var startDateFilter:Date
    private lateinit var endDateFilter:Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(
            applicationContext,
            OrderDatabase::class.java,
            "Orders Database"
        ).build()

        // Initialize the RecyclerView adapter
        orderAdapter = OrderRecyclerAdapter(orderList)
        binding.orderRecycler.adapter = orderAdapter
        binding.orderRecycler.layoutManager = LinearLayoutManager(this)

        binding.startDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.startDate.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)

                startDate=DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)
                if(orderAdapter.itemCount>0)
                {
                  filterOrders()

                }else
                {
                    Toast.makeText(this,"No Orders",Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.endDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.endDate.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)
                endDate=DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)

                if(orderAdapter.itemCount>0&&startDate.isNullOrEmpty().not())
                {
                    filterOrders()
                }else
                {
                    Toast.makeText(this,"No Orders",Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                orderList = database.contactDao().getOrders().toMutableList()
                withContext(Dispatchers.Main) {
                    orderAdapter.setOrders(orderList)
                }
                orderAdapter.notifyDataSetChanged()
            }
        }

        binding.backButton.setOnClickListener{
            finish()
        }
    }

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val date = format.parse("$dayOfMonth-${(monthOfYear + 1)}-$year")
                if (date != null) {
                    onDateSelected(date)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)


        )

        datePickerDialog.show()
    }

    private fun filterOrders() {

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (startDate.isNullOrEmpty().not())
                {   startDateFilter =
                    SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(startDate)

                }
                if (endDate.isNullOrEmpty().not())
                {
                     endDateFilter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(endDate)
                }




                val filteredOrders = if (endDate.isNullOrEmpty()){
                    orderList.filter { order ->
                        val orderDate = Date(order.date)
                        orderDate >=startDateFilter
                    }

                }else {
                    orderList.filter { order ->
                        val orderDate = Date(order.date)
                        orderDate in startDateFilter..endDateFilter
                    }

                }

                withContext(Dispatchers.Main) {
                    orderAdapter.setOrders(filteredOrders.toMutableList())
                }
            }
        }
    }


    }
