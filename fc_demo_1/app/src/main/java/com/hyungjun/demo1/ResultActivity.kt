package com.hyungjun.demo1

import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class ResultActivity: AppCompatActivity() {

  //해당 액티비티가 실행될 때 실행하는 함수
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    val height = intent.getIntExtra("height", 0)
    val weight = intent.getIntExtra("weight", 0)

    Log.d("result", "height=$height weight=$weight")

    val bmi = weight / (height / 100.0).pow(2.0)
    val resultText = when {
      bmi >= 35.0 -> "고릴라"
      bmi >= 30.0 -> "차우"
      bmi >= 25.0 -> "멧돼지"
      bmi >= 23.0 -> "돼지"
      bmi >= 18.5 -> "사람"
      else -> "멸치"
    }

    val resultValueTextView = findViewById<TextView>(R.id.bmiResultTextView)
    val resultStringTextView = findViewById<TextView>(R.id.resultTextView)

    resultValueTextView.text = bmi.toString()
    resultStringTextView.text = resultText
  }
}