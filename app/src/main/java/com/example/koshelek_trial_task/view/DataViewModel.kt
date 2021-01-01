package com.example.koshelek_trial_task.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koshelek_trial_task.data_classes.SingleEntity
import com.example.koshelek_trial_task.network.BinanceWebSocket
import com.example.koshelek_trial_task.view.Adapter

class DataViewModel: ViewModel() {

    private val _adapter = MutableLiveData<Adapter>()
    val adapter: LiveData<Adapter>
        get() = _adapter

    private val _symbol = MutableLiveData<String>()
    private val _fullSymbol = MutableLiveData<String>()
    val symbol: LiveData<String>
    get() = _fullSymbol

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    init {
        _adapter.value = Adapter()
        _symbol.value = "btcusdt"
        _fullSymbol.value = "/ws/${_symbol.value}@depth"
        _type.value = "bids"
    }

    fun setSymbol(newSymbol: String) {
        _symbol.value = newSymbol
        _fullSymbol.value = "/ws/${_symbol.value}@depth"
    }

    fun setType(newType: String) {
        _type.value = newType
    }
}