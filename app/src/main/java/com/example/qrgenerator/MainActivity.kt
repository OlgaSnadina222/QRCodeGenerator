package com.example.qrgenerator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.WriterException

class MainActivity : AppCompatActivity() {

    private var imQR:ImageView? = null
    private var bGenerate:Button? = null
    private var bScanner:Button? = null
    private var edInfo:EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imQR = findViewById(R.id.imQR)
        bGenerate = findViewById(R.id.bGenerate)
        bScanner = findViewById(R.id.bScanner)
        edInfo = findViewById(R.id.edInfo)


        bGenerate?.setOnClickListener {
            generateQrCode(edInfo?.text.toString())
            clearTextForInfo()
        }
        bScanner?.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun clearTextForInfo(){
        val info = edInfo?.text.toString()
        if (info.isNotEmpty()){
            edInfo?.setText("")
        }
    }

    private fun generateQrCode(text: String){
        val qrGenerator = QRGEncoder(text, null, QRGContents.Type.TEXT, 600)
        try {
            val bMap = qrGenerator.encodeAsBitmap()
            imQR?.setImageBitmap(bMap)
        } catch (e: WriterException){
        }
    }

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)

        } else {
            startActivity(Intent(this, ScannerActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(this, ScannerActivity::class.java))
            }
        }
    }
}