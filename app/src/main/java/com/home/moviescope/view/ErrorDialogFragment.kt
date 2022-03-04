package com.home.moviescope.view

import android.app.Dialog
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.home.moviescope.R

class ErrorDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (Settings.System.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) == 0
        ) {
            return AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.airplane_message_off))
                .setPositiveButton(getString(R.string.dialog_error_ok)) { _, _ -> }
                .create()
        } else {
            return AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.airplane_message_on))
                .setPositiveButton(getString(R.string.dialog_error_ok)) { _, _ -> }
                .create()
        }
    }

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}