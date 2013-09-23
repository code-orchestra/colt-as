package codeOrchestra.colt.as.air.util

import codeOrchestra.colt.as.model.beans.air.AndroidAirModel
import codeOrchestra.colt.as.model.beans.air.descriptor.AndroidDescriptorModel
import codeOrchestra.colt.as.model.beans.air.descriptor.DescriptorModel
import codeOrchestra.colt.as.model.beans.air.descriptor.IOSDescriptorModel
import codeOrchestra.util.FileUtils
import groovy.xml.DOMBuilder
import groovy.xml.XmlUtil
import groovy.xml.dom.DOMCategory
import org.w3c.dom.Document
import codeOrchestra.colt.as.air.ui.descriptor.Devices
import codeOrchestra.colt.as.util.ASPathUtils

/**
 * @author Dima Kruk
 */
class DescriptorConverter {
    static File getBaseTemplate() {
        return new File(ASPathUtils.flexSDKPath, "templates/air/descriptor-template.xml")
    }
    static void makeTemplate(DescriptorModel model, File outFile ) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void makeTemplateForIOS(DescriptorModel model, IOSDescriptorModel iosModel, File outFile) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)
        out = replaceForIOS(out, iosModel)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void makeTemplateForAndroid(DescriptorModel model, AndroidDescriptorModel androidModel, File outFile) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)
        out = replaceForAndroid(out, androidModel)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void makeTemplateForDesktop(DescriptorModel model, File outFile) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model, true)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static String replaceBase(String source, DescriptorModel model, boolean forDesktop = false) {
        String result = source.replaceAll("\n\t\t<!-- Note: In Flash Builder, the SWF reference is set automatically. -->", "")
        if (!forDesktop) {
            result = result.replaceAll("<!-- <autoOrients></autoOrients> -->", "<autoOrients></autoOrients>")
            result = result.replaceAll("<!-- <fullScreen></fullScreen> -->", "<fullScreen></fullScreen>")
        } else {
            result = result.replaceAll("<!-- <visible></visible> -->", "<visible>true</visible>")
        }

        Document document = DOMBuilder.parse(new StringReader(result))
        def application = document.documentElement
        use(DOMCategory) {
            def nodes = application.'**'
            nodes.each{
                switch (it.name()){
                    case "id":
                        it.value = model.id
                        break
                    case "filename":
                        it.value = model.name
                        break
                    case "name":
                        it.value = model.name
                        break
                    case "versionNumber":
                        it.value = model.version
                        break
                    case "content":
                        it.value = "SWF file name is set automatically at compile time"
                        break
                    case "autoOrients":
                        if (!forDesktop) {
                            it.value = model.autoOrient
                        }
                        break
                    case "fullScreen":
                        if (!forDesktop) {
                            it.value = model.fullScreen
                        }
                        break
                }
            }
        }

        result = XmlUtil.serialize(application)

        return result
    }

    static String replaceForIOS(String source, IOSDescriptorModel model) {
        String result = source.replaceAll("<!-- <iPhone> -->", "<iPhone>")
        result = result.replaceAll("<!-- <InfoAdditions>", "<InfoAdditions>")
        result = result.replaceAll("</InfoAdditions> -->", "</InfoAdditions>")
        result = result.replaceAll("<!-- </iPhone> -->", "</iPhone>")
        println "model.highResolution = $model.highResolution"
        if (model.highResolution) {
            result = result.replaceAll("<!-- <requestedDisplayResolution></requestedDisplayResolution> -->", "<requestedDisplayResolution>high</requestedDisplayResolution>")
        }
        Devices devices = Devices.valueOf(model.devices)
        switch (devices) {
            case Devices.ALL:
                break
            case Devices.IPHONE:
                result = result.replaceAll("<string>2</string>", "<!--<string>2</string>-->")
                break
            case Devices.IPAD:
                result = result.replaceAll("<string>1</string>", "<!--<string>1</string>-->")
                break
        }

        return result
    }

    static String replaceForAndroid(String source, AndroidDescriptorModel model) {
        String result = source.replaceAll("<!--<android> -->", "<android>")
        result = result.replaceAll("<!--\t<manifestAdditions>", "\t<manifestAdditions>")
        result = result.replaceAll("<manifest android:installLocation=\"auto\">\n" +
                "\t\t\t\t<uses-permission android:name=\"android.permission.INTERNET\"/>\n" +
                "\t\t\t\t<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>\n" +
                "\t\t\t\t<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\"/>\n" +
                "\t\t\t\t<uses-feature android:required=\"true\" android:name=\"android.hardware.touchscreen.multitouch\"/>\n" +
                "\t\t\t\t<application android:enabled=\"true\">\n" +
                "\t\t\t\t\t<activity android:excludeFromRecents=\"false\">\n" +
                "\t\t\t\t\t\t<intent-filter>\n" +
                "\t\t\t\t\t\t\t<action android:name=\"android.intent.action.MAIN\"/>\n" +
                "\t\t\t\t\t\t\t<category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                "\t\t\t\t\t\t</intent-filter>\n" +
                "\t\t\t\t\t</activity>\n" +
                "\t\t\t\t</application>\n" +
                "            </manifest>",
        "<manifest android:installLocation=\"auto\">\n" +
                "\t\t\t\t<uses-permission android:name=\"android.permission.INTERNET\"/>\n" +
                "\t\t\t\t<!--<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>-->\n" +
                "\t\t\t\t<!--<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\"/>-->\n" +
                "\t\t\t\t<!--<uses-feature android:required=\"true\" android:name=\"android.hardware.touchscreen.multitouch\"/>-->\n" +
                "\t\t\t\t<!--<application android:enabled=\"true\">\n" +
                "\t\t\t\t\t<activity android:excludeFromRecents=\"false\">\n" +
                "\t\t\t\t\t\t<intent-filter>\n" +
                "\t\t\t\t\t\t\t<action android:name=\"android.intent.action.MAIN\"/>\n" +
                "\t\t\t\t\t\t\t<category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                "\t\t\t\t\t\t</intent-filter>\n" +
                "\t\t\t\t\t</activity>\n" +
                "\t\t\t\t</application>-->\n" +
                "            </manifest>")
        result = result.replaceAll("</manifestAdditions> -->", "</manifestAdditions>")
        result = result.replaceAll("<!-- </android> -->", "<android>")

        return result
    }

    static String replaceAfterCompile(String source, String swf) {
        Document document = DOMBuilder.parse(new StringReader(source))
        def application = document.documentElement
        use(DOMCategory) {
            application.'**'.content.each {it.value = swf}
        }

        return XmlUtil.serialize(application)
    }

    static void afterCompileReplace(File template, File outFile, String swf) {
        String fileContent = FileUtils.read(template)

        String out = replaceAfterCompile(fileContent, swf)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void afterCompileReplace(DescriptorModel model, File outFile, String swf) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model, true)
        out = replaceAfterCompile(out, swf)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void afterCompileReplace(DescriptorModel model, IOSDescriptorModel iosModel, File outFile, String swf) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)
        out = replaceForIOS(out, iosModel)
        out = replaceAfterCompile(out, swf)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void afterCompileReplace(DescriptorModel model, AndroidDescriptorModel andriondModel, File outFile, String swf) {
        File file = getBaseTemplate()
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)
        out = replaceForAndroid(out, andriondModel)
        out = replaceAfterCompile(out, swf)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }
}
