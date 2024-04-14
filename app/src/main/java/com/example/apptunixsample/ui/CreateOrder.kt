package com.example.apptunixsample.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.apptunixsample.OrderDatabase
import com.example.apptunixsample.model.Orders
import com.example.apptunixsample.R
import com.example.apptunixsample.databinding.ActivityOrderBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateOrder : AppCompatActivity() {
    private lateinit var binding:ActivityOrderBinding

    private lateinit var database: OrderDatabase

    private var date:Long=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database=Room.databaseBuilder(applicationContext,
            OrderDatabase::class.java,
            "Orders Database").build()


        initializeSpinner(binding.spinner,arrayOf("Morning", "Afternoon", "Evening"))
        initializeSpinner(binding.spinner3,arrayOf("Low", "Medium", "High"))

        date= Calendar.getInstance().apply {
            // Set time components to zero (midnight)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        binding.calendarView.minDate=System.currentTimeMillis()


        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // Set time components to zero (midnight)
            selectedDate.set(Calendar.HOUR_OF_DAY, 0)
            selectedDate.set(Calendar.MINUTE, 0)
            selectedDate.set(Calendar.SECOND, 0)
            selectedDate.set(Calendar.MILLISECOND, 0)

            date = selectedDate.timeInMillis

        }

        binding.backButton.setOnClickListener{
            finish()
        }

        binding.save.setOnClickListener{
            if (binding.quantity.text.isNullOrEmpty())
            {
                binding.quantity.error = "Please enter a valid quantity"
            }else{
                lifecycleScope.launch(Dispatchers.IO) {

                    database.contactDao().insertOrder(
                        Orders(quantity = binding.quantity.text.toString(),
                        time = binding.spinner.selectedItem.toString(),
                        fat = binding.spinner3.selectedItem.toString(),
                        date =date, orderId = 0
                    )
                    )
                }

                Toast.makeText(this@CreateOrder,"Order Successful",Toast.LENGTH_SHORT).show()

            }


        }

    }


    private fun initializeSpinner(spinner: Spinner, values: Array<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}