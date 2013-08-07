#!/bin/bash

ADT="{air-sdk}/bin/adt"

CER="{keystore}"
PASS="{storepass}"

OUTPUT_DIR="{OUTPUT_DIR}"

cd "$OUTPUT_DIR"

"$ADT" -package -target apk-captive-runtime -storetype pkcs12 -keystore "$CER" -storepass "$PASS" "{APK_FILE}" "{DESCRIPTOR_FILE}" {PACKAGED_FILES}
"$ADT" -installApp -platform android -package "{APK_FILE}"