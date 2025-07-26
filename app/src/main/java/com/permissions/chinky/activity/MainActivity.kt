package com.permissions.chinky.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.permissions.chinky.R
import com.permissions.chinky.utility.Utility

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (if (Thread.currentThread().stackTrace.size > 2) Thread.currentThread().stackTrace[2] else null)?.let {
            Utility.printLog(MainActivity::class.java, it, "")
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
        val btnStoragePermission: Button = findViewById(R.id.btnStoragePermission)
        val btnCameraPermission: Button = findViewById(R.id.btnCameraPermission)
        val btnFragmentLifecycle: Button = findViewById(R.id.btnFragmentLifecycle)
        btnStoragePermission.setOnClickListener {
            Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "onClick")
            val storagePermissionIntent = Intent(this, StoragePermissionActivity::class.java)
            startActivity(storagePermissionIntent)
        }
        btnCameraPermission.setOnClickListener {
            Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "onClick")
            val cameraPermissionIntent = Intent(this, CameraPermissionActivity::class.java)
            startActivity(cameraPermissionIntent)
        }
        btnFragmentLifecycle.setOnClickListener {
            Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "onClick")
            val fragmentLifecycleInfoIntent = Intent(this, FragmentLifecycleInfoActivity::class.java)
            startActivity(fragmentLifecycleInfoIntent)
        }

    }

    override fun onStart() {
        super.onStart()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onResume() {
        super.onResume()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onPause() {
        super.onPause()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onStop() {
        super.onStop()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onRestart() {
        super.onRestart()
        Utility.printLog(MainActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

}
