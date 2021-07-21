package com.hyungjun.fc_demo_9_firebase_notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hyungjun.fc_demo_9_firebase_notification.NotificationType.*

//앤드로이드 8.0 부터는 채널을 만들어서 할당해야함, 그 이하는 오류가 발생할 가능성이 있음
class MyFireBaseMessageService: FirebaseMessagingService() {

  //사용자가 앱 데이터를 클리어 하거나 하면 토큰이 변경될 수 있음
  //때문에 토큰을 새로 발급해주는 메소드를 오버라이드해서 사용할 필요가 있음
  override fun onNewToken(p0: String) {
    super.onNewToken(p0)
  }

  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)

    createNotiChannel()

    val type = p0.data["type"]
      ?.let { valueOf(it) }
    val title = p0.data["title"]
    val message = p0.data["message"]

    type ?: return



    NotificationManagerCompat.from(this)
      .notify(type.id, createNoti(title, message, type))
  }

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

  private fun createNoti(title: String?, message: String?, type: NotificationType): Notification {
    val intent = Intent(this, MainActivity::class.java).apply {
      putExtra("notificationType", "${type.title} 타입")
      //기존 액티비티를 쌓는 방법은 스택에 그냥 쌓는 방법(스탠다드)
      //플래그를 singletop 을 주면 같은 걸 안쌓아서 onCreate 가 호출되지 않고 onNewIntent 가 호출된다. (화면 갱신)
      addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    //내가 직접 intent를 다루는 게 아닌 다른 누군가에게 위임 가능
    val pendingIntent = PendingIntent.getActivities(this, type.id,
      arrayOf(intent), FLAG_UPDATE_CURRENT)

    val notiBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notifications)
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent(pendingIntent)
      //해당 속성 true 안하면 클릭해도 안없어진다고 함..
      .setAutoCancel(true)

    when(type) {
      NORMAL -> Unit
      EXPANDABLE -> {
        notiBuilder
          .setStyle(NotificationCompat.BigTextStyle()
            .bigText("asdsdfffsdfasdgdfgkjhsdkgldjkbsfhjsdhgfnhbjdfsgjfnmasdkjgfshnfksdljgfbaskfgnsbdjhfkjadsgnkjdf")
          )
      }
      CUSTOM -> {
        notiBuilder
          .setStyle(NotificationCompat.DecoratedCustomViewStyle())
          .setCustomContentView(
            RemoteViews(
              packageName,
              R.layout.view_custom_noti
            ).apply {
              setTextViewText(R.id.title, title)
              setTextViewText(R.id.message, message)
            }
          )
      }
    }
    return notiBuilder.build()
  }

  companion object {
    private const val CHANNEL_NAME = "emoji_party"
    private const val CHANNEL_DESCRIPTION = "emoji_party_channel"
    private const val CHANNEL_ID = "channel_id"
  }
}