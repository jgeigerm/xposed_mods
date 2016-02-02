package de.havefuninsi.noswift;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;

import android.content.Intent;
import android.view.KeyEvent;
import android.content.Context;
import android.app.AndroidAppHelper;

import java.lang.reflect.Field;

public class Hooks implements IXposedHookLoadPackage {

    private Context ctx;
    private String trackName = "";

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.google.android.music"))
            return;
        XposedBridge.log("Loaded NoSwift");
        findAndHookMethod("com.google.android.music.playback.TrackInfo", lpparam.classLoader, "getArtistName", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                ctx = AndroidAppHelper.currentApplication().createPackageContext("de.havefuninsi.noswift", Context.CONTEXT_IGNORE_SECURITY);
                String result = (String)param.getResult();
                Class<?> c = param.thisObject.getClass();
                Field f = c.getDeclaredField("mTrackName");
                f.setAccessible(true);
                String tempTrackName = (String)f.get(param.thisObject);
                if (result.toLowerCase().contains("taylor swift") && !trackName.equals(tempTrackName)){
                    trackName = tempTrackName;
                    XposedBridge.log("NoSwift: " + result);
                    Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    synchronized (this) {
                                i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
                                ctx.sendOrderedBroadcast(i, null);

                                i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
                                ctx.sendOrderedBroadcast(i, null);
                    }
                }
            }
        });
    }
}
