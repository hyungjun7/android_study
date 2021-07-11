package com.hyungjun.fc_demo_3_diary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

  private var changePasswordMode = false

  //뷰를 늦게 렌더링하는 이유, 액티비티가 생성되는 시점에 뷰가 렌더링이 안됐기 때문
  // onCreate 가 실행된 이후 뷰에 접근해야함

  private val numberPicker1: NumberPicker by lazy {
    findViewById<NumberPicker>(R.id.numberPicker1).apply {
      minValue = 0
      maxValue = 9
    }
  }

  private val numberPicker2: NumberPicker by lazy {
    findViewById<NumberPicker>(R.id.numberPicker2).apply {
      minValue = 0
      maxValue = 9
    }
  }

  private val numberPicker3: NumberPicker by lazy {
    findViewById<NumberPicker>(R.id.numberPicker3).apply {
      minValue = 0
      maxValue = 9
    }
  }

  private val openBtn: AppCompatButton by lazy {
    findViewById<AppCompatButton>(R.id.openBtn)
  }

  private val changeBtn: AppCompatButton by lazy {
    findViewById<AppCompatButton>(R.id.changeBtn)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    numberPicker1
    numberPicker2
    numberPicker3

    changeBtn.setOnClickListener {
      val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
      if (changePasswordMode) {
        val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

        passwordPreferences.edit {
          putString("password", passwordFromUser)

          //preferences 를 저장하는 방식에는 2가지가 있음, commit, apply
          //commit 은 작업이 끝날 때 까지 UI를 멈추고, apply 는 비동기적으로 처리함
          commit()
        }

        changePasswordMode = false
        changeBtn.setBackgroundColor(Color.BLACK)
      } else {
        val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

        if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
          changePasswordMode = true
          Toast.makeText(this, "변경할 비밀번호 입력", Toast.LENGTH_SHORT).show()
          changeBtn.setBackgroundColor(Color.RED)
        } else {
          showErrorDialog()
        }
      }
    }

    openBtn.setOnClickListener {

      if (changePasswordMode) {
        Toast.makeText(this, "비밀번호 변경 중", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      // 로컬 파일을 사용하여 데이터 저장가능
      val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

      val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

      //값이 없는 경우 defaultValue 를 가져옴
      if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
        startActivity(Intent(this, DiaryActivity::class.java))
      } else {
        showErrorDialog()
      }
    }
  }

  private fun showErrorDialog() {
    AlertDialog.Builder(this)
      .setTitle("실패")
      .setMessage("비밀번호 틀렸어")
      .setPositiveButton("확인") { _, _ -> }
      .create()
      .show()
  }
}