package com.example.apptunixsample.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apptunixsample.databinding.ActivityMainBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createOrder.setOnClickListener{
            startActivity(Intent(this, CreateOrder::class.java))

        }
        binding.viewOrder.setOnClickListener{

            startActivity(Intent(this, ViewOrders::class.java))
        }

    }
}