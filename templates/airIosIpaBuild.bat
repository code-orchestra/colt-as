@echo off

set ADT={air-sdk}/bin/adt

set PROV={provisioning-profile}
set CER={keystore}
set PASS={storepass}

set IPANAME=

set OUTPUT_DIR={OUTPUT_DIR}

cd /d "%OUTPUT_DIR%"

@echo on
cmd /C "%ADT%" -package -target ipa-debug-interpreter -provisioning-profile "%PROV%" -storetype pkcs12 -keystore "%CER%" -storepass "%PASS%" {IPA_FILE} {DESCRIPTOR_FILE} {PACKAGED_FILES}
cmd /C "%ADT%" -installApp -platform ios -package {IPA_FILE}