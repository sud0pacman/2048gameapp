package com.example.a2048.utils

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.example.a2048.data.SideEnum
import kotlin.math.abs

class MyTouchListener(private val context: Context) : View.OnTouchListener {
    private val gestureDetector = GestureDetector(context, MyGestureDetector())
    private var actionSideEnumListener : ((SideEnum) -> Unit)?= null

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    private inner class MyGestureDetector : SimpleOnGestureListener() {
        override fun onFling(
            startEvent: MotionEvent?,
            endEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (abs(startEvent!!.x - endEvent.x) <= 100 && abs(startEvent.y - endEvent.y) <= 100) return true
            // horizontal
            if (abs(startEvent.x - endEvent.x) > abs(startEvent.y - endEvent.y)) {
                if (startEvent.x > endEvent.x) actionSideEnumListener?.invoke(SideEnum.LEFT)
                else actionSideEnumListener?.invoke(SideEnum.RIGHT)
            } else {
                if (startEvent.y > endEvent.y) actionSideEnumListener?.invoke(SideEnum.UP)
                else actionSideEnumListener?.invoke(SideEnum.DOWN)
            }

            return super.onFling(startEvent, endEvent, velocityX, velocityY)
        }
    }

    fun setActionSideEnumListener(block: (SideEnum) -> Unit) {
        this.actionSideEnumListener = block
    }
}