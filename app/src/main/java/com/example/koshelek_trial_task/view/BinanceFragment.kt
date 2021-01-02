package com.example.koshelek_trial_task.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koshelek_trial_task.R
import com.example.koshelek_trial_task.databinding.FragmentBinanceBinding
import com.example.koshelek_trial_task.network.BinanceWebSocket


class BinanceFragment : Fragment() {

    lateinit var binding: FragmentBinanceBinding
    lateinit var viewModel: DataViewModel
    lateinit var binanceWebSocket: BinanceWebSocket
    lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBinanceBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        binding.table.adapter = viewModel.adapter.value

        binding.table.layoutManager = LinearLayoutManager(context)
        binding.table.setHasFixedSize(true)
        binding.table.itemAnimator = DefaultItemAnimator()

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bids -> {
                    viewModel.setType("bids")
                    binanceWebSocket.showLoading()
                }
                R.id.asks -> {
                    viewModel.setType("asks")
                    binanceWebSocket.showLoading()
                }
                R.id.detail -> {
                    viewModel.setType("detail")
                    binanceWebSocket.showLoading()
                    Toast.makeText(context, "Detail screen not implemented",
                        Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        var spinnerAdapter =
            context?.let { ArrayAdapter.createFromResource(
                it, R.array.currencies, R.layout.spinner_item
            ) }
        spinnerAdapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)

        spinner = binding.spinner
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSymbol(spinner.selectedItem.toString().toLowerCase())
                binanceWebSocket.updateConnect()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binanceWebSocket = BinanceWebSocket(this, viewModel)
        binanceWebSocket.connectSocket()

        return binding.root
    }
}
