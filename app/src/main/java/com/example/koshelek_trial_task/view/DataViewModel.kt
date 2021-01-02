package com.example.koshelek_trial_task.view

import android.util.Log
import androidx.lifecycle.*
import com.example.koshelek_trial_task.data_classes.SingleEntity
import com.example.koshelek_trial_task.network.BinanceWebSocket
import com.example.koshelek_trial_task.view.Adapter
import com.neovisionaries.ws.client.WebSocketCloseCode
import com.neovisionaries.ws.client.WebSocketState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {

    private val _adapter = MutableLiveData<Adapter>()
    val adapter: LiveData<Adapter>
        get() = _adapter

    private val _symbol = MutableLiveData<String>()
    val symbol: LiveData<String>
    get() = _symbol

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    private val _binanceWebSocket = MutableLiveData<BinanceWebSocket>()
    val binanceWebSocket: LiveData<BinanceWebSocket>
        get() = _binanceWebSocket

    private val _connectError = MutableLiveData<Boolean>()
    val connectError: LiveData<Boolean>
        get() = _connectError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _wsCreated = MutableLiveData<Boolean>()
    val wsCreated: LiveData<Boolean>
        get() = _wsCreated

    private val _connected = MutableLiveData<Boolean>()
    val connected: LiveData<Boolean>
        get() = _connected

    fun wsExists(value: Boolean) {
        _wsCreated.value = value
    }

    fun isLoading(value: Boolean) {
        _loading.value = value
        Log.d("WebSocket", "Loading value ${loading.value}")
    }

    fun setConnectError(value: Boolean) {
        _connectError.value = value
    }

    init {
        _adapter.value = Adapter()
        _loading.value = true
        _wsCreated.value = false
        _symbol.value = "btcusdt"
        _type.value = "bids"
        _connected.value = false
    }

    fun connectWebSocket() {
        if (!_wsCreated.value!!) {
            _binanceWebSocket.value = BinanceWebSocket( this)
            _binanceWebSocket.value!!.socket.connectAsynchronously()
            _connected.value = true
        }
        wsExists(true)
    }

    fun reconnectWebSocket() {
        _connected.value = false
        _binanceWebSocket.value!!.disconnectSocket()
        _binanceWebSocket.value = BinanceWebSocket( this)
        _binanceWebSocket.value!!.socket.connectAsynchronously()
        _connected.value = true
    }

    fun setSymbol(newSymbol: String) {
        _symbol.value = newSymbol
    }

    fun setType(newType: String) {
        _type.value = newType
    }
}