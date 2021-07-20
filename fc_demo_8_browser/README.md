###웹 브라우저

```kotlin
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
```

```kotlin
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

    //화면을 아래로 잡아서 리프레시 할 때
    refreshLayout.setOnRefreshListener {
      webView.reload()
    }
  }
```

```kotlin
  //WebChromeClient 도 있음, 좀 더 브라우저에 가까운 듯
  inner class WebChromeClient: android.webkit.WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
      super.onProgressChanged(view, newProgress)
      progressBar.progress = newProgress
    }
  }
```