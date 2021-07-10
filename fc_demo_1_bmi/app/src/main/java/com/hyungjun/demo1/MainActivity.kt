package com.hyungjun.demo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

//액티비티는 단일 화면을 나타냄
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val heightEditText: EditText = findViewById(R.id.heightEditText)
    val weightEditText = findViewById<EditText>(R.id.weightEditText)
    val resultButton = findViewById<Button>(R.id.resultButton)

    resultButton.setOnClickListener {
      Log.d("resultButton", "onClick")

      if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
        Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val height = heightEditText.text.toString().toInt()
      val weight = weightEditText.text.toString().toInt()

      Log.d("resultButton", "height=$height weight=$weight")

      //intent 는 해당 액티비티의 데이터를 가지고 있음
      val intent = Intent(this, ResultActivity::class.java)

      //다음 액티비티로 데이터 넘기기
      intent.putExtra("height", height)
      intent.putExtra("weight", weight)

      startActivity(intent)
      //startActivity 가 작동하면 android 로 intent 를 전달하고 manifest 에서 해당 액티비티를 찾으면
      // android 에서 실행할 액티비티로 전달함 액티비티에서 onCreate 함수 실행
    }
  }
}