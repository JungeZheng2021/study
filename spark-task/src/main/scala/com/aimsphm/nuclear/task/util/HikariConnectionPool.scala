package com.aimsphm.nuclear.task.util

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

object HikariConnectionPool {
  @transient private var instance: HikariDataSource = _ //单例，不用序列化

  def getDataSourceInstance: HikariDataSource = {
    if (instance == null) {
      try {
        val config = new HikariConfig
        config.setJdbcUrl("jdbc:mysql://192.168.16.28:3306/nuclear_tw?serverTimezone=GMT%2B8")
        //config.setJdbcUrl("jdbc:mysql://localhost:3306/nuclear_phm?serverTimezone=GMT%2B8")
        config.setUsername("root")
        config.setPassword("aims2016")
        //config.setPassword("abcd1234")
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        //config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.setMaximumPoolSize(100)
        instance = new HikariDataSource(config)
      } catch {
        case ex: Exception => ex.printStackTrace()
      }
    }
    instance
  }
}
