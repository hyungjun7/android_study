package com.hyungjun.fc_demo_5_photo_frame

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {

  private val photoList = mutableListOf<Uri>()

  private var currentPosition = 0

  private var timer: Timer? = null

  private val photoImageView: ImageView by lazy {
    findViewById(R.id.photoImageView)
  }

  private val backGroundImageView: ImageView by lazy {
    findViewById(R.id.backgroundImageView)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo_frame)
    getPhotoUriFromIntent()
    startTimer()
  }

  private fun getPhotoUriFromIntent() {
    val size = intent.getIntExtra("photoListSize", 0)
    for (i in 0..size) {
      intent.getStringExtra("photo$i")?.let {
        photoList.add(Uri.parse(it))
      }
    }
  }

  private fun startTimer() {
    timer = timer(period = 5000) {
      runOnUiThread {

        Log.d("photoFrame", "5")
        val current = currentPosition
        val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

        backGroundImageView.setImageURI(photoList[current])

        //투명도
        photoImageView.alpha = 0f
        photoImageView.setImageURI(photoList[next])
        //애니메이션
        photoImageView.animate()
          .alpha(1.0f)
          .setDuration(1000)
          .start()

        currentPosition = next
      }
    }
  }

  override fun onStop() {
    super.onStop()
    timer?.cancel()
  }

  override fun onStart() {
    super.onStart()
    startTimer()
  }

  override fun onDestroy() {
    super.onDestroy()
    timer?.cancel()
  }

}