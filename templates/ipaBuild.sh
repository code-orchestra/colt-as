#!/bin/bash

ACOMPC="{FLEX_SDK}/bin/acompc"
ADT="{FLEX_SDK}/bin/adt"

PROV="/Users/someuser/Documents/xCode/someuser_Provisioning.mobileprovision"
CER="/Users/someuser/Documents/xCode/someuser.p12"
PASS="12345"

PLATFORMSDK="/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.1.sdk"

IPANAME=""

$ADT -package -target ipa-debug-interpreter -provisioning-profile $PROV -storetype pkcs12 -keystore $CER -storepass $PASS {APPNAME}.ipa {APPNAME}-app.xml {APPNAME}.swf
$ADT -installApp -platform ios -package {APPNAME}.ipa