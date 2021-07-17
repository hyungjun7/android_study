```kotlin
    //네트워크 작업의 경우 다른 스레드를 생성해서 처리함
    Thread(Runnable {
      db.historyDao().getAll().reversed().forEach {
        //새로 생성한 다른 스레드에서 UI 스레드를 가져오기
        runOnUiThread {
          val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
          historyView.findViewById<TextView>(R.id.expressionText).text = it.expression
          historyView.findViewById<TextView>(R.id.resultText).text = it.result

          historyLinearLayout.addView(historyView)
        }
      }
    }).start()
```

```kotlin
    //확장 함수,, 해당 객체에 원래 있던 것 처럼 사용할 수 있음
    fun String.isNumber(): Boolean {
      return try {
        this.toBigInteger()
        true
      } catch (e: NumberFormatException) {
        false
      }
    }
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- ripple : 눌렀을 때 눌렸다! 를 가지고 있는 UI -->
<!-- stroke : 테두리 가지고 있는 UI -->
<!-- corners : 양악 수술 UI -->
<!-- solid : 색칠 공부 UI -->
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
  android:color="@color/btnOnPress">
  <item>
    <shape android:shape="rectangle">
      <solid android:color="@color/gray" />
      <corners android:radius="100dp" />
      <stroke android:width="1dp"
        android:color="@color/btnOnPress"/>
    </shape>
  </item>
</ripple>
```

```xml
      <!-- stateListAnimator 라는 속성이 있어서 색을 바꿔도 보라색에서 변하지 않는다. -->
      <!-- -> AppCompatButton 을 사용하면 된다 -->
      <!-- onClick 을 함수명으로 binding 할 수 있음 -->
      <!-- clickable 클릭 못하게 하기 -->

      <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/clearBtn"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_margin="7dp"
        android:background="@drawable/btn_backgroud"
        android:onClick="clearButtonClicked"
        android:stateListAnimator="@null"
        android:text="C"
        android:textSize="24sp" />

      <androidx.appcompat.widget.AppCompatButton
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_margin="7dp"
        android:background="@drawable/btn_backgroud"
        android:clickable="false"
        android:enabled="false"
        android:stateListAnimator="@null"
        android:text="()"
        android:textColor="@color/green"
        android:textSize="24sp" />
```

```kotlin
  //Room 에서는 아래와 같은 방식으로 쿼리를 해서 데이터를 가져올 수 있음
  @Dao
  interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>
  }
```
