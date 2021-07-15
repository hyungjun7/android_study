```kotlin
// 로컬 파일을 사용하여 데이터 저장가능
val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
```

```kotlin
val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

passwordPreferences.edit {
  putString("password", passwordFromUser)

  //preferences 를 저장하는 방식에는 2가지가 있음, commit, apply
  //commit 은 작업이 끝날 때 까지 UI를 멈추고, apply 는 비동기적으로 처리함
  commit()
}
```

```kotlin
//뷰를 늦게 렌더링하는 이유, 액티비티가 생성되는 시점에 뷰가 렌더링이 안됐기 때문
// onCreate 가 실행될 때 뷰에 접근해야함

private val numberPicker1: NumberPicker by lazy {
  findViewById<NumberPicker>(R.id.numberPicker1).apply {
    minValue = 0
    maxValue = 9
  }
}
```

```kotlin
private val handler = Handler(Looper.getMainLooper())

val runnable = Runnable {
  getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
    putString("diary", diaryText.text.toString())
    Log.d("diary", "text : ${diaryText.text.toString()}")
  }
}

//handler, 스레드와 스레드를 연결헤주는 기능
diaryText.addTextChangedListener {
  Log.d("diary", "text : $it")
  //기존에 돌고 있는 것이 있다면 없에기
  handler.removeCallbacks(runnable)
  //0.5초 이내에 다른 변화를 감지하지 못한다면 runnable 실행
  handler.postDelayed(runnable, 500)
}
```

