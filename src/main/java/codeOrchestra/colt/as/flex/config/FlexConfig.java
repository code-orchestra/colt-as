package codeOrchestra.colt.as.flex.config;

import codeOrchestra.colt.core.build.BuildException;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Flex compiler configuration.
 *
 * @author Alexander Eliseyev
 */
public class FlexConfig {

  public static final boolean DEFAULT_LIBRARY_APPEND = true;
  
  public static final String CONFIG_FILE_POSTFIX = "_flex_config.xml";

  private static final String CONFIG_NAMESPACE = "http://www.adobe.com/2006/flex-config";

  private static final String FLEX_CONFIG_ELEMENT = "flex-config";
  private static final String TARGET_PLAYER_ELEMENT = "target-player";
  private static final String COMPILER_ELEMENT = "compiler";
  private static final String DEBUG_ELEMENT = "debug";
  private static final String SOURCE_PATH_ELEMENT = "source-path";
  private static final String PATH_ELEMENT_ELEMENT = "path-element";
  private static final String LIBRARY_PATH_ELEMENT = "library-path";
  private static final String EXTERNAL_LIBRARY_PATH_ELEMENT = "external-library-path";
  private static final String APPEND_ATTRIBUTE = "append";
  private static final String FILE_SPECS_ELEMENT = "file-specs";
  private static final String OUTPUT_ELEMENT = "output";
  private static final String INCLUDE_CLASSES_ELEMENT = "include-classes";
  private static final String CLASS_ELEMENT = "class";
  private static final String LOCALE_ELEMENT = "locale";
  private static final String LOCALE_ELEMENT_ELEMENT = "locale-element";
  private static final String RSL_ELEMENT = "static-link-runtime-shared-libraries";
  public static final String LINK_REPORT_ELEMENT = "link-report";
  public static final String LOAD_EXTERNS_ELEMENT = "load-externs";
  private static final String STRICT_ELEMENT = "strict";
  private static final String INCLUDE_INHERITANCE_ONLY_ELEMENT = "include-inheritance-dependencies-only";
  private static final String VERIFY_DIGESTS_ELEMENT = "verify-digests";
  public static final String WARNINGS_ELEMENT = "warnings";
  public static final String INCREMENTAL_ELEMENT = "incremental";
  private static final String OMIT_TRACE_STATEMENTS = "omit-trace-statements";

  private List<String> customMetadataList = new ArrayList<>();
  private List<String> sourcePaths = new ArrayList<>();
  private List<String> includeClasses = new ArrayList<>();
  private List<FileWrapper> includeFiles = new ArrayList<>();

  private List<String> fileSpecs = new ArrayList<>();

  private List<String> libraryPaths = new ArrayList<>();
  private List<String> externalLibraryPaths = new ArrayList<>();

  List<String> locales;

  public String getOutputPath() {
    return outputPath;
  }

  private String outputPath;
  private String targetPlayerVersion;
  private boolean debug;
  private boolean runtimeSharedLibrary;
  private boolean doIncludeFiles;
  private boolean omitTraceStatements;
  private String linkReportFilePath;
  private String loadExternsFilePath;

  // These are set to non-default values only in live mode
  private boolean strict = true;
  private boolean warnings = true;
  private boolean verifyDigests = false;
  private boolean incremental = true;
  
  private boolean allowCompression = false;
  private boolean allowOptimization = false;  

  private boolean includeInheritanceDependenciesOnly;

  public void setLibrary(boolean library) {
    isLibrary = library;
  }

  private boolean isLibrary;
  private static final String INCLUDE_FILE_ELEMENT = "include-file";
  private static final String METADATA_NAME_ELEMENT = "name";
  private static final String FILE_NAME_ELEMENT = METADATA_NAME_ELEMENT;
  private static final String PATH_ELEMENT = "path";
  private static final String KEEP_AS3_METADATA_ELEMENT = "keep-as3-metadata";


  public FlexConfig(boolean isLibrary, boolean doIncludeFiles) {
    this.isLibrary = isLibrary;
    this.doIncludeFiles = doIncludeFiles;
  }

  public File saveToFile(String filename) throws BuildException {
    Document flexConfigDocument = toXML();

    try {
      return XMLUtils.saveToFile(filename, flexConfigDocument);
    } catch (TransformerException e) {
      throw new BuildException("Can't transform flex config XML document");
    }
  }
  
  public void setAllowCompression(boolean allowCompression) {
    this.allowCompression = allowCompression;
  }

  public void setAllowOptimization(boolean allowOptimization) {
    this.allowOptimization = allowOptimization;
  }

  public void addClass(String classFQName) {
    if (!isLibrary) {
      throw new IllegalStateException("Can't add a class name to a non-library config");
    }
    includeClasses.add(classFQName);
  }

  public void setIncremental(boolean incremental) {
    this.incremental = incremental;
  }

  public void setVerifyDigests(boolean verifyDigests) {
    this.verifyDigests = verifyDigests;
  }

  public void setWarnings(boolean warnings) {
    this.warnings = warnings;
  }

  public void setIncludeInheritanceDependenciesOnly(boolean includeInheritanceDependenciesOnly) {
    this.includeInheritanceDependenciesOnly = includeInheritanceDependenciesOnly;
  }

  public void setStrict(boolean strict) {
    this.strict = strict;
  }

  public void setOmitTraceStatements(boolean omitTraceStatements) {
    this.omitTraceStatements = omitTraceStatements;
  }

  public void addCustomMetadata(String metatag) {
    customMetadataList.add(metatag);
  }

  public void addIncludedFile(String path, String relativePath) {
    includeFiles.add(new FileWrapper(path, relativePath));
  }

  public void addSourcePath(String sourcePath) {
    sourcePaths.add(sourcePath);
  }

  public List<String> getSourcePaths() {
    return sourcePaths;
  }

  public void addLibraryPath(String libraryPath) {
    libraryPaths.add(libraryPath);
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  public void setLoadExternsFilePath(String loadExternsFilePath) {
    this.loadExternsFilePath = loadExternsFilePath;
  }

  public void setLinkReportFilePath(String linkReportFilePath) {
    this.linkReportFilePath = linkReportFilePath;
  }

  public void setLocale(String localeStr) {
    if (localeStr != null) {
        //noinspection Convert2Diamond
        locales = new ArrayList<>();
      String[] localesArray = localeStr.split(",");
      for (String splittedLocale : localesArray) {
        String trimmedLocale = splittedLocale.trim();
        if (!StringUtils.isEmpty(trimmedLocale)) {
          locales.add(trimmedLocale);
        }
      }
    }
  }

  public void addFileSpecPathElement(String pathElement) {
    if (isLibrary) {
      throw new IllegalStateException("Can't add a file spec to a library config");
    }
    fileSpecs.add(pathElement);
  }

  public void setRuntimeSharedLibrary(boolean runtimeSharedLibrary) {
    this.runtimeSharedLibrary = runtimeSharedLibrary;
  }

  public void setTargetPlayerVersion(String targetPlayerVersion) {
    this.targetPlayerVersion = targetPlayerVersion;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isEqualToConfig(File file) {
    Document thisDocument;
    try {
      thisDocument = toXML();
    } catch (BuildException e) {
      return false;
    }

      Document externalDocument = null;
      try {
          externalDocument = XMLUtils.fileToDOM(file);
      } catch (Throwable t) {
          return false;
      }
      if (externalDocument == null) {
      return false;
    }

    return XMLUtils.documentToString(thisDocument).equals(XMLUtils.documentToString(externalDocument));
  }

  public Document toXML() throws BuildException {
    Document document = XMLUtils.createDocument();
    if (document == null) {
      throw new BuildException("Can't create config XML document");
    }

    // Root
    Element rootElement = document.createElementNS(CONFIG_NAMESPACE, FLEX_CONFIG_ELEMENT);
    document.appendChild(rootElement);

    // Player version
    Element targetPlayerElement = (Element) rootElement.appendChild(document.createElement(TARGET_PLAYER_ELEMENT));
    targetPlayerElement.setTextContent(targetPlayerVersion);

    // Include inheritance dependencies only
    if (includeInheritanceDependenciesOnly) {
      Element includeInheritanceOnlyElement = (Element) rootElement.appendChild(document.createElement(INCLUDE_INHERITANCE_ONLY_ELEMENT));
      includeInheritanceOnlyElement.setTextContent(Boolean.valueOf(includeInheritanceDependenciesOnly).toString());
    }

    // Compiler settings
    Element compilerElement = (Element) rootElement.appendChild(document.createElement(COMPILER_ELEMENT));
    {
      // 'Is debug' flag
      if (debug) {
        Element debugElement = (Element) compilerElement.appendChild(document.createElement(DEBUG_ELEMENT));
        debugElement.setTextContent(Boolean.valueOf(debug).toString());
      }

      // 'Strict' flag
      if (!strict) {
        Element strictElement = (Element) compilerElement.appendChild(document.createElement(STRICT_ELEMENT));
        strictElement.setTextContent(Boolean.valueOf(strict).toString());
      }
      // 'Incremental' flag
      if (incremental) {
        Element incrementalElement = (Element) compilerElement.appendChild(document.createElement(INCREMENTAL_ELEMENT));
        incrementalElement.setTextContent(Boolean.valueOf(incremental).toString());
      }
      
      // 'compression' flag
      Element compressionElement = (Element) compilerElement.appendChild(document.createElement("compress"));
      compressionElement.setTextContent(Boolean.valueOf(allowCompression).toString());        

      // 'compression' flag
      Element optimizeElement = (Element) compilerElement.appendChild(document.createElement("optimize"));
      optimizeElement.setTextContent(Boolean.valueOf(allowOptimization).toString());
      
      // 'Omit trace statements' flag
      Element omitTraceStatementsElement = (Element) compilerElement.appendChild(document.createElement(OMIT_TRACE_STATEMENTS));
      omitTraceStatementsElement.setTextContent(Boolean.valueOf(omitTraceStatements).toString());
      
      // Locale
      if (locales != null) {
        Element localeElement = (Element) compilerElement.appendChild(document.createElement(LOCALE_ELEMENT));
        for (String locale : locales) {
          Element localeElementElement = (Element) localeElement.appendChild(document.createElement(LOCALE_ELEMENT_ELEMENT));
          localeElementElement.setTextContent(locale);
        }
      }

      // Source paths
      Element sourcePathElement = (Element) compilerElement.appendChild(document.createElement(SOURCE_PATH_ELEMENT));
      for (String sourcePath : sourcePaths) {
        Element pathElementElement = (Element) sourcePathElement.appendChild(document.createElement(PATH_ELEMENT_ELEMENT));
        pathElementElement.setTextContent(sourcePath);
      }

      // Library paths
      if (!libraryPaths.isEmpty()) {
        Element libraryPathElement = (Element) compilerElement.appendChild(document.createElement(LIBRARY_PATH_ELEMENT));
        libraryPathElement.setAttribute(APPEND_ATTRIBUTE, Boolean.valueOf(DEFAULT_LIBRARY_APPEND).toString());
        for (String libraryPath : libraryPaths) {
          Element pathElementElement = (Element) libraryPathElement.appendChild(document.createElement(PATH_ELEMENT_ELEMENT));
          pathElementElement.setTextContent(libraryPath);
        }
      }

      // External Library paths
      if (!externalLibraryPaths.isEmpty()) {
        Element externalLibraryPathElement = (Element) compilerElement.appendChild(document.createElement(EXTERNAL_LIBRARY_PATH_ELEMENT));
        for (String libraryPath : externalLibraryPaths) {
          Element pathElementElement = (Element) externalLibraryPathElement.appendChild(document.createElement(PATH_ELEMENT_ELEMENT));
          pathElementElement.setTextContent(libraryPath);
        }
      }

      // Custom metadata
      if (!customMetadataList.isEmpty()) {
        Element keepMetadataElement = (Element) compilerElement.appendChild(document.createElement(KEEP_AS3_METADATA_ELEMENT));
        for (String customMetadata : customMetadataList) {
          Element metadataNameElement = (Element) keepMetadataElement.appendChild(document.createElement(METADATA_NAME_ELEMENT));
          metadataNameElement.setTextContent(customMetadata);
        }
      }
    }

    // Output a XML-formatted report of all definitions linked into the application
    if (linkReportFilePath != null) {
      Element linkReportElement = (Element) rootElement.appendChild(document.createElement(LINK_REPORT_ELEMENT));
      linkReportElement.setTextContent(linkReportFilePath);
    }

    // Load an XML file containing <def>, <pre>, and <ext> symbols to omit from linking when building a SWF
    if (loadExternsFilePath != null) {
      Element loadExternsElement = (Element) rootElement.appendChild(document.createElement(LOAD_EXTERNS_ELEMENT));
      loadExternsElement.setTextContent(loadExternsFilePath);
    }

    // RSL
    Boolean staticLinkRuntimeSharedLibraries = !runtimeSharedLibrary;
    Element rslElement = (Element) rootElement.appendChild(document.createElement(RSL_ELEMENT));
    rslElement.setTextContent(staticLinkRuntimeSharedLibraries.toString());

    // 'verify-digests' flag
    if (!verifyDigests) {
      Element verifyDigestsElement = (Element) rootElement.appendChild(document.createElement(VERIFY_DIGESTS_ELEMENT));
      verifyDigestsElement.setTextContent(Boolean.valueOf(verifyDigests).toString());
    }
    // 'warnings' flag
    if (!warnings) {
      Element warningsElement = (Element) rootElement.appendChild(document.createElement(WARNINGS_ELEMENT));
      warningsElement.setTextContent(Boolean.valueOf(warnings).toString());
    }


    // Include-classes / file-specs
    if (isLibrary) {
      // Include-classes
      Element includeClassesElement = (Element) rootElement.appendChild(document.createElement(INCLUDE_CLASSES_ELEMENT));
      for (String clazz : includeClasses) {
        Element classElement = (Element) includeClassesElement.appendChild(document.createElement(CLASS_ELEMENT));
        classElement.setTextContent(clazz);
      }

      // Include files
      if (doIncludeFiles) {
        for (FileWrapper fileWrapper : includeFiles) {
          Element includeFileElement = (Element) rootElement.appendChild(document.createElement(INCLUDE_FILE_ELEMENT));

          Element fileNameElement = (Element) includeFileElement.appendChild(document.createElement(FILE_NAME_ELEMENT));
          fileNameElement.setTextContent(fileWrapper.getRelativePath());
          Element filePathElement = (Element) includeFileElement.appendChild(document.createElement(PATH_ELEMENT));
          filePathElement.setTextContent(fileWrapper.getFilePath());
        }
      }
    } else {
      // File-specs
      Element fileSpecsElement = (Element) rootElement.appendChild(document.createElement(FILE_SPECS_ELEMENT));

      // File specs path elements
      for (String fileSpecPathElement : fileSpecs) {
        Element fileSpecPathElementElement = (Element) fileSpecsElement.appendChild(document.createElement(PATH_ELEMENT_ELEMENT));
        fileSpecPathElementElement.setTextContent(fileSpecPathElement);
      }
    }

    // Output element
    Element outputElement = (Element) rootElement.appendChild(document.createElement(OUTPUT_ELEMENT));
    outputElement.setTextContent(outputPath);

    return document;
  }

  public void addExternalLibraryPath(String swcPath) {
    if (!externalLibraryPaths.contains(swcPath)) {
      externalLibraryPaths.add(swcPath);
    }
  }

  public static class FileWrapper {
    private String filePath;
    private String relativePath;

    public FileWrapper(String filePath, String relativePath) {
      this.filePath = filePath;
      this.relativePath = relativePath;
    }

    public String getFilePath() {
      return filePath;
    }

    public String getRelativePath() {
      return relativePath;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      FileWrapper that = (FileWrapper) o;

      if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
      if (relativePath != null ? !relativePath.equals(that.relativePath) : that.relativePath != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = filePath != null ? filePath.hashCode() : 0;
      result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
      return result;
    }
  }


}
