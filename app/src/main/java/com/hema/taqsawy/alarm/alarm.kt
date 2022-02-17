package com.hema.taqsawy.alarm

import android.media.Ringtone

object alarm {
    lateinit var ringtone: Ringtone
    fun isringtoneInit()= ::ringtone.isInitialized

}