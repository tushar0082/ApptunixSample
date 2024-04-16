package com.example.apptunixsample.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptunixsample.OrderDatabase
import com.example.apptunixsample.OrderRecyclerAdapter
import com.example.apptunixsample.OrderRepository
import com.example.apptunixsample.R
import com.example.apptunixsample.databinding.FragmentViewOrdersBinding
import com.example.apptunixsample.model.Orders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ViewOrdersFragment : Fragment() {

    private var _binding: FragmentViewOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderRecyclerAdapter
    private var orderList: MutableList<Orders> = mutableListOf()
    private var startDate: String? = null
    private var endDate: String? = null
    private lateinit var startDateFilter:Date
    private lateinit var endDateFilter:Date
    private lateinit var viewModel: DashboardActivityViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = OrderDatabase.getInstance(requireContext().applicationContext).contactDao()
        val dataRepository = OrderRepository(dao)
        val viewModelFactory = DashBoardActivityViewModelFactory(dataRepository)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[DashboardActivityViewModel::class.java]

        setupRecyclerView()
        setupDatePickers()
        loadOrders()
    }


    private fun setupRecyclerView() {
        orderAdapter = OrderRecyclerAdapter(orderList)
        binding.orderRecycler.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupDatePickers() {
        binding.startDate.setOnClickListener { showDatePickerDialog(true) }
        binding.endDate.setOnClickListener { showDatePickerDialog(false) }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }.time
                val formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)
                if (isStartDate) {
                    startDate = formattedDate
                    binding.startDate.text = startDate
                } else {
                    endDate = formattedDate
                    binding.endDate.text = endDate
                }
                if (startDate.isNullOrEmpty().not())
                {
                    filterOrders()
                }else{
                    Toast.makeText(requireContext(),"Please Also select start Date",Toast.LENGTH_SHORT).show()
                }


            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun loadOrders() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val orders =viewModel.getData()
                binding.totalOrders.text=getString(R.string.total_no_of_orders)+": ${orders.count()}"
                orderList.addAll(orders)
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show()
                }

                e.printStackTrace()
            }
        }
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
                binding.totalOrders.text=getString(R.string.total_no_of_orders)+": ${filteredOrders.count()}"

                withContext(Dispatchers.Main) {
                    orderAdapter.setOrders(filteredOrders.toMutableList())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}