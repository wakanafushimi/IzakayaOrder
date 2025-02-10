package com.pi12a082_wakanafushimi.kadai10

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "menu.sqlite"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE menu_items(menu_name TEXT)")
        db?.execSQL("CREATE TABLE order_history(menu_name TEXT, createdAt DATETIME DEFAULT CURRENT_TIMESTAMP)")

        val menuNames = listOf("ビール","焼酎","日本酒","レモンサワー","ハイボール","カクテル","ワイン","梅酒")

        db?.beginTransaction()
        try {
            menuNames.forEach { menuName ->
                val value = ContentValues().apply {
                    put("menu_name", menuName)
                }
                db?.insert("menu_items", null, value)
            }
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS order_history")
        db?.execSQL("DROP TABLE IF EXISTS menu_items")
        onCreate(db)
    }

    // メニュー一覧を取得
    fun getMenuList(): List<String> {
        val menuList = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT menu_name FROM menu_items", null)
        cursor.use {
            while (it.moveToNext()) {
                menuList.add(it.getString(0))
            }
        }
        return menuList
    }

    // 注文履歴を追加
    fun addOrder(menuName: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("menu_name", menuName)
        }
        db.insert("order_history", null, values)
    }

    // 注文履歴を取得（降順）
    fun getOrderHistory(): List<String> {
        val orderList = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT menu_name FROM order_history ORDER BY createdAt DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                orderList.add(it.getString(0))
            }
        }
        return orderList
    }

    // アプリを終了・再起動したら履歴を削除する
    fun clearOrderHistory() {
        val db = writableDatabase
        db.execSQL("DELETE FROM order_history")
    }
}
