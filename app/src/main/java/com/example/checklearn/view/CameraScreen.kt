package com.example.checklearn.view

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.checklearn.R
import com.example.checklearn.components.CustomButton
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.CameraViewModel
import kotlin.apply

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel,
    onCameraClick : () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val imageCash = remember { mutableStateOf<Bitmap?>(null) }
    val isPhotoTaking = remember { mutableStateOf(false) }

    //лаунчер для получения разрешений
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(), //контракт на получение нескольких разрешений
        onResult = { permissions->
            permissions.entries.forEach { //перебираем все разрешения
                val isGranted = it.value //есть или нет доступа
                if(isGranted){
                    Log.d("permission","херачь фото все окей")
                } else {
                    Toast.makeText(context,"хрен тебе а не фото", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(Manifest.permission.CAMERA)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black)
    ) {
        if (isPhotoTaking.value && imageCash.value != null) {
            Image(
                bitmap = imageCash.value!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    PreviewView(it).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        scaleType = PreviewView.ScaleType.FIT_CENTER
                    }.also {
                        it.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.Transparent)
                .align(alignment = Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if(!isPhotoTaking.value){
                    CustomButton(
                        color = ButtonDefaults.buttonColors(BlueMainColor),
                        text = "Сфотографировать",
                        textColor = androidx.compose.ui.graphics.Color.White,
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.camera),
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    ) {
                        cameraController.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)
                                    val bitmap = image.toBitmap().toRotates(image.imageInfo.rotationDegrees.toFloat())
                                    imageCash.value = bitmap
                                    isPhotoTaking.value = true
                                    image.close()
                                }
                            }
                        )
                    }
                } else {
                    CustomButton(
                        color = ButtonDefaults.buttonColors(androidx.compose.ui.graphics.Color.White),
                        text = "Повторить",
                        textColor = BlueMainColor,
                        modifier = Modifier.weight(1f)
                    ) {
                        isPhotoTaking.value = false
                        imageCash.value = null
                    }
                    CustomButton(
                        color = ButtonDefaults.buttonColors(BlueMainColor),
                        text = "Подтвердить",
                        textColor = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.weight(1f)
                    ) {
                        imageCash.value?.let {
                            cameraViewModel.saveImage(it)
                            onCameraClick()
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(MyGray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Сфотографируйте задание для проверки",
                    fontSize = 22.sp,
                    color = BlueMainColor
                )
            }
        }
    }
}

fun Bitmap.toRotates(degrees: Float): Bitmap{
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this,0,0,width,height,matrix,true)
}