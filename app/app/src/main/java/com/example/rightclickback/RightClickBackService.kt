package com.example.rightclickback

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

/**
 * 全域滑鼠右鍵攔截與返回轉譯無障礙服務
 */
class RightClickBackService : AccessibilityService() {

    companion object {
        private const val TAG = "RightClickBack"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "RightClickBackService 連線成功")

        val info = serviceInfo ?: AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = flags or AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
        }
        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // UI 事件觀察
    }

    override fun onInterrupt() {
        Log.w(TAG, "服務中斷")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val keyCode = event.keyCode
            Log.d(TAG, "偵測到 KeyCode: $keyCode")

            when (keyCode) {
                KeyEvent.KEYCODE_BACK,
                KeyEvent.KEYCODE_CONTEXT_MENU,
                KeyEvent.KEYCODE_MENU,
                KeyEvent.KEYCODE_ESCAPE,
                KeyEvent.KEYCODE_STB_INPUT,
                KeyEvent.KEYCODE_BUTTON_2,
                KeyEvent.KEYCODE_BUTTON_B -> {
                    Log.d(TAG, "觸發系統返回 GLOBAL_ACTION_BACK")
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    return true
                }
            }
        }
        return false
    }
}
