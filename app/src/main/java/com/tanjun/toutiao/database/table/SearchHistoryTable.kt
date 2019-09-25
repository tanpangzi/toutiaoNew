package com.tanjun.toutiao.database.table

class SearchHistoryTable{
    companion object{
        /** 浏览记录表 */
        val TABLENAME= "SearchHistoryTable"
        val ID = "id"
        val KEYWORD = "keyWord"
        val TIME = "time"

        /** 字段id 数据库操作建立字段对应关系 从0开始*/
        val ID_ID = 0
        val ID_KEYWORKD = 1
        val ID_TIME = 2

        val builder = StringBuilder()
            .append("create table if not exists")
            .append(ID)
            .append(" text auto_increment, ")
            .append(KEYWORD)
            .append(" text primary key, ")
            .append(TIME)
            .append(" text")

    }
}
