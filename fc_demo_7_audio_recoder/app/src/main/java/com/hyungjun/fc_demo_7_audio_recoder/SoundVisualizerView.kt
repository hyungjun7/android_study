package com.hyungjun.fc_demo_7_audio_recoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SoundVisualizerView(
  context: Context,
  attrs: AttributeSet? = null
): View(context, attrs) {

  //곡선이 부드럽게? 그려짐
  private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = context.getColor(R.color.purple_500)
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
  }

  private var drawingWidth = 0
  private var drawingHeight = 0
  private var drawingAmplitudes = (0..10).map { Random.nextInt(Short.MAX_VALUE.toInt()) }
  private var isReplaying = false

  var onRequestCurrentAmplitude: (() -> Int)? = null

  private val visualizerRepeatAction = object : Runnable {
    override fun run() {
      val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
      drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
      //드로잉시 해당 메소드를 실행하지 않으면 데이터만 쌓이고 뷰는 안바뀜
      invalidate()
      handler?.postDelayed(this, ACTION_INTERVAL)
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    drawingWidth = w
    drawingHeight = h
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    canvas ?: return

    val centerY = drawingHeight / 2f
    var offsetX = drawingWidth.toFloat()

    drawingAmplitudes.forEach { item ->
      val lineLength = item / MAX_AMPLITUDE * drawingHeight * 0.8f
      offsetX -= LINE_SPACE
      if (offsetX < 0) return@forEach
      canvas.drawLine(
        offsetX,
        centerY - lineLength / 2f,
        offsetX,
        centerY + lineLength / 2f,
        amplitudePaint
      )
    }

  }

  fun startVisualizer(isReplaying: Boolean) {
    this.isReplaying = isReplaying
    handler?.post(visualizerRepeatAction)
  }

  fun stopVisualizer() {
    handler?.removeCallbacks(visualizerRepeatAction)
  }

  companion object {
    private const val LINE_WIDTH = 10f
    private const val LINE_SPACE = 15f
    private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
    private const val ACTION_INTERVAL = 20L
  }
}