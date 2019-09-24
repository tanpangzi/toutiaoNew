package com.tanjun.toutiao.database.table

class MediaChannelTable {
    companion object{
        /** 头条号信息表  */
        val TABLENAME = "mediaChannelTable"

        /** 字段 */
        val ID = "id"
        val NAME = "name"
        val AVATAR = "avatar"
        val TYPE = "type"
        val FOLLOWCOUNT = "followCount"
        val DESCTEXT = "desctext"
        var URL = "url"

        /** 字段id数据 */
        val ID_ID = 0
        val ID_NAME = 1
        val ID_AVATAR = 2
        val ID_TYPE = 3
        val ID_FOLLOW_COUNT = 4
        val ID_DESCTEXT = 5
        val ID_URL = 6

        var builder = StringBuilder()
            .append("create table if not exists")
            .append(TABLENAME)
            .append("(")
            .append(ID)
            .append(" text primary key, ")
            .append(NAME).append(" text, ")
            .append(AVATAR).append(" text, ")
            .append(TYPE).append(" text, ")
            .append(FOLLOWCOUNT).append(" text, ")
            .append(DESCTEXT).append(" text, ")
            .append(URL).append(" text)")
    }

}
