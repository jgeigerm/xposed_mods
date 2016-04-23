package de.havefuninsi.wwf;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XC_MethodHook;

public class Hooks implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.zynga.words"))
            return;
        XposedBridge.log("Loaded wwf cheater, you cheater ;)");
        findAndHookMethod("com.zynga.words.d.a.a", lpparam.classLoader, "a", String.class, XC_MethodReplacement.returnConstant(true));
    }
}
