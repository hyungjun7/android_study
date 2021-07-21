package com.hyungjun.fc_demo_9_firebase_notification

enum class NotificationType(val title: String, val id: Int) {
  NORMAL("일반 알람", 0),
  EXPANDABLE("확장 알람", 1),
  CUSTOM("커스텀 알람", 3)
}