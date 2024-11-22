package com.permissions.chinky.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.permissions.chinky.R
import com.permissions.chinky.utility.Utility
import java.util.logging.Logger

class StoragePermissionActivity : AppCompatActivity() {

    private lateinit var launcher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_storage_permission)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        val btnWritePermission: Button = findViewById(R.id.btnWritePermission)
        val btnReadPermission: Button = findViewById(R.id.btnReadPermission)
        val btnPickVisualMedia: Button = findViewById(R.id.btnPickVisualMedia)
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Utility.printLog(
                        StoragePermissionActivity::class.java,
                        Thread.currentThread().stackTrace[2],
                        "Permission Granted"
                    )
                } else {
                    Utility.printLog(
                        StoragePermissionActivity::class.java,
                        Thread.currentThread().stackTrace[2],
                        "Permission Denied"
                    )
                }
            }
        btnWritePermission.setOnClickListener {
            Utility.printLog(
                StoragePermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                "Before Asking Write Permission"
            )
            // Repeat the same process before asking any permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionRationaleForStorage()
            } else {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            Utility.printLog(
                StoragePermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                "After Asking Write Permission"
            )
        }
        btnReadPermission.setOnClickListener {
            Utility.printLog(
                StoragePermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                "Before Asking Read Permission"
            )
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            Utility.printLog(
                StoragePermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                "After Asking Read Permission"
            )
        }
        btnPickVisualMedia.setOnClickListener {
            try {
                val pickVisualMediaIntent = Intent(this, PickVisualMediaActivity::class.java)
                startActivity(pickVisualMediaIntent)
            } catch (exception: ActivityNotFoundException) {
                Utility.printExceptionLog(
                    StoragePermissionActivity::class.java,
                    Thread.currentThread().stackTrace[2],
                    Logger.getLogger(Utility.APPLICATION_TAG),
                    exception
                )
            }
        }
    }

    private fun showPermissionRationaleForStorage() {
        AlertDialog.Builder(this)
            .setTitle("Read Permission Needed")
            .setMessage("This application needs access to your external storage")
            .setPositiveButton("OK") { _, _ ->
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}
