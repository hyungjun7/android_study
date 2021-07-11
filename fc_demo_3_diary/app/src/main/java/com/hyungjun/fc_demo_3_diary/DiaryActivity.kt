package com.hyungjun.fc_demo_3_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

  //메인 스레드에 연결된 스레드
  private val handler = Handler(Looper.getMainLooper())

  private val diaryText: EditText by lazy {
    findViewById<EditText>(R.id.diaryText)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_diary)

    val detailPref = getSharedPreferences("diary", Context.MODE_PRIVATE)

    diaryText.setText(detailPref.getString("diary", ""))

    val runnable = Runnable {
      getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
        putString("diary", diaryText.text.toString())
        Log.d("diary", "text : ${diaryText.text.toString()}")
      }
    }

    //handler, 스레드와 레드를 연결헤주는 기능
    diaryText.addTextChangedListener {
      Log.d("diary", "text : $it")
      //기존에 돌고 있는 것이 있다면 없에기
      handler.removeCallbacks(runnable)
      //0.5초 이내에 다른 변화를 감지하지 못한다면 runnable 실행
      handler.postDelayed(runnable, 500)
    }
  }
}