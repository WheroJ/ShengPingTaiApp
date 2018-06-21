package com.cmcc.pp.util

import com.cmcc.pp.entity.UserEntity
import org.litepal.crud.DataSupport
import java.util.*

/**
 * Created by shopping on 2018/1/23 11:30.
 * https://github.com/wheroj
 */
object TokenUtils {

    /**
     * 提前三分钟判断openId是否过期
     */
    private const val advanceRemindDate: Int = 3 * 60

    /**
     * 提前三分钟判断token是否过期
     */
    fun isTokenGoingOutOfDate(): Boolean {
        val userEntity = getCurrentLoginUser()
        return if (userEntity != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = userEntity.tokenSaveTime
            println("curentSaveTime=" + calendar.timeInMillis)
            calendar.add(Calendar.SECOND, userEntity.expireIn - advanceRemindDate)
            println("outDateSaveTime=" + calendar.timeInMillis)
            println("currentSystemTime=" + System.currentTimeMillis())
            calendar.timeInMillis <= System.currentTimeMillis()
        } else true
    }

    /**
     * token已经过期
     */
    fun isTokenOutOfDate(): Boolean {
        val userEntity = getCurrentLoginUser()
        return if (userEntity != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = userEntity.tokenSaveTime
            println("curentSaveTime=" + calendar.timeInMillis)
            calendar.add(Calendar.SECOND, userEntity.expireIn)
            println("outDateSaveTime=" + calendar.timeInMillis)
            println("currentSystemTime=" + System.currentTimeMillis())
            calendar.timeInMillis <= System.currentTimeMillis()
        } else true
    }

    fun getToken(): String? {
        return getCurrentLoginUser()?.openId
    }

    fun setAllUserLogout() {
        val userEntities = DataSupport.findAll(UserEntity::class.java)
        if (!userEntities.isEmpty()) {
            for (i in userEntities.indices) {
                userEntities[i].currentLoginIn = 1
            }
        }
    }

    fun getCurrentLoginUser(): UserEntity? {
        var userEntity: UserEntity? = null
        val userEntities = DataSupport.findAll(UserEntity::class.java)
        if (!userEntities.isEmpty()) {
            for (i in userEntities.indices) {
                if (userEntities[i].currentLoginIn == 2) {
                    userEntity = userEntities[i]
                    break
                }
            }
        }
        return userEntity
    }
}