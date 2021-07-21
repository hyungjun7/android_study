package com.hyungjun.fc_demo_9_firebase_notification

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

  private val resultView: TextView by lazy {
    findViewById(R.id.resultView)
  }

  private val tokenView: TextView by lazy {
    findViewById(R.id.tokenView)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initViews()
    updateResult()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    updateResult(true)
  }

  private fun initViews() {
    FirebaseMessaging.getInstance().token
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          tokenView.text = task.result
        }
      }
  }

  @SuppressLint("SetTextI18n")
  private fun updateResult(isNewIntent: Boolean = false) {
    resultView.text = (intent.getStringExtra("notificationType") ?: "앱 ") + if (isNewIntent) {
      "갱신"
    } else {
      "실행"
    }
  }
}