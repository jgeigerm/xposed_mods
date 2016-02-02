Just run gradle init in the module folder and then gradle build.<br>
If you want to compile the latest XposedBridge, the link in libs is to<br>
../XposedBridge/app/build/intermediates/packaged/release/classes.jar<br>
so just clone the XposedBridge in the root of this project and then run<br>
gradle build. It should work fine. If you have another version of XposedBridge<br>
just replace the symlink in libs.
