package com.hyungjun.fc_demo_4_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.hyungjun.fc_demo_4_calculator.model.History
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

  private val expressionTextView: TextView by lazy {
    findViewById<TextView>(R.id.expressionText)
  }

  private val resultTextView: TextView by lazy {
    findViewById<TextView>(R.id.resultText)
  }

  private val historyLayout: View by lazy {
    findViewById<View>(R.id.historyLayout)
  }

  private lateinit var db:AppDatabase

  private val historyLinearLayout: LinearLayout by lazy {
    findViewById<LinearLayout>(R.id.historyLinearLayout)
  }

  private var isOperator = false

  private var hasOperator = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    db = Room.databaseBuilder(
      applicationContext,
      AppDatabase::class.java,
      "historyDB").build()
  }

  fun buttonClicked(view: View) {
    when (view.id) {
      R.id.btn0 -> numberBtnClicked("0")
      R.id.btn1 -> numberBtnClicked("1")
      R.id.btn2 -> numberBtnClicked("2")
      R.id.btn3 -> numberBtnClicked("3")
      R.id.btn4 -> numberBtnClicked("4")
      R.id.btn5 -> numberBtnClicked("5")
      R.id.btn6 -> numberBtnClicked("6")
      R.id.btn7 -> numberBtnClicked("7")
      R.id.btn8 -> numberBtnClicked("8")
      R.id.btn9 -> numberBtnClicked("9")
      R.id.btnPlus -> operatorBtnClicked("+")
      R.id.btnMinus -> operatorBtnClicked("-")
      R.id.btnMulti -> operatorBtnClicked("*")
      R.id.btnDivider -> operatorBtnClicked("/")
      R.id.btnModulo -> operatorBtnClicked("%")
    }
  }

  private fun operatorBtnClicked(operator: String) {
    if (expressionTextView.text.isEmpty()) {
      return
    }

    when {
      isOperator -> {
        val text = expressionTextView.text.toString()
        expressionTextView.text = text.dropLast(1) + operator
      }
      hasOperator -> {
        Toast.makeText(this, "???????????? ??? ??????", Toast.LENGTH_SHORT).show()
        return
      }
      else -> {
        expressionTextView.append(" $operator")
      }
    }

    val ssb = SpannableStringBuilder(expressionTextView.text)
    ssb.setSpan(
      ForegroundColorSpan(getColor(R.color.green)),
        expressionTextView.text.length - 1,
        expressionTextView.text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    expressionTextView.text = ssb

    isOperator = true
    hasOperator = true
  }

  private fun numberBtnClicked(number: String) {
    if (isOperator) {
      expressionTextView.append(" ")
    }
    isOperator = false
    val expressionText = expressionTextView.text.split(" ")
    if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
      Toast.makeText(this, "15?????? ?????? ????????? ??? ??????.", Toast.LENGTH_SHORT).show()
      return
    } else if (expressionText.last().isEmpty() && number == "0") {
      Toast.makeText(this, "0 ??? ?????? ??? ??????.", Toast.LENGTH_SHORT).show()
      return
    }

    expressionTextView.append(number)
    resultTextView.text = calculateExpression()
  }

  fun resultButtonClicked(view: View) {
    val expressionTexts = expressionTextView.text.split(" ")

    if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
      return
    }

    if (expressionTexts.size != 3 && hasOperator) {
      Toast.makeText(this, "?????? ????????? ?????????", Toast.LENGTH_SHORT).show()
      return
    }

    if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
      Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show()
      return
    }

    val expressionText = expressionTextView.text.toString()
    val resultText = calculateExpression()


    //???????????? ????????? ?????? ?????? ???????????? ???????????? ?????????, context switch ?????????
    Thread(Runnable {
      db.historyDao().insertHistory(History(null, expressionText, resultText))
    }).start()

    resultTextView.text = ""
    expressionTextView.text = resultText

    isOperator = false
    hasOperator = false
  }

  private fun calculateExpression(): String {
    val expressionTexts = expressionTextView.text.split(" ")
    if (hasOperator.not() || expressionTexts.size != 3) {
      return ""
    } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
      return ""
    } else {
      val exp1 = expressionTexts[0].toBigInteger()
      val exp2 = expressionTexts[2].toBigInteger()
      val op = expressionTexts[1]

      return when(op) {
        "+" -> (exp1 + exp2).toString()
          "-" -> (exp1 - exp2).toString()
          "*" -> (exp1 * exp2).toString()
          "/" -> (exp1 / exp2).toString()
          "%" -> (exp1 % exp2).toString()
          else -> ""
      }
    }
  }

  fun historyButtonClicked(view: View) {
    historyLayout.isVisible = true
    //????????? ?????? ?????? ?????? ????????????
    historyLinearLayout.removeAllViews()
    Thread(Runnable {
      db.historyDao().getAll().reversed().forEach {
        //?????? ????????? ?????? ??????????????? UI ???????????? ????????????
        runOnUiThread {
          val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
          historyView.findViewById<TextView>(R.id.expressionText).text = it.expression
          historyView.findViewById<TextView>(R.id.resultText).text = it.result

          historyLinearLayout.addView(historyView)
        }
      }
    }).start()
  }

  fun historyClearBtnClicked(view: View) {
    historyLinearLayout.removeAllViews()
    Thread(Runnable {
      db.historyDao().deleteAll()
    }).start()
  }

  fun closeHistoryBtnClicked(view: View) {
    historyLayout.isVisible = false
  }

  fun clearButtonClicked(view: View) {
    expressionTextView.text = ""
    resultTextView.text = ""
    isOperator = false
    hasOperator = false
  }
}

//?????? ??????,, ?????? ????????? ?????? ?????? ??? ?????? ????????? ??? ??????
fun String.isNumber(): Boolean {
  return try {
    this.toBigInteger()
    true
  } catch (e: NumberFormatException) {
    false
  }
}