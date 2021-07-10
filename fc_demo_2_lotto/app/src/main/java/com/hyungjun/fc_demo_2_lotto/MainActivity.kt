package com.hyungjun.fc_demo_2_lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

  private var didRun = false

  private val pickNumber = hashSetOf<Int>()

  private val numberTextViewList: List<TextView> by lazy {
    listOf<TextView>(
      findViewById<TextView>(R.id.lotto1),
      findViewById<TextView>(R.id.lotto2),
      findViewById<TextView>(R.id.lotto3),
      findViewById<TextView>(R.id.lotto4),
      findViewById<TextView>(R.id.lotto5),
      findViewById<TextView>(R.id.lotto6)
    )
  }

  private val clearBtn: Button by lazy {
    findViewById<Button>(R.id.clear)
  }

  private val addBtm: Button by lazy {
    findViewById<Button>(R.id.addBtn)
  }

  private val runBtn: Button by lazy {
    findViewById<Button>(R.id.runBtn)
  }

  private val numberPicker: NumberPicker by lazy {
    findViewById<NumberPicker>(R.id.numberPicker)
  }

  private fun initRunBtn() {
    runBtn.setOnClickListener {
      val list = getRandomNumber()
      didRun = true

      list.forEachIndexed { index, i ->
        val textView = numberTextViewList[index]

        textView.text = i.toString()
        textView.isVisible = true

        setNumberBackground(i, textView)
      }
    }
  }

  private fun initClearBtn() {
    clearBtn.setOnClickListener {
      pickNumber.clear()
      numberTextViewList.forEach {
        it.isVisible = false
      }
      didRun = false
    }
  }

  private fun initAddBtm() {
    addBtm.setOnClickListener {
      if (didRun) {
        Toast.makeText(this, "초기화 후 시도해주세요", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (pickNumber.size >= 5) {
        Toast.makeText(this, "5개 까지 선택할 수 있다!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (pickNumber.contains(numberPicker.value)) {
        Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val textView = numberTextViewList[pickNumber.size]
      textView.isVisible = true
      textView.text = numberPicker.value.toString()

      setNumberBackground(numberPicker.value, textView)

      pickNumber.add(numberPicker.value)
    }
  }

  private fun getRandomNumber(): List<Int> {
    val numberList = mutableListOf<Int>().apply {
      for (i in 1..45) {
        if (pickNumber.contains(i)) continue
        this.add(i)
      }
    }
    numberList.shuffle()
    return (pickNumber.toList() + numberList.subList(0, 6 - pickNumber.size)).sorted()
  }

  private fun setNumberBackground(number: Int, textView: TextView) {
    when (number) {
      in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
      in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
      in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
      in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
      else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    numberPicker.minValue = 1
    numberPicker.maxValue = 45

    initRunBtn()
    initAddBtm()
    initClearBtn()
  }
}