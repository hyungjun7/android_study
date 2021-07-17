package com.hyungjun.fc_demo_5_photo_frame

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

  private val addPhoto: Button by lazy {
    findViewById<Button>(R.id.addPhoto)
  }

  val startPhotoFrame: Button by lazy {
    findViewById<Button>(R.id.startPhotoFrame)
  }

  private val imageUriList: MutableList<Uri> = mutableListOf()

  private val imageViewList: List<ImageView> by lazy {
    mutableListOf<ImageView>().apply {
      add(findViewById<ImageView>(R.id.imgView1))
      add(findViewById<ImageView>(R.id.imgView2))
      add(findViewById<ImageView>(R.id.imgView3))
      add(findViewById<ImageView>(R.id.imgView4))
      add(findViewById<ImageView>(R.id.imgView5))
      add(findViewById<ImageView>(R.id.imgView6))
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initAddPhotoBtn()
    initStartPhotoFrame()
  }

  private fun initAddPhotoBtn() {
    addPhoto.setOnClickListener {
      when {
        //권한 체크
        ContextCompat.checkSelfPermission(
          this,
          android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
          navigatePhotos()
        }
        //교육용 팝업이 필요하다면
        shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
          showPermissionContextPopup()
        }
        else -> {
          //필요없다면, requestCode를 onRequestPermissionResult에 전달함
          requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        }
      }
    }
  }

  //requestPermissions 콜백 함수
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      1000 -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          navigatePhotos()
        } else {
          Toast.makeText(this, "권한 거부", Toast.LENGTH_SHORT).show()
        }
      }
      else -> {

      }
    }
  }

  private fun showPermissionContextPopup() {
    AlertDialog.Builder(this)
      .setTitle("권한이 필요합니다")
      .setMessage("전자액자 앱에서 사진을 불러오기 위해 권한이 필요함")
      .setPositiveButton("동의보감") { _, _ ->
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
      }
      .setNegativeButton("취소") { _, _ -> }
      .create()
      .show()
  }

  private fun initStartPhotoFrame() {
    startPhotoFrame.setOnClickListener {
      val intent = Intent(this, PhotoFrameActivity::class.java)
      imageUriList.forEachIndexed { index, uri ->
        intent.putExtra("photo$index", uri.toString())
      }
      intent.putExtra("photoListSize", imageUriList.size)
      startActivity(intent)
    }
  }

  private fun navigatePhotos() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    launcher.launch(intent)
  }

  private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    Log.d("$it.resultCode", "asdasd")
    if (it.resultCode == RESULT_OK) {
      val imageUri = it.data?.data
      if (imageUri != null) {
        if (imageUriList.size == 6) {
          AlertDialog.Builder(this)
            .setTitle("사진 꽉 참")
            .setMessage("돌아가")
            .setPositiveButton("동의보감") { _, _ ->

            }
            .create()
            .show()
          Toast.makeText(this, "사진 꽉 찼어", Toast.LENGTH_SHORT).show()
          return@registerForActivityResult
        }
        imageUriList.add(imageUri)
        imageViewList[imageUriList.size - 1].setImageURI(imageUri)
      } else {
        Toast.makeText(this, "사진을 못가져왔어", Toast.LENGTH_SHORT).show()
        return@registerForActivityResult
      }
    } else {
      Toast.makeText(this, "사진을 못가져왔어", Toast.LENGTH_SHORT).show()
      return@registerForActivityResult
    }
  }
}