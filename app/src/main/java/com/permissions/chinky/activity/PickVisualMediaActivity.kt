package com.permissions.chinky.activity

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.permissions.chinky.R

/**
 * Resource : https://developer.android.com/training/data-storage/shared/photopicker?form=MG0AV3
 * */
class PickVisualMediaActivity : AppCompatActivity() {

    private lateinit var pickVisualMediaRequest: PickVisualMediaRequest
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var launcher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pick_visual_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        val btnPickFile: Button = findViewById(R.id.btnPickFile);
        val imgFilePreview: ImageView = findViewById(R.id.imgFilePreview);
        storagePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission(), {
                if (it) {
                    launcher.launch(pickVisualMediaRequest)
                } else {
                    showPermissionRationale()
                }
            })
        pickVisualMediaRequest = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        btnPickFile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(pickVisualMediaRequest)
            } else {
                storagePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imgFilePreview.setImageURI(uri)
            }
        }
    }


    private fun showPermissionRationale() {
        AlertDialog.Builder(this).setTitle("Read Permission Needed")
            .setMessage("This app needs access to your external storage to fetch images.")
            .setPositiveButton("OK") { _, _ -> storagePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE) }
            .setNegativeButton("Cancel", null).create().show()
    }
}