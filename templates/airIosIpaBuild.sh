#!/bin/bash

ADT="{air-sdk}/bin/adt"

PROV="{provisioning-profile}"
CER="{keystore}"
PASS="{storepass}"

PLATFORMSDK="/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.1.sdk"

IPANAME=""

OUTPUT_DIR="{OUTPUT_DIR}"

cd "$OUTPUT_DIR"

"$ADT" -package -target ipa-debug-interpreter -provisioning-profile "$PROV" -storetype pkcs12 -keystore "$CER" -storepass "$PASS" "{IPA_FILE}" "{DESCRIPTOR_FILE}" {PACKAGED_FILES}
"$ADT" -installApp -platform ios -package "{IPA_FILE}"
