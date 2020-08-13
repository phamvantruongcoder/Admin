package com.example.admin.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.admin.BaseFragment
import com.example.admin.BuildConfig
import com.example.admin.R
import com.example.admin.SingleMediaScanner
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_load_image.*
import java.io.File

class LoadImageFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_load_image

    override fun initViews() {
         tvLoadImage.setOnClickListener {
             Dexter.withActivity(activity).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object : PermissionListener{
                 override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                     pickImageFromGallery()
                 }

                 override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                 }

                 override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                     val dialog = AlertDialog.Builder(activity!!)
                     dialog.setTitle("Notification")
                     dialog.setMessage("You need accept all permissions to use app")
                     dialog.setPositiveButton("OK") { _, _ ->
                         startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)))
                     }

                     dialog.setNegativeButton("No") { p, _ -> p.dismiss() }
                     dialog.create().show()
                 }

             }).check()
         }
    }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Check default gallery app found
        galleryIntent.resolveActivity(activity!!.packageManager)?.let {
            startActivityForResult(galleryIntent, 100)
        }
    }

    private fun openGalleryFromPathToFolder(
        context: Context,
        folderPath: String?
    ): Boolean {
        val folder = File(folderPath)
        val allFiles = folder.listFiles()
        if (allFiles != null && allFiles.isNotEmpty()) {
            val imageInFolder = getImageContentUri(context, allFiles[0])
            if (imageInFolder != null) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = imageInFolder
                context.startActivity(intent)
                return true
            }
        }
        return false
    }

    private fun getImageContentUri(
        context: Context,
        imageFile: File
    ): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath),
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id =
                cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "" + id
            )
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}