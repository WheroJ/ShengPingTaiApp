package com.zrspring.libv2.base;

/**
 * Created by developer on 2017/12/11.
 */

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class ActivityCollector {
    //声明一个List集
    public static List<Activity> activities=new ArrayList<Activity>();

    //将activity添加到List集中
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    //将某一个Activity移除
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //结束所有添加进来的的Activity
    public static void finishAll(){
        for(Activity activity:activities){
            //如果activity没有销毁，那么销毁
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
