package com.example.koshelek_trial_task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koshelek_trial_task.data_classes.Entities
import com.example.koshelek_trial_task.data_classes.SingleEntity
import com.example.koshelek_trial_task.databinding.FragmentBinanceBinding
import com.example.koshelek_trial_task.network.BinanceWebSocket
import com.example.koshelek_trial_task.network.Message
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.neovisionaries.ws.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_main.*


class BinanceFragment : Fragment() {

    lateinit var binding: FragmentBinanceBinding
    lateinit var viewModel: DataViewModel
    lateinit var binanceWebSocket: BinanceWebSocket

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
                R.id.bids -> {viewModel.setType("bids")
                    Log.d("WebSocket", "Bids selected")
                }
                R.id.asks -> {viewModel.setType("asks")
                    Log.d("WebSocket", "Asks selected")
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        binanceWebSocket = BinanceWebSocket(this, viewModel)

        return binding.root
    }
}