##android lifecycle
onCreate() : 액티비티가 launch 되었을 때 실행

onStart()

onResume() : onPause 상태에서 현재 액티비티로 전환 된 경우 돌아오는 곳

onPause() : 현재 액티비티에서 다른 액티비티를 사용하면 작동함

onStop() : 더 이상 액티비티가 보이지 않을 때 작동함, 다시 액티비티가 작동한다면 onRestart() -> onStart() 를 거침, 
    메모리가 부족할 경우 액티비티를 메모리에서 지워버림, 재 실행시 onCreate() 로 돌아감

onDestroy() : 액티비티가 메모리에서 사라질 때 동작함

```kotlin
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
```

```kotlin
  //requestPermissions 콜백 함수
  // requestPermissions 에서 전달한 값들이 담김
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
```

```kotlin
  private fun navigatePhotos() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    launcher.launch(intent)
  }

  //사용자 기기 내의 데이터를 가져오는 함수, onCreate() 에 연결되어 있어야 함
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
```