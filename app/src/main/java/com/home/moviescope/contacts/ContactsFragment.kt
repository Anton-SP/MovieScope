package com.home.moviescope.contacts

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.moviescope.databinding.ContactsFragmentBinding


const val REQUEST_CODE = 42

/**
 * Оказалось выгрузка списков контактов занимает время
 * и не маленькое когда контаков много
 */
class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter()
        binding.contactsFragmentRv.adapter = adapter
        checkPermission()

        adapter.setOnItemClickListener(object : ContactsAdapter.onItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                var intent: Intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:" + adapter.getContactsList()[position].phone)
                )
                startActivity(intent)
            }
        })
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    if (ContextCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        getContacts()
                    } else {
                        requestPermission()
                    }
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Необходим в целях обучения!")
                        .setPositiveButton("Хорошо, согласен") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("НЕТ!") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.CALL_PHONE
            ), REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        getContacts()
                    } else context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к вызову")
                            .setMessage("Доступ к совершению вызовов не предоставлен, невозможно произвести вызов абонента")
                            .setNegativeButton("Закрыть") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Доступ к контактам не предоставлен, невозможно отобразить информацию")
                            .setNegativeButton("Закрыть") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsFragment()
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver

            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            val cursotPhones: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                null,
                null
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val id =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val hasPhone =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if (hasPhone.toInt() == 1) {
                            var cursorPhones: Cursor? = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )
                            cursorPhones?.let { it ->
                                for (i in 0..cursorPhones.count)
                                    if (cursorPhones.moveToPosition(i)) {
                                        val phoneNumber =
                                            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        addView(name, phoneNumber)
                                    }
                                it.close()
                            }
                        }
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun addView(name: String?, phone: String?) {
        name?.let {
            phone?.let { adapter.insertData(Contacts(name, phone)) }
        }
    }

}