package com.hyungjun.fc_demo_7_audio_recoder

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

  private val visualizerView: SoundVisualizerView by lazy {
    findViewById(R.id.visualizerView)
  }

  private val recordButton: RecordButton by lazy {
    findViewById(R.id.recordBtn)
  }

  private val resetButton: Button by lazy {
    findViewById(R.id.resetBtn)
  }

  private var recorder: MediaRecorder? = null

  private var player: MediaPlayer? = null

  private var state: State = State.BEFORE_RECORDING
    set(value) {
      field = value
      resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
      recordButton.updateIconWithState(value)
    }

  private val requiredPermission = arrayOf(
    android.Manifest.permission.RECORD_AUDIO,
    android.Manifest.permission.READ_EXTERNAL_STORAGE
  )

  private val recordingFilePath: String by lazy {
    "${externalCacheDir?.absolutePath}/recording.3gp"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    requestAudioPermission()
    initViews()
    bindViews()
    initVariables()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_PERMISSION && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

    if (!audioRecordPermissionGranted) {
      finish()
    } else {

    }
  }

  private fun requestAudioPermission() {
    requestPermissions(requiredPermission, REQUEST_RECORD_PERMISSION)
  }

  private fun initViews() {
    recordButton.updateIconWithState(state)
  }

  private fun bindViews() {
    visualizerView.onRequestCurrentAmplitude = {
      recorder?.maxAmplitude ?: 0
    }
    recordButton.setOnClickListener {
      when(state) {
        State.BEFORE_RECORDING -> {
          startRecord()
        }
        State.ON_RECORDING -> {
          stopRecord()
        }
        State.AFTER_RECORDING -> {
          startPlaying()
        }
        State.ON_PLAYING -> {
          stopPlaying()
        }
      }
    }
    resetButton.setOnClickListener {
      stopPlaying()
      state = State.BEFORE_RECORDING
    }
  }

  private fun initVariables() {
    state = State.BEFORE_RECORDING
  }

  private fun startRecord() {
    recorder = MediaRecorder().apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
      setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
      setOutputFile(recordingFilePath)
      prepare()
    }
    recorder?.start()
    visualizerView.startVisualizer()
    state = State.ON_RECORDING
  }

  private fun startPlaying() {
    player = MediaPlayer().apply {
      setDataSource(recordingFilePath)
      //스트리밍 같은 경우는 prepareAsync 를 사용함 (네트워크 통신)
      prepare()
    }
    player?.start()
    state = State.ON_PLAYING
  }

  private fun stopPlaying() {
    player?.release()
    player = null
    state = State.AFTER_RECORDING
  }

  private fun stopRecord() {
    recorder?.run {
      stop()
      release()
    }
    recorder = null
    visualizerView.stopVisualizer()
    state = State.AFTER_RECORDING
  }

  companion object {
    private const val REQUEST_RECORD_PERMISSION = 200
  }
}