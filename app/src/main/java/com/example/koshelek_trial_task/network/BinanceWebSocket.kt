package com.example.koshelek_trial_task.network

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.koshelek_trial_task.MainActivity
import com.example.koshelek_trial_task.view.BinanceFragment
import com.example.koshelek_trial_task.view.DataViewModel
import com.example.koshelek_trial_task.data_classes.Entities
import com.example.koshelek_trial_task.data_classes.SingleEntity
import com.example.koshelek_trial_task.view.Adapter
import com.google.gson.Gson
import com.neovisionaries.ws.client.*
import kotlinx.coroutines.*


const val baseEndpoint = "wss://stream.binance.com:9443"
const val connectionTimeout = 5000

class BinanceWebSocket(val viewModel: DataViewModel) {

    var socket: WebSocket

    init {
        var factory: WebSocketFactory = WebSocketFactory().setConnectionTimeout(connectionTimeout)
        socket = factory.createSocket("${baseEndpoint}/ws/${viewModel.symbol.value}@depth")
        socket.addListener(object : WebSocketAdapter() {
            override fun onConnected(
                websocket: WebSocket?,
                headers: MutableMap<String, MutableList<String>>?
            ) {
                super.onConnected(websocket, headers)
                Log.d("WebSocket", "Connected")
            }

            override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                super.onConnectError(websocket, exception)
                Log.d("WebSocket", "On connect error $exception")
                viewModel.setConnectError(true)
            }

            override fun onTextMessageError(
                websocket: WebSocket?,
                cause: WebSocketException?,
                data: ByteArray?
            ) {
                super.onTextMessageError(websocket, cause, data)
                Log.d("WebSocket", "On text message error ${cause.toString()}")
            }

            override fun onTextMessage(websocket: WebSocket?, text: String?) {
                super.onTextMessage(websocket, text)
                Log.d("WebSocket", "Text message received $text")
                val message = messageToGson(text)
                val bidsAndAsks = messageToEntities(message)
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    when (viewModel.type.value) {
                        "bids" -> {
                            viewModel.adapter.value!!.isBids = true
                            viewModel.adapter.value!!.data = bidsAndAsks.first.values
                            if (viewModel.connected.value!!)
                                viewModel.isLoading(false)
                        }
                        "asks" -> {
                            viewModel.adapter.value!!.isBids = false
                            viewModel.adapter.value!!.data = bidsAndAsks.second.values
                            if (viewModel.connected.value!!)
                                viewModel.isLoading(false)
                        }
                    }
                }
            }

            override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
                super.onError(websocket, cause)
                Log.d("WebSocket", "Error ${cause.toString()}")
            }

            override fun onDisconnected(
                websocket: WebSocket?,
                serverCloseFrame: WebSocketFrame?,
                clientCloseFrame: WebSocketFrame?,
                closedByServer: Boolean
            ) {
                super.onDisconnected(
                    websocket,
                    serverCloseFrame,
                    clientCloseFrame,
                    closedByServer
                )
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    Log.d("WebSocket", "Disconnected")
                    viewModel.isLoading(true)
                }
            }
        })
    }

    fun messageToGson(message: String?): Message {
        return Gson().fromJson("""$message""", Message::class.java)
    }

    fun messageToEntities(message: Message): Pair<Entities, Entities> {
        var bids = Entities("bids", mutableListOf())
        var asks = Entities("asks", mutableListOf())
        for (elem in message.bids) {
            var singleEntity = SingleEntity(mutableListOf())
            singleEntity.values = elem
            bids.values.add(singleEntity)
        }
        for (elem in message.asks) {
            var singleEntity = SingleEntity(mutableListOf())
            singleEntity.values = elem
            asks.values.add(singleEntity)
        }
        return Pair(bids, asks)
    }

    fun disconnectSocket() {
        socket = socket.disconnect()
        socket = socket.sendClose()
    }
}


