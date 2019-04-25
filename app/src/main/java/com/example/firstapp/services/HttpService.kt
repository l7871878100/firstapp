package com.example.firstapp.services

import android.app.IntentService
import android.content.Intent
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

private const val SERVER_URL = "https://localhost"

class HttpService : IntentService("HttpService") {

    private var queue: RequestQueue? = null

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_HISTORY -> {
                val jsonRequest = JSONObject()
                val imei = intent.getStringExtra(IMEI)
                val nickname = intent.getStringExtra(NICK_NAME)

                jsonRequest.put("imei", imei)
                jsonRequest.put("nickName", nickname)
                handleActionGetHistoryScore(jsonRequest)
            }
            ACTION_UPDATE -> {
                val jsonRequest = JSONObject()
                val imei = intent.getStringExtra(IMEI)
                val score = intent.getStringExtra(SCORE)
                jsonRequest.put("imei", imei)
                jsonRequest.put("score", score)
                handleActionUpdateScore(jsonRequest)

            }
        }
    }

    private fun getQueue(): RequestQueue {
        if (queue == null) {
            queue = Volley.newRequestQueue(this)
        }
        return queue!!
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionGetHistoryScore(params: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(SERVER_URL, params, Response.Listener<JSONObject> {
        }, Response.ErrorListener { })
        getQueue().add(jsonObjectRequest)
    }

    private fun handleActionUpdateScore(params: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(SERVER_URL, params, Response.Listener<JSONObject> {
        }, Response.ErrorListener { })
        getQueue().add(jsonObjectRequest)
    }

}
