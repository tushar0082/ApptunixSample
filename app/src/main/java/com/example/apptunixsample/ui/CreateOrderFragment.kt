package com.example.apptunixsample.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apptunixsample.OrderDatabase
import com.example.apptunixsample.OrderRepository
import com.example.apptunixsample.R
import com.example.apptunixsample.model.Orders
import com.example.apptunixsample.databinding.FragmentCreateOrderBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class CreateOrderFragment : Fragment() {
    private var _binding: FragmentCreateOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardActivityViewModel
    private var date: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateOrderBinding.inflate(inflater, container, false)
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

        setupSpinners()
        setupDatePicker()
        setupSaveButton()
        setPrice()
    }


    private fun setupSpinners() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_options,
            R.layout.spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.fat_options,
            R.layout.spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner3.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            date = selectedDate.timeInMillis
        }
    }

    private fun setupSaveButton() {
        binding.save.setOnClickListener {
            val quantity = binding.quantity.text.toString()
            if (quantity.isBlank()) {
                binding.quantity.error = "Please enter a valid quantity"
            } else {
                lifecycleScope.launch(Dispatchers.IO) {

                    viewModel.insertData(  Orders(
                        quantity = quantity,
                        time = binding.spinner.selectedItem.toString(),
                        fat = binding.spinner3.selectedItem.toString(),
                        date = date,
                        orderId = 0
                    ))

                }
                Toast.makeText(requireContext(), "Order Successful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPrice() {
        binding.quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.price.text =
                    getString(R.string.calculated_price) + ": ${s.toString().toInt() * 50}"
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
