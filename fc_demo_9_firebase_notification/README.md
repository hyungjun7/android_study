```kotlin
    val intent = Intent(this, MainActivity::class.java).apply {
      putExtra("notificationType", "${type.title} 타입")
      //기존 액티비티를 쌓는 방법은 스택에 그냥 쌓는 방법(스탠다드)
      //플래그를 singletop 을 주면 같은 걸 안쌓아서 onCreate 가 호출되지 않고 onNewIntent 가 호출된다. (화면 갱신)
      addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
}
```

```kotlin
    //내가 직접 intent를 다루는 게 아닌 다른 누군가에게 위임 가능
    val pendingIntent = PendingIntent.getActivities(this, type.id,
      arrayOf(intent), FLAG_UPDATE_CURRENT)
```

```kotlin
  //사용자가 앱 데이터를 클리어 하거나 하면 토큰이 변경될 수 있음
  //때문에 토큰을 새로 발급해주는 메소드를 오버라이드해서 사용할 필요가 있음
  override fun onNewToken(p0: String) {
    super.onNewToken(p0)
  }
```

```kotlin
    //앤드로이드 8.0 부터는 채널을 만들어서 할당해야함, 그 이하는 오류가 발생할 가능성이 있음
    private fun createNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }
```