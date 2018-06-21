package com.cmcc.pp.entity.transactiondata

/**
 * Created by shopping on 2018/1/22 9:50.
 * https://github.com/wheroj
 */
class RefreshViewObservable {
    private var loadCount: Int = 0
    private var status: Int = -1
    private var observers: ArrayList<RefreshViewObserver> = ArrayList()

    fun addOberver(observer: RefreshViewObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: RefreshViewObserver) {
        observers.remove(observer)
    }

    fun addLoadCount() {
        loadCount++
    }

    /**
     * {@link RefreshLinearLayout}
     */
    fun changeStatus(status: Int) {
        if (this.status != status) {
            this.status = status
        }

        if(loadCount == 0) {
            notifyAllObserver()
        }
    }

    fun subLoadCount() {
        loadCount--
        if (loadCount == 0) {
            notifyAllObserver()
        }
    }

    private fun notifyAllObserver() {
        for (i in observers.indices) {
            observers[i].changeViewStatus(status)
        }
    }
}