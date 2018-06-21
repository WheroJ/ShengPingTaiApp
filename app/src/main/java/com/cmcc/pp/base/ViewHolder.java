package com.cmcc.pp.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zrspring.libv2.annotation.Injector;


/**
 *
 * Created by Administrator on 2016/4/21.
 */
public abstract class  ViewHolder<T> extends RecyclerView.ViewHolder{

    public ViewHolder(View itemView) {
        super(itemView);
        Injector.inject(this, itemView);
    }

    public abstract void  setData(T data, int position);
}
