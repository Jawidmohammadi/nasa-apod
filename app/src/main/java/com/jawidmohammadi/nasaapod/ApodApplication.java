package com.jawidmohammadi.nasaapod;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.jawidmohammadi.nasaapod.service.ApodDatabase;

public class ApodApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    ApodDatabase.setContext(this);
  }
}
