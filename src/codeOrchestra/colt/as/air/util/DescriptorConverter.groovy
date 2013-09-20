package codeOrchestra.colt.as.air.util

import codeOrchestra.colt.as.model.beans.air.descriptor.DescriptorModel
import codeOrchestra.util.FileUtils
import groovy.xml.DOMBuilder
import groovy.xml.XmlUtil
import groovy.xml.dom.DOMCategory
import org.w3c.dom.Document

/**
 * @author Dima Kruk
 */
class DescriptorConverter {

    static void makeTemplate(File file, DescriptorModel model, File outFile ) {
        String fileContent = FileUtils.read(file)
        fileContent = fileContent.replaceAll("\n\t\t<!-- Note: In Flash Builder, the SWF reference is set automatically. -->", "")
        //for iOS
        fileContent = fileContent.replaceAll("<!-- <iPhone> -->", "<iPhone>")
        fileContent = fileContent.replaceAll("<!-- <InfoAdditions>", "<InfoAdditions>")
        fileContent = fileContent.replaceAll("</InfoAdditions> -->", "</InfoAdditions>")
        fileContent = fileContent.replaceAll("<!-- </iPhone> -->", "</iPhone>")
        //end for iOS

        Document document = DOMBuilder.parse(new StringReader(fileContent))
        def application = document.documentElement
        use(DOMCategory) {
            def nodes = application.'**'
            nodes.each{
                switch (it.name()){
                    case "id":
                        it.value = model.id
                        break
                    case "filename":
                        it.value = model.fileName
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
                    case "InfoAdditions":
//                        println "it. = ${it.item(1).data}"
//                        it.item(1).data = ""
                        break
                }
            }
        }

        def writer = new StringWriter()
        writer.write(XmlUtil.serialize(application))
        FileWriter fileWriter = new FileWriter(outFile)
        fileWriter.write(writer.toString())
        fileWriter.close()
    }
}
