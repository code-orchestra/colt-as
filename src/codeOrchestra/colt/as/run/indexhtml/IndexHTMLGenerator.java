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

  private AsProject project;

  public IndexHTMLGenerator(AsProject project) {
    this.project = project;
  }

  public void generate() throws IOException {
    File targetSWFObjectFile = new File(project.getOutputDir(), "swfobject.html");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "swfobject.html"), targetSWFObjectFile, false);
    
    File targetIndexFile = new File(project.getOutputDir(), "index.html");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "index.html"), targetIndexFile, false);
    
    @SuppressWarnings("Convert2Diamond") Map<String, String> replacements = new HashMap<>();
    replacements.put("{SWF_NAME}", project.getProjectBuildSettings().getOutputFilename());
    new TemplateProcessor(targetIndexFile, replacements).process();
  }
  
}
