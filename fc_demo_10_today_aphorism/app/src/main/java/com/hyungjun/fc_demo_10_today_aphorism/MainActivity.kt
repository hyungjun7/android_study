package com.hyungjun.fc_demo_10_today_aphorism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

  private val viewPager: ViewPager2 by lazy {
    findViewById(R.id.viewPager)
  }

  private val progressBar: ProgressBar by lazy {
    findViewById(R.id.progressBar)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initData()
    initViews()
  }

  private fun initViews() {
    viewPager.setPageTransformer { page, position ->
     when {
       position.absoluteValue >= 1f -> {
         page.alpha = 0f
       }
       position.absoluteValue == 0f -> {
         page.alpha = 1f
       }
       else -> {
         page.alpha = 1f - 2 * position.absoluteValue
       }
     }
    }
  }

  //firebase remote config
  //별도의 업데이트 없이 데이터 변경 가능
  private fun initData() {
    val remoteConfig = Firebase.remoteConfig
    remoteConfig.setConfigSettingsAsync(
      remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
      }
    )
    remoteConfig.fetchAndActivate().addOnCompleteListener {
      progressBar.visibility = View.GONE
      if (it.isSuccessful) {
        val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
        val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
        display(quotes, isNameRevealed)
      }
    }
  }

  private fun display(quotes: List<Quote>, isNameRevealed: Boolean) {
    val adapter = QuotesPagerAdapter(quotes, isNameRevealed)
    viewPager.adapter = adapter
    viewPager.setCurrentItem(adapter.itemCount / 2, false)
  }

  private fun parseQuotesJson(json: String): List<Quote> {
    val jsonArray = JSONArray(json)
    var jsonList = emptyList<JSONObject>()
    for (i in 0 until jsonArray.length()) {
      val obj = jsonArray.getJSONObject(i)
      obj?.let {
        jsonList = jsonList + it
      }
    }
    return jsonList.map {
      Quote(it.getString("quote"), it.getString("name"))
    }
  }
}