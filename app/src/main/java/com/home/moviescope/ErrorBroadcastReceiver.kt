package com.home.moviescope

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.home.moviescope.view.ErrorDialogFragment

const val TAG = "MyBroadcastReceiver"

class MyTestBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        ErrorDialogFragment().show(
            (context as AppCompatActivity).supportFragmentManager,ErrorDialogFragment.TAG)
    }
}