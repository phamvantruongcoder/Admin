package com.example.admin

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

class SingleMediaScanner: MediaScannerConnection.MediaScannerConnectionClient {
    private var mMs: MediaScannerConnection? = null
    private var mFile: File? = null
    constructor(context: Context?, f: File)  {
        mFile = f
        mMs = MediaScannerConnection(context, this)
        mMs!!.connect()
    }
    override fun onMediaScannerConnected() {
        mMs!!.scanFile(mFile!!.absolutePath, null)
    }

    override fun onScanCompleted(p0: String?, p1: Uri?) {
         mMs!!.disconnect()
    }
}