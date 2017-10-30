# salento-driver-android #
Driver based on soot for extracting API sequences from Java bytecode and Android.

# Compilation and setup #
NOTE: The soot version distributed (2.5.0) has been compiled with JDK "1.7.0_80" and is compatible with JRE (build 1.7.0_80-b15).

1. Setup two environment variables as follows
    `export SALENTO_ANDROID_HOME=/path/to/salento-driver-android`
    `export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home`

    Note: there should be a "jre" folder within `$JAVA_HOME`

2. Compile the driver by running `ant` in `$SALENTO_ANDROID_HOME`

3. (Optional) Add `$SALENTO_ANDROID_HOME` to `$PATH` for the "driver" script

4. Run the driver by doing:
    
    `driver <app>.apk <optional soot args>`