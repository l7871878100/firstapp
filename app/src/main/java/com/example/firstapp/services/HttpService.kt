package com.example.firstapp.services

import android.app.IntentService
import android.content.Intent
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

const val ACTION_HISTORY = "com.example.firstapp.services.action.HISTORY"
const val ACTION_UPDATE = "com.example.firstapp.services.action.UPDATE"

const val IMEI = "com.example.firstapp.services.extra.IMEI"
const val NICK_NAME = "com.example.firstapp.services.extra.NICK_NAME"
const val SCORE = "com.example.firstapp.services.extra.SCORE"

private const val SERVER_URL = "https://localhost:8443/"

class HttpService : IntentService("HttpService") {

    private var queue: RequestQueue = Volley.newRequestQueue(this)

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_HISTORY -> {
                val imei = intent.getStringExtra(IMEI)
                handleActionGetHistoryScore(imei)
            }
            ACTION_UPDATE -> {
                val jsonRequest = JSONObject()
                val imei = intent.getStringExtra(IMEI)
                val score = intent.getStringExtra(SCORE)
                jsonRequest.put("score", score)
                handleActionUpdateScore(imei, jsonRequest)

            }
        }
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionGetHistoryScore(imei: String) {
        val jsonObjectRequest = JsonObjectRequest("$SERVER_URL/getAccount/$imei", null, Response.Listener<JSONObject> {
        }, Response.ErrorListener { })
        queue.add(jsonObjectRequest)
    }

    private fun handleActionUpdateScore(imei: String, params: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT,
            "$SERVER_URL/updateScore/$imei",
            params,
            Response.Listener<JSONObject> {
        }, Response.ErrorListener { })
        queue.add(jsonObjectRequest)
    }

}
