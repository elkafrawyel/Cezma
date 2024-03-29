package com.cezma.app.utiles

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cezma.app.BuildConfig.DEBUG
import com.cezma.app.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Context.changeLanguage() {
    var locale: Locale? = null
    when (Injector.getPreferenceHelper().language) {
        Constants.Language.ARABIC.value -> {
            locale = Locale("ar")
        }
        Constants.Language.ENGLISH.value -> {
            locale = Locale("en")
        }
    }
    Locale.setDefault(locale)
    val config = this.resources.configuration
    config.setLocale(locale)
    this.createConfigurationContext(config)
//    Injector.getApplicationContext().createConfigurationContext(config)
    this.resources.updateConfiguration(config, this.resources.displayMetrics)
}


fun Context.restartApplication() {
    val intent = Injector.getApplicationContext().packageManager.getLaunchIntentForPackage(
        Injector.getApplicationContext().packageName
    )
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}

fun Context.saveLanguage(language: Constants.Language) {
    Injector.getPreferenceHelper().language = language.value
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBarWithAction(
    message: String,
    actionTitle: String,
    rootView: View,
    dismiss: Boolean = true,
    action: () -> Unit
) {
    val snackBar: Snackbar? = if (dismiss)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    else
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)

    if (snackBar != null) {
        val view = snackBar.view
        val textView = view.findViewById<View>(R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        snackBar.setAction(actionTitle) {
            action.invoke()
            snackBar.dismiss()
        }
        snackBar.show()
    }
}

fun Context.snackBar(message: String?, rootView: View) {
    val snackBar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}

fun Context.showMessageInDialog(message: String, okAction: () -> Unit, cancelAction: () -> Unit) {
    val dialog = AlertDialog.Builder(this)
        .setMessage(message)
        .setTitle(getString(R.string.app_name))
        .setCancelable(true)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            okAction.invoke()
            dialog.dismiss()
        }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            cancelAction.invoke()
            dialog.dismiss()
        }.create()

    dialog.show()

}

//=============================== Image Real Path =============================
@SuppressLint("ObsoleteSdkInt")
fun Context.getRealPathFromUri(uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(Injector.getApplicationContext(), uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return "${Environment.getExternalStorageDirectory()}/${split[1]}"
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )

            return getDataColumn(contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
        return getDataColumn(uri, null, null)
    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
        return uri.path
    }// File
    // MediaStore (and general)

    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

private fun getDataColumn(
    uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor =
            Injector.getApplicationContext().getContentResolver()
                .query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor!!.moveToFirst()) {
            if (DEBUG)
                DatabaseUtils.dumpCursor(cursor)

            val column_index = cursor!!.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

//=============================================================================================


