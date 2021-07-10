```kotlin
      //액티비티는 단일 화면을 나타냄
      class MainActivity : AppCompatActivity()
```

```kotlin
      //해당 액티비티가 실행될 때 실행하는 함수
      override fun onCreate(savedInstanceState: Bundle?)
```

```kotlin
      //intent 는 해당 액티비티의 데이터를 가지고 있음
      val intent = Intent(this, ResultActivity::class.java)

      //다음 액티비티로 데이터 넘기기
      intent.putExtra("height", height)
      intent.putExtra("weight", weight)

      startActivity(intent)
      //startActivity 가 작동하면 android 로 intent 를 전달하고 manifest 에서 해당 액티비티를 찾으면
      // android 에서 실행할 액티비티로 전달함 액티비티에서 onCreate 함수 실행
```
      
 ```xml
  <!-- match-parent 는 상위 컴포넌트에 맞춰줌 -->
  <!-- padding 은 안쪽 여백, margin 은 바깥 -->
  <!-- sp 는 핸드폰에서 설정한 글자크기에 반응함, dp는 고정 -->
  <!-- res/values/colors 에 색상을 미리 정의할 수 있음 -->
  <!-- res/values/string 에 문자열을 미리 정의할 수 있음 -->
  <TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@string/height"
    android:textColor="@color/custom_black"
    android:textSize="20sp"
    android:textStyle="bold" />
```
