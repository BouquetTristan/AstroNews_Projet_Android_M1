package com.astronews.core.communicationBridge;

import android.util.Log;

import com.unity3d.player.UnityPlayer;

import rx.subjects.BehaviorSubject;


public class CommunicationBridge {
    static public BehaviorSubject<String> bs = BehaviorSubject.create("");

    public CommunicationBridge() {}

    public void callFromAndroidToUnity(String gameObject, String methodFromUnity, String arg) {
        UnityPlayer.UnitySendMessage(gameObject, methodFromUnity, arg);
    }

    public void unityCallback(String data) {
        callFromUnityToAndroid(data);
    }

    public static void callFromUnityToAndroid(String data) {
        // Log.d("SUBJECT", data);
        bs.onNext(data);
    }
}
