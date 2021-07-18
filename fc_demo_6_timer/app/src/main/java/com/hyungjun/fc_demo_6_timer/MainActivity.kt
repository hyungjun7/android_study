package com.hyungjun.fc_demo_6_timer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

  private val remainMinutesTextView: TextView by lazy {
    findViewById(R.id.remainMinutesTextView)
  }

  private val remainSecondsTextView: TextView by lazy {
    findViewById(R.id.remainSecondsTextView)
  }

  private val seekBar: SeekBar by lazy {
    findViewById(R.id.seekBar)
  }

  private var currentTimer: CountDownTimer? = null

  private val soundPool = SoundPool.Builder().build()

  private var tickSound: Int? = null

  private var endSound: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    bindView()
    initSound()
  }

  override fun onResume() {
    super.onResume()
    soundPool.autoResume()
  }

  override fun onPause() {
    super.onPause()
    //현재 활성화된 모든 사운드 풀을 정지함
    soundPool.autoPause()
  }

  //사운드나 멀티미디어 파일은 메모리에 올라가기 때문에 메모리에 영향이 심함
  override fun onDestroy() {
    super.onDestroy()
    soundPool.release()
  }

  private fun bindView() {
    seekBar.setOnSeekBarChangeListener(
      object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
          if (fromUser) {
            updateRemainTime(progress * 60 * 1000L)
          }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
          stopTimer()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
          seekBar ?: return
          if (seekBar.progress == 0) {
            stopTimer()
          } else startTimer()
        }
      }
    )
  }

  private fun stopTimer() {
    currentTimer?.cancel()
    currentTimer = null
    soundPool.autoPause()
  }

  private fun initSound() {
    endSound = soundPool.load(this, R.raw.end, 1)
    tickSound = soundPool.load(this, R.raw.ticktock, 1)
  }

  private fun createCountDownTimer(initMillis: Long) =
    object : CountDownTimer(initMillis, 1000L) {
      override fun onTick(millisUntilFinished: Long) {
        updateRemainTime(millisUntilFinished)
        updateSeekBar(millisUntilFinished)
      }

      override fun onFinish() {
        completeTimer()
      }
    }

  private fun startTimer() {
    currentTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
    currentTimer?.start()
    tickSound?.let { soundPool.play(it, 1F, 1F, 0, -1, 1F) }
  }

  private fun completeTimer() {
    updateRemainTime(0)
    updateSeekBar(0)

    soundPool.autoPause()

    endSound?.let { soundPool.play(it, 1F, 1F, 0, 0, 1F) }
  }

  private fun updateRemainTime(remainMillis: Long) {
    val remainSeconds = remainMillis / 1000
    remainMinutesTextView.text = "%02d".format(remainSeconds / 60)
    remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
  }

  private fun updateSeekBar(remainMillis: Long) {
    seekBar.progress = (remainMillis / 1000L / 60).toInt()
  }
}