package com.example.koshelek_trial_task.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.koshelek_trial_task.BinanceFragment
import com.example.koshelek_trial_task.DataViewModel
import com.example.koshelek_trial_task.data_classes.Entities
import com.example.koshelek_trial_task.data_classes.SingleEntity
import com.google.gson.Gson
import com.neovisionaries.ws.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val baseEndpoint = "wss://stream.binance.com:9443"
const val connectionTimeout = 5000

class BinanceWebSocket(fragment: BinanceFragment, viewModel: DataViewModel) {

    lateinit var socket: WebSocket
    lateinit var factory: WebSocketFactory

    init {
        fragment.lifecycleScope.launch(Dispatchers.IO) {
            factory = WebSocketFactory().setConnectionTimeout(connectionTimeout)

            socket = WebSocketFactory().createSocket("${baseEndpoint}${viewModel.symbol.value}")

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
                }

                override fun onTextMessage(websocket: WebSocket?, text: String?) {
                    super.onTextMessage(websocket, text)
                    Log.d("WebSocket", "Text message received")

                    val message = messageToGson(text)
                    val bidsAndAsks = messageToEntities(message)
                    Log.d("WebSocket", "Bids ${bidsAndAsks.first.values}")
                    Log.d("WebSocket", "Asks ${bidsAndAsks.second.values}")

                    fragment.lifecycleScope.launch(Dispatchers.Main) {
                        fragment.binding.table.recycledViewPool.clear()
                        when (viewModel.type.value) {
                            "bids" -> fragment.viewModel.adapter.value!!.data = bidsAndAsks.first.values
                            "asks" -> fragment.viewModel.adapter.value!!.data = bidsAndAsks.second.values
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
                    Log.d("WebSocket", "Disconnected")
                }
            })
            socket.connectAsynchronously()
        }
    }

    fun messageToGson(message: String?): Message {
        return Gson().fromJson("""$message""", Message::class.java)
    }

    fun messageToEntities(message: Message): Pair<Entities, Entities> {

        var bids = Entities(
            "bids",
            mutableListOf()
        )
        var asks = Entities(
            "asks",
            mutableListOf()
        )

        for (elem in message.bids) {
            var singleEntity =
                SingleEntity(
                    mutableListOf()
                )
            singleEntity.values = elem
            bids.values.add(singleEntity)
        }
        for (elem in message.asks) {
            var singleEntity =
                SingleEntity(
                    mutableListOf()
                )
            singleEntity.values = elem
            asks.values.add(singleEntity)
        }
        return Pair(bids, asks)
    }
}

