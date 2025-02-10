package com.pi12a082_wakanafushimi.kadai10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LifeCycle", "ResultActivityのonCreateが実行")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val dbHelper = DatabaseHelper(this)

        val menuName = intent.getStringExtra("menu_name") ?: "メニュー不明"

        val menuTextView: TextView = findViewById(R.id.menuText) // IDを修正
        menuTextView.text = menuName

        // 注文履歴に追加
        dbHelper.addOrder(menuName)

        // 「メニューに戻る」ボタン
        val backButton: Button = findViewById(R.id.resultButton)
        backButton.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}
