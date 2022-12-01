package com.example.yonunca_juegoparabeber.base

import android.app.Application
import android.content.Context

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private lateinit var instance: BaseApplication

        fun getApplicationContext(): Context {
            return instance.applicationContext
        }
    }

}