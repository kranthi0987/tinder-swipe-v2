package dependency.greendao.test.tinder.directional.app;

import android.app.Application;
import android.content.Context;



public class MyApplication extends Application {

    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }
    public static Context getContext() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

}
