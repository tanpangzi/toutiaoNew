package com.tanjun.toutiao.database.table

class NewsChannelTable {
    companion object{
        /** 新闻频道信息 */
        val TABLENAME = "NewsChannelTable"

        /** 字段 */
        val ID = "id"
        val NAME = "name"
        val IS_ENABLE = "isEnable"
        val POSITION = "position"

        /** 字段 */
        val ID_ID = 0
        val ID_NAME = 1
        val ID_ISENABLE = 2
        val ID_POSTION = 3

        val builder = StringBuilder()
            .append("create table if not exists")
            .append(TABLENAME)
            .append("(")
            .append(ID)
            .append(" text primary key, ")
            .append(NAME)
            .append(" text, ")
            .append(IS_ENABLE)
            .append(" text default '1', ")
            .append(POSITION)
            .append(" text")
    }

}
