package com.pi12a082_wakanafushimi.kadai10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var orderListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LifeCycle", "MainActivityのonCreateが実行")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        dbHelper.clearOrderHistory()
        val menuList = dbHelper.getMenuList()

        val buttonIds = listOf(
            R.id.menu_1, R.id.menu_2, R.id.menu_3, R.id.menu_4,
            R.id.menu_5, R.id.menu_6, R.id.menu_7, R.id.menu_8
        )

        for (i in buttonIds.indices) {
            val button: Button = findViewById(buttonIds[i])
            if (i < menuList.size) {
                button.text = menuList[i]
                button.setOnClickListener {
                    startActivity(Intent(this, ResultActivity::class.java).apply {
                        putExtra("menu_name", menuList[i])
                    })
                }
            } else {
                button.isEnabled = false
            }
        }

        orderListView = findViewById(R.id.orderHistory)
        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val orderHistory = dbHelper.getOrderHistory()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderHistory)
        orderListView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadOrderHistory() // 戻ったときに履歴を更新
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.clearOrderHistory() // アプリ再起動時に履歴を削除
    }
}
