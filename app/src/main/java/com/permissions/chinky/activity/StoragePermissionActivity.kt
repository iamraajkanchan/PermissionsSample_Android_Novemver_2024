package com.permissions.chinky.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
            launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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

}