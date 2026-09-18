package com.example.rightclickback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var tvStatus: TextView
    private lateinit var btnOpenSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnOpenSettings = findViewById(R.id.btnOpenSettings)

        btnOpenSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    private fun updateServiceStatus() {
        val isEnabled = isAccessibilityServiceEnabled(this, RightClickBackService::class.java)
        if (isEnabled) {
            tvStatus.text = "服務狀態：已啟用 (運作中)"
            tvStatus.setTextColor(Color.GREEN)
        } else {
            tvStatus.text = "服務狀態：未啟用 (請點擊下方按鈕開啟)"
            tvStatus.setTextColor(Color.RED)
        }
    }

    private fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
        val expectedComponentName = "${context.packageName}/${service.name}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)

        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals(expectedComponentName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
