package com.tanjun.toutiao.database.table

class NewsHistoryChannel{
    companion object{
        /** 浏览记录表 */
        val TABLENAME = "NewsHistoryTable"

        /** 字段部分 */
        val news_id = "id"
        val news_title = "title"
        val news_abstract = "abstractX"
        val news_source = "source"
        val news_comment_count = "comment_count"
        val news_behot_time = "behot_time"



    }
}
