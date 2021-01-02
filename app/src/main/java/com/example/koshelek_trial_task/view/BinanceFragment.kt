package com.example.koshelek_trial_task.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koshelek_trial_task.R
import com.example.koshelek_trial_task.databinding.FragmentBinanceBinding
import com.example.koshelek_trial_task.network.BinanceWebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BinanceFragment : Fragment() {

    lateinit var binding: FragmentBinanceBinding
    lateinit var viewModel: DataViewModel
    lateinit var spinner: Spinner

    fun binanceLifecycleScope(): LifecycleCoroutineScope {
        return this.lifecycleScope
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBinanceBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        binding.table.setItemViewCacheSize(0)
        binding.table.layoutManager = LinearLayoutManager(context)
        binding.table.setHasFixedSize(true)
        binding.table.itemAnimator = DefaultItemAnimator()

        binding.table.adapter = viewModel.adapter.value

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bids -> {
                    viewModel.setType("bids")
                    viewModel.isLoading(true)
                }
                R.id.asks -> {
                    viewModel.setType("asks")
                    viewModel.isLoading(true)
                }
                R.id.detail -> {
                    viewModel.setType("detail")
                    viewModel.isLoading(true)
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
        spinner.setSelection(0, false)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSymbol(spinner.selectedItem.toString().toLowerCase())
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    viewModel.reconnectWebSocket()
                    Log.d("Debugging", "Spinner called")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.connectError.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                Toast.makeText(
                    context, "WebSocket connection error. Please, check your " +
                            "network connection and reconnect to the WebSocket by selecting type above",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            when (loading) {
                true -> {
                    showLoading()
                    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                    if (!activeNetwork?.isConnectedOrConnecting!!) {
                        viewModel.setConnectError(true)
                    }
                }
                false -> stopLoading()
            }
        })

        viewModel.connectWebSocket()

        return binding.root
    }

    fun showLoading() {
        makeInvisible(binding.table)
        makeVisible(binding.loading)
    }

    fun stopLoading() {
        makeInvisible(binding.loading)
        makeVisible(binding.table)
    }

    fun makeVisible(view: View) {
        if (view.visibility == View.INVISIBLE || view.visibility == View.GONE)
            view.visibility = View.VISIBLE
    }

    fun makeInvisible(view: View) {
        if (view.visibility == View.VISIBLE)
            view.visibility = View.INVISIBLE
    }

}
