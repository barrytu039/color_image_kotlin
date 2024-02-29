package com.barry.color_imgage_kotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.barry.color_imgage_kotlin.databinding.ActivityStartBinding
import com.barry.color_imgage_kotlin.main.MainActivity

class StartActivity : AppCompatActivity() {

    private var _viewBind: ActivityStartBinding? = null
    private val viewBind: ActivityStartBinding get() = _viewBind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBind = ActivityStartBinding.inflate(layoutInflater)
        setContentView(viewBind.root)


        viewBind.activityStartNextPageButton.setOnClickListener {
            val mainIntent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }
        GlideApp.with(this)

    }


    fun checkPermission(permission: String?): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, 1001)
    }
}