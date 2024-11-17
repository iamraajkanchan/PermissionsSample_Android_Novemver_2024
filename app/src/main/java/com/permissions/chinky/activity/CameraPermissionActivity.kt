package com.permissions.chinky.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.permissions.chinky.R
import com.permissions.chinky.utility.Utility

class CameraPermissionActivity : AppCompatActivity() {

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera_permission)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        val imgCameraPreview: ImageView = findViewById(R.id.imgCameraPreview)
        val btnTakeAPicture: Button = findViewById(R.id.btnTakeAPicture)
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    if (result.data != null) {
                        val imageBitMap = result.data?.extras?.get("data") as Bitmap
                        Utility.printLog(
                            CameraPermissionActivity::class.java,
                            Thread.currentThread().stackTrace[2],
                            "data : ${result.data?.extras}"
                        )
                        imgCameraPreview.setImageBitmap(imageBitMap)
                    }
                }
            }
        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    takePicture()
                } else {
                    showPermissionRationale()
                }
            }
        btnTakeAPicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicture()
            } else {
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun takePicture() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) == null) {
            cameraLauncher.launch(cameraIntent)
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this).setTitle("Read Permission Needed")
            .setMessage("This app needs permission to click photographs.")
            .setPositiveButton("OK") { _, _ -> requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }
            .setNegativeButton("Cancel", null).create().show()
    }
}