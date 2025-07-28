package com.bitchat.android.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class NetworkManager(private val context: Context) {

    companion object {
        private const val TAG = "NetworkManager"
        private const val RENDEZVOUS_PORT = 12345
    }

    private val networkScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var serverSocket: ServerSocket? = null
    private val connectedSockets = mutableMapOf<String, Socket>()

    fun startServer() {
        if (isNetworkAvailable()) {
            networkScope.launch {
                try {
                    serverSocket = ServerSocket(RENDEZVOUS_PORT)
                    Log.d(TAG, "Server started on port $RENDEZVOUS_PORT")
                    while (true) {
                        val clientSocket = serverSocket!!.accept()
                        Log.d(TAG, "Client connected: ${clientSocket.inetAddress.hostAddress}")
                        // Handle client connection in a new coroutine
                        networkScope.launch {
                            handleClient(clientSocket)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting server: ${e.message}")
                }
            }
        } else {
            Log.w(TAG, "Network not available, server not started.")
        }
    }

    private fun handleClient(clientSocket: Socket) {
        try {
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val writer = PrintWriter(clientSocket.getOutputStream(), true)
            val clientAddress = clientSocket.inetAddress.hostAddress
            connectedSockets[clientAddress] = clientSocket

            // Read messages from the client
            var inputLine: String?
            while (reader.readLine().also { inputLine = it } != null) {
                Log.d(TAG, "Received from $clientAddress: $inputLine")
                // Echo the message back to the client
                writer.println("Echo: $inputLine")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling client: ${e.message}")
        } finally {
            clientSocket.close()
            connectedSockets.remove(clientSocket.inetAddress.hostAddress)
        }
    }

    fun connectToPeer(peerAddress: String) {
        if (isNetworkAvailable()) {
            networkScope.launch {
                try {
                    val socket = Socket(peerAddress, RENDEZVOUS_PORT)
                    Log.d(TAG, "Connected to peer: $peerAddress")
                    // Handle the connection
                    handleClient(socket)
                } catch (e: Exception) {
                    Log.e(TAG, "Error connecting to peer: ${e.message}")
                }
            }
        } else {
            Log.w(TAG, "Network not available, cannot connect to peer.")
        }
    }

    fun sendMessage(peerAddress: String, message: String) {
        val socket = connectedSockets[peerAddress]
        if (socket != null && socket.isConnected) {
            networkScope.launch {
                try {
                    val writer = PrintWriter(socket.getOutputStream(), true)
                    writer.println(message)
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending message: ${e.message}")
                }
            }
        } else {
            Log.w(TAG, "Socket not connected to $peerAddress")
        }
    }

    fun disconnect() {
        try {
            serverSocket?.close()
            connectedSockets.values.forEach { it.close() }
            connectedSockets.clear()
            Log.d(TAG, "Network manager disconnected.")
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting: ${e.message}")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
