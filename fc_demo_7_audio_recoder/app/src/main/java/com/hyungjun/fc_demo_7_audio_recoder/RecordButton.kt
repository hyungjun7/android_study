package com.hyungjun.fc_demo_7_audio_recoder

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton

//안드로이드는 1년 마다 새로운 버전이 새로 나오는데, 기존에 사용하던 것들을 현재 나오는 버전에서도
//호환이 가능하도록 하는게 AppCompat

//xml 에서는 자동으로 AppCompat 으로 wrapping 해줌
class RecordButton(
  context: Context,
  attrs: AttributeSet
): AppCompatImageButton(context, attrs) {
  fun updateIconWithState(state: State) {
    when(state) {
      State.BEFORE_RECORDING -> {
        setImageResource(R.drawable.ic_record)
      }
      State.ON_RECORDING -> {
        setImageResource(R.drawable.ic_stop)
      }
      State.AFTER_RECORDING -> {
        setImageResource(R.drawable.ic_play)
      }
      State.ON_PLAYING -> {
        setImageResource(R.drawable.ic_stop)
      }
    }
  }
}