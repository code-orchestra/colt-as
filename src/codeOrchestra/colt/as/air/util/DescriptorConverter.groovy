package codeOrchestra.colt.as.air.util

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
    static void makeTemplate(DescriptorModel model, File outFile ) {
        File file = new File(ASPathUtils.flexSDKPath, "templates/air/descriptor-template.xml")
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)

        def writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static void makeTemplateForIOS(DescriptorModel model, IOSDescriptorModel iosModel, File outFile) {
        File file = new File(ASPathUtils.flexSDKPath, "templates/air/descriptor-template.xml")
        String fileContent = FileUtils.read(file)

        String out = replaceBase(fileContent, model)
        out = replaceForIOS(out, iosModel)

        StringWriter writer = new StringWriter()
        writer.write(out)
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }

    static String replaceBase(String source, DescriptorModel model) {
        String result = source.replaceAll("\n\t\t<!-- Note: In Flash Builder, the SWF reference is set automatically. -->", "")
        result = result.replaceAll("<!-- <autoOrients></autoOrients> -->", "<autoOrients></autoOrients>")
        result = result.replaceAll("<!-- <fullScreen></fullScreen> -->", "<fullScreen></fullScreen>")

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
                        it.value = model.autoOrient
                        break
                    case "fullScreen":
                        it.value = model.fullScreen
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
}
