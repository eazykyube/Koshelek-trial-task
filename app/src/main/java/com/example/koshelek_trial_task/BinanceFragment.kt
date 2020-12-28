package com.example.koshelek_trial_task

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.koshelek_trial_task.databinding.FragmentBinanceBinding
import com.google.gson.Gson
import com.neovisionaries.ws.client.*


class BinanceFragment : Fragment() {

    companion object{
        const val baseEndpoint = "wss://stream.binance.com:9443"
        const val connectionTimeout = 5000
    }

    lateinit var socket: WebSocket
    lateinit var factory: WebSocketFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBinanceBinding.inflate(inflater)

        factory = WebSocketFactory().setConnectionTimeout(connectionTimeout)
        socket = WebSocketFactory().createSocket("${baseEndpoint}/ws/btcusdt@depth")

        socket.addListener(object: WebSocketAdapter() {
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
                val message = messageToGson(text)
                Log.d("WebSocket", "Bids ${message.bids}")
                Log.d("WebSocket", "Asks ${message.asks}")
            }

            override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
                super.onError(websocket, cause)
                Log.d("WebSocket", "Error ${cause.toString()}")
            }
        })

        socket.connectAsynchronously()

        return binding.root
    }

    fun messageToGson(message: String?): Message {
        return Gson().fromJson("""$message""", Message::class.java)
    }

}