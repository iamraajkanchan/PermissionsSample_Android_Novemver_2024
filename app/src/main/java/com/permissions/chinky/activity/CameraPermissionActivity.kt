package com.permissions.chinky.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.ImageView
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

class CameraPermissionActivity : AppCompatActivity() {

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityCamera: CameraDevice
    private lateinit var textureViewCameraPreview: TextureView
    private lateinit var surface: Surface
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var activityCameraCaptureSession: CameraCaptureSession

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
        textureViewCameraPreview = findViewById(R.id.textureViewCameraPreview)
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
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    takePicture()
                } else {
                    showPermissionRationaleForCamera()
                }
            }
        btnTakeAPicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicture()
            }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showPermissionRationaleForCamera()
            }
            else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        textureViewCameraPreview.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surfaceTexture: SurfaceTexture,
                    startPoint: Int,
                    endPoint: Int
                ) {
                    openCamera()
                }

                override fun onSurfaceTextureSizeChanged(
                    surfaceTexture: SurfaceTexture,
                    startPoint: Int,
                    endPoint: Int
                ) {
                    // Do Nothing
                }

                override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                    // Do Nothing
                }
            }
    }

    private fun takePicture() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) == null) {
            stopCameraPreview()
            cameraLauncher.launch(cameraIntent)
        }
    }

    private fun showPermissionRationaleForCamera() {
        AlertDialog.Builder(this).setTitle("Read Permission Needed")
            .setMessage("This app needs permission to click photographs.")
            .setPositiveButton("OK") { _, _ -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            .setNegativeButton("Cancel", null).create().show()
    }

    private fun openCamera() {
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                return
            }
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    activityCamera = camera
                    startCameraPreview()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                }
            }, Handler(Looper.getMainLooper()))
        } catch (cae: CameraAccessException) {
            Utility.printExceptionLog(
                CameraPermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                Logger.getLogger(Utility.APPLICATION_TAG),
                cae
            )
        } catch (e: Exception) {
            Utility.printExceptionLog(
                CameraPermissionActivity::class.java,
                Thread.currentThread().stackTrace[2],
                Logger.getLogger(Utility.APPLICATION_TAG),
                e
            )
        }
    }

    private fun startCameraPreview() {
        val surfaceTexture = textureViewCameraPreview.surfaceTexture
        surfaceTexture?.setDefaultBufferSize(
            textureViewCameraPreview.width,
            textureViewCameraPreview.height
        )
        surface = Surface(surfaceTexture)
        captureRequestBuilder = activityCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)
        activityCamera.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                activityCameraCaptureSession = cameraCaptureSession
                updatePreview()
            }

            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                activityCameraCaptureSession.abortCaptures()
            }
        }, Handler(Looper.getMainLooper()))
    }

    private fun updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        activityCameraCaptureSession.setRepeatingRequest(
            captureRequestBuilder.build(),
            object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    Utility.printLog(
                        CameraPermissionActivity::class.java,
                        Thread.currentThread().stackTrace[2],
                        "request :: $request"
                    )
                    Utility.printLog(
                        CameraPermissionActivity::class.java,
                        Thread.currentThread().stackTrace[2],
                        "result :: $result"
                    )
                    super.onCaptureCompleted(session, request, result)
                }
            },
            null
        )
    }

    private fun stopCameraPreview() {
        activityCameraCaptureSession.close()
        activityCamera.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (surface.isValid) {
            surface.release()
        }
    }
}