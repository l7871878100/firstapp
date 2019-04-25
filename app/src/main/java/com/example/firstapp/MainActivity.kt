package com.example.firstapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {


    companion object {
        val logger: Logger = Logger.getLogger(this.javaClass.name)
        var level = 0
        const val READ_PHONE_IMEI_CODE = 10
        const val INTERNET_CODE = 10
        var imei = ""
    }

    var queue: RequestQueue? = null

    private val levels = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIMEINumber()
        queue = Volley.newRequestQueue(this)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels.map { "等级$it" })
        val listView = findViewById<ListView>(R.id.levels_list)
        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            logger.info("选择的难度等级是：${levels[position]} ")
            level = levels[position]
        }
    }

    fun onClickStart(view: View) {
        val nickName = findViewById<EditText>(R.id.nicknameEditView).text.toString()
        val intent = Intent(this, TetrisActivityAW::class.java)
        intent.putExtra("level", level) //等级
        intent.putExtra("nickName", nickName) //昵称用户名
        intent.putExtra("imei", imei) //昵称用户名
        startActivity(intent)
    }

    private fun getText(url: String) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this)
        }

        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            logger.info(it)
        }, Response.ErrorListener {
            logger.info(it.message)
        })
        queue?.run {
            add(stringRequest)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_PHONE_IMEI_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getIMEINumber()
                else {
                    Toast.makeText(this, "用户未允许获取IMEI权限", Toast.LENGTH_SHORT).show()
                    val b = shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)
                    if (!b) {
                        //用户没同意授权,还不让下次继续提醒授权了,这是比较糟糕的情况
                        Toast.makeText(this, "用户勾选了下次不再提醒选项", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun getIMEINumber() {
        val readPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), READ_PHONE_IMEI_CODE)
        } else {

            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//28++(8.0)
                imei = telephonyManager.imei
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {//23~27(6.0~7.0)
                imei = telephonyManager.deviceId
            }
        }
    }

}
