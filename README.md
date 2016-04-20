Just create local.properties with your sdk.dir in the module folder and then run gradle build.<br>
You can also just set the ANDROID_HOME environmental variable to the sdk dir and that will work too<br>
If you want to compile the latest XposedBridge, the link in libs is to<br>
../XposedBridge/app/build/intermediates/packaged/release/classes.jar<br>
so you can git clone --recursive, cd into the XposedBridge folder, and then run <br>
gradle jarReleaseClasses. It should work fine. If you have another version of XposedBridge<br>
just replace the symlink in libs.
