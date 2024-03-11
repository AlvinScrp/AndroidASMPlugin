package com.webuy.autotrack;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.List;

public class AutoTrackUtil {

    public static void before(Object object) {
        Log.d("alvin", "AutoTrack before :" + object.getClass().getName());
    }

    public static void after(Object object) {
        Log.d("alvin", "AutoTrack after:" + object.getClass().getName());
    }

    public static void autoCurrentPage(Fragment fragment) {
        Log.d("alvin", "autoCurrentPage  " + fragment.toString() + "," + Log.getStackTraceString(new IllegalStateException("hehe")));

    }

    public static void autoTrackClick(View v) {
        Log.d("alvin", "autoTrackClick: setOnclick " + Log.getStackTraceString(new IllegalStateException("hehe")));
    }

    public static void autoTrackClickTest() {
        Log.d("alvin", "autoTrackClick: setOnclick " + Log.getStackTraceString(new IllegalStateException("hehe")));
    }

    public static void autoTrackClickString(String str) {
        Log.d("alvin", "autoTrackClick: setOnclick " + Log.getStackTraceString(new IllegalStateException(str)));

    }

    public static void autoTrackClickString2(Object obj, String str) {
        Log.d("alvin", "autoTrackClick: setOnclick " + obj + "," + Log.getStackTraceString(new IllegalStateException(str)));

    }

    public static void autoTrackClickLambda(Object obj, View v) {
        Log.d("alvin", "autoTrackClick: setOnclick " + Log.getStackTraceString(new IllegalStateException("hehe")));
    }


    public static void autoTrackOnViewClick(Object object, View v) {
        Log.d("alvin", "autoTrackClick: onViewClick " + object + "," + v);
    }


    public static void trackDo1(Object obj, int v1, Integer[] v3, List<Integer> v4, List<String> v5) {
        Log.d("alvin", "AutoTrackUtil trackDo1:" + obj.toString() + "," + v1 + "," + v3 + "," + v4.toString() + "," + v5.toString());
    }

    public static void trackDo2(Object obj, int v1, int[] v3, List<Integer> v4, List<String> v5) {
        Log.d("alvin", "trackDo2" + obj.toString() + "," + v1 + "," + v3 + "," + v4.toString() + "," + v5.toString());
    }

    public static void trackDo3(int v1, int[] v3, List<Integer> v4, List<String> v5) {
        Log.d("alvin", "trackDo3:" + v1 + "," + v3 + "," + v4.toString() + "," + v5.toString());
    }
}
