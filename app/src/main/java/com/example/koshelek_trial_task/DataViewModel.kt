package com.example.koshelek_trial_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koshelek_trial_task.data_classes.SingleEntity

class DataViewModel: ViewModel() {

    private val _tableData = MutableLiveData<MutableList<SingleEntity>>()
    val tableData: LiveData<MutableList<SingleEntity>>
    get() = _tableData

    private val _adapter = MutableLiveData<Adapter>()
    val adapter: LiveData<Adapter>
    get() = _adapter

    private val _symbol = MutableLiveData<String>()
    val symbol: LiveData<String>
    get() = _symbol

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    init {
        _adapter.value = Adapter()
        _symbol.value = "/ws/btcusdt@depth"
        _type.value = "bids"
    }

    fun putTableData(entities: MutableList<SingleEntity>) {
        _tableData.value = entities
        _adapter.value?.data = _tableData.value!!
    }

    fun setSymbol(newSymbol: String) {
        _symbol.value = newSymbol
    }

    fun setType(newType: String) {
        _type.value = newType
    }

}