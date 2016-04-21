package de.havefuninsi.Yak;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;

import javax.net.ssl.SSLSession;
import java.util.List;

public class YakLog implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.yik.yak"))
            return;
        findAndHookMethod("java.lang.String", lpparam.classLoader, "getBytes", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable{
                XposedBridge.log((String)param.thisObject);
            }
        });
        //disable certificate pinning
        findAndHookMethod("nC", lpparam.classLoader, "a", String.class, List.class, XC_MethodReplacement.DO_NOTHING);
        /*findAndHookMethod("nC", lpparam.classLoader, "a", String.class, List.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log((String)param.args[0]);
                return null;
            }
        });*/

    }
}
