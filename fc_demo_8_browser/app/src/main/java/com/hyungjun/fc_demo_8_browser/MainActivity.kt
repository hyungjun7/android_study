package com.hyungjun.fc_demo_8_browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

  private val webView: WebView by lazy {
    findViewById(R.id.webView)
  }

  private val progressBar: ContentLoadingProgressBar by lazy {
    findViewById(R.id.progressBar)
  }

  private val refreshLayout: SwipeRefreshLayout by lazy {
    findViewById(R.id.refreshLayout)
  }

  private val addrText: EditText by lazy {
    findViewById(R.id.addrText)
  }

  private val homeBtn: ImageButton by lazy {
    findViewById(R.id.homeBtn)
  }

  private val backBtn: ImageButton by lazy {
    findViewById(R.id.backBtn)
  }

  private val forwardBtn: ImageButton by lazy {
    findViewById(R.id.forwardBtn)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initViews()
    bindViews()
  }

  //키보드의 액션 버튼을 사용하기 위해 imeActions 를 사용함

  //웹뷰로 작동시켰는데 다른 웹브라우저가 실행된다;;
  @SuppressLint("SetJavaScriptEnabled")
  private fun initViews() {
    webView.apply {
      //웹뷰 내에서 작동시키려면 웹뷰를 오버라이딩 해야 함
      webViewClient = WebViewClient()
      webChromeClient = WebChromeClient()
      //안드로이드 내에서 웹에서 작동하는 자바스크립트를 막아놨음
      settings.javaScriptEnabled = true
      loadUrl(HOME)
    }

  }

  //안드로이드에서 뒤로 가기 버튼을 눌렀을 떄
  override fun onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack()
    } else {
      //호출되면 웹뷰가 꺼짐
      super.onBackPressed()
    }
  }

  private fun bindViews() {
    //뷰, 액션 아이디, 이벤트
    addrText.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        val loadUrl = v.text.toString()
        //앞에 네트워크 프로토콜이 붙어 있는지 확인해주는 메소드
        if (URLUtil.isNetworkUrl(loadUrl)) {
          webView.loadUrl(loadUrl)
        } else {
          webView.loadUrl("http://$loadUrl")
        }
      }
      return@setOnEditorActionListener false
    }

    backBtn.setOnClickListener {
      webView.goBack()
    }

    forwardBtn.setOnClickListener {
      webView.goForward()
    }

    homeBtn.setOnClickListener {
      webView.loadUrl(HOME)
    }

    //화면을 아래로 잡아서 리프레시 할 때
    refreshLayout.setOnRefreshListener {
      webView.reload()
    }
  }

  inner class WebViewClient: android.webkit.WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      super.onPageStarted(view, url, favicon)
      progressBar.show()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
      super.onPageFinished(view, url)
      refreshLayout.isRefreshing = false
      progressBar.hide()
      backBtn.isEnabled = webView.canGoBack()
      forwardBtn.isEnabled = webView.canGoForward()
      addrText.setText(url)
    }
  }

  //WebChromeClient 도 있음, 좀 더 브라우저에 가까운 듯
  inner class WebChromeClient: android.webkit.WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
      super.onProgressChanged(view, newProgress)
      progressBar.progress = newProgress
    }
  }

  companion object {
    const val HOME = "https://www.google.com"
  }
}