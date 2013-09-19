@echo off

set ADT={AIR_SDK}/bin/adt

set CER={keystore}
set PASS={storepass}

set OUTPUT_DIR={OUTPUT_DIR}

cd /d "%OUTPUT_DIR%"

@echo on
cmd /C " "%ADT%" -package -target apk-captive-runtime -storetype pkcs12 -keystore "%CER%" -storepass "%PASS%" {APK_FILE} {DESCRIPTOR_FILE} {PACKAGED_FILES} "
cmd /C " "%ADT%" -installApp -platform android -package {APK_FILE} "