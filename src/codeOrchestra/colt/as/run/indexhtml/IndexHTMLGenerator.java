package codeOrchestra.colt.as.run.indexhtml;

import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.templates.TemplateProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class IndexHTMLGenerator {

  public static String generate(AsProject project) throws IOException {
    File targetSWFObjectFile = new File(project.getOutputDir(), "swfobject.js");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "swfobject.js"), targetSWFObjectFile, false);
    
    File targetIndexFile = new File(project.getOutputDir(), "index.html");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "index.html"), targetIndexFile, false);
    
    Map<String, String> replacements = new HashMap<>();
    replacements.put("{SWF_NAME}", project.getProjectBuildSettings().getOutputFilename());
    new TemplateProcessor(targetIndexFile, replacements).process();
    return targetIndexFile.getPath();
  }
  
}
