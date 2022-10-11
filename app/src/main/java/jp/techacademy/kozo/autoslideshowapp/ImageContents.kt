package jp.techacademy.kozo.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat


class ImageContents(activity: MainActivity) {

    // property
    private val PERMISSIONS_REQUEST_CODE = 100
    private var cursor: Cursor? = null
    private var index = 0

    // initialize
    init {
        Log.d("test", "Create ImageContents Instance...")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("test", "permitted")
                var resolver = activity.contentResolver
                this.cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目（null = 全項目）
                    null, // フィルタ条件（null = フィルタなし）
                    null, // フィルタ用パラメータ
                    null // ソート (nullソートなし）
                )
            } else {
                Log.d("test", "not permitted")
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    // get an image
    fun getNextUri(reversed: Boolean) : Uri? {
        // check index
        Log.d("test", "${this.index} ${this.cursor!!.count}")
        if (reversed) {
            this.index--
            if (this.index == -1) {
                this.index = this.cursor!!.count - 1
            }
            this.cursor!!.moveToPosition(index)
        } else {
            this.index++
            if (this.index == this.cursor!!.count) {
                this.index = 0
            }
            this.cursor!!.moveToPosition(index)
        }

        // get uri
        if (this.cursor!!.moveToPosition(index)){
            val fieldIndex = this.cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = this.cursor!!.getLong(fieldIndex)
            return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        }

        return null
    }
}