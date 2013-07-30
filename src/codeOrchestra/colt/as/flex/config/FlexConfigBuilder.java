package codeOrchestra.colt.as.flex.config;

import codeOrchestra.colt.as.flex.LiveCodingAnnotation;
import codeOrchestra.colt.as.flex.usedCode.LinkReportException;
import codeOrchestra.colt.as.flex.usedCode.LinkReportReader;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.COLTAsProjectBuildSettings;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFile;
import codeOrchestra.colt.as.session.sourcetracking.IgnoredSources;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.colt.core.build.BuildException;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.NameUtil;
import codeOrchestra.util.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class FlexConfigBuilder {
  
  private final COLTAsProject project;
  private final boolean incrementalCompilation;
  private final List<ASSourceFile> changedFiles;
  private final boolean assetsUpdateMode;

  public FlexConfigBuilder(COLTAsProject project, boolean incrementalCompilation, List<ASSourceFile> changedFiles, boolean skipClear) {
    this.project = project;
    this.incrementalCompilation = incrementalCompilation;
    this.changedFiles = changedFiles;
    this.assetsUpdateMode = skipClear;
  }

  public FlexConfig build() throws BuildException {
    COLTAsProjectBuildSettings compilerSettings = COLTAsProject.getCurrentProject().getProjectBuildSettings();
    FlexConfig flexConfig = new FlexConfig(incrementalCompilation, false);

    // Sources
    if (!incrementalCompilation) {
      // Defined source paths
      for (String sourcePath : project.getProjectPaths().getSourcePaths()) {
        flexConfig.addSourcePath(sourcePath);
      }
      
      // Provided libraries (language extensions) sources
      flexConfig.addLibraryPath(ASPathUtils.getColtSWCPath());
    }
    
    // Always strict = false 
    flexConfig.setStrict(false);

    // Incremental settings
    if (incrementalCompilation) {
      // Add root module generated sources
      String outputFileName = compilerSettings.getOutputFilename().replaceFirst("\\.swf$", ".swc");
      flexConfig.addLibraryPath(project.getOutputDir().getPath() +  File.separator + outputFileName);
      flexConfig.addLibraryPath(ASPathUtils.getColtSWCPath());
      flexConfig.setOutputPath(ASPathUtils.getSourceIncrementalSWCPath(project));
      
      // Add previous incremental SWCs as libraries
      File incrementalOutputDir = new File(ASPathUtils.getIncrementalOutputDir(project));
      File[] incrementalSWCs = incrementalOutputDir.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".swc");
        }
      });
      if (incrementalSWCs != null && incrementalSWCs.length > 0) {
        for (int i = 0; i < incrementalSWCs.length; i++) {
          File incrementalSWC = incrementalSWCs[i];
          flexConfig.addLibraryPath(incrementalSWC.getPath());
        }
      }
      
      // Load root module link report file for externs for Live-Coding
      // incremental module
      flexConfig.setLoadExternsFilePath(project.getLinkReportFile().getPath());

      // CO-4487 - compiler shenanigans for incremental mode      
      flexConfig.setVerifyDigests(false);
      flexConfig.setWarnings(false);
      flexConfig.setIncremental(true);
      
      // Changed source files must be copied to a separate folder and added to the config
      File incrementalSourcesDir = project.getOrCreateIncrementalSourcesDir();
      if (!assetsUpdateMode) {
        FileUtils.clear(incrementalSourcesDir);
        for (ASSourceFile sourceFile : changedFiles) {
          try {
            FileUtils.copyFileWithIncrementalReplaces(sourceFile.getFile(), new File(incrementalSourcesDir, sourceFile.getRelativePath()));
          } catch (IOException e) {
            throw new BuildException("Can't copy changed source file to 'incremental' dir", e);
          }
        }
      }      
      // Copy assets to the incremental source dir
      for (String sourcePath : project.getProjectPaths().getSourcePaths()) {
        File sourceDir = new File(sourcePath);
        if (sourceDir.exists()) {
          List<File> nonSources = FileUtils.listFileRecursively(sourceDir, new FileFilter() {            
            @Override
            public boolean accept(File file) {
              if (file.isDirectory()) {
                return false;
              }
              String fileNameLowerCase = file.getName().toLowerCase();
              return !fileNameLowerCase.endsWith(".as") && !fileNameLowerCase.endsWith(".mxml");
            }
          });
          for (File nonSource : nonSources) {
            String relativePath = FileUtils.getRelativePath(nonSource.getPath(), sourceDir.getPath(), File.separator);
            try {
              File newFile = new File(incrementalSourcesDir, relativePath);
              if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
              }
              FileUtils.copyFileChecked(nonSource, newFile, false);
            } catch (IOException e) {
              throw new BuildException("Can't copy file to 'incremental' dir", e);
            }
          }
        }
      }
      flexConfig.addSourcePath(incrementalSourcesDir.getPath());
    }

    // Link report file generation
    if (incrementalCompilation) {
      flexConfig.setLinkReportFilePath(null);      
    } else {
      flexConfig.setLinkReportFilePath(project.getLinkReportFile().getPath());
    }

    // Output path
    if (!incrementalCompilation) {
      flexConfig.setOutputPath(project.getOutputDir().getPath() + File.separator + compilerSettings.getOutputFilename());
    }

    // Libraries
    for (String libraryPath : project.getProjectPaths().getLibraryPaths()) {
      flexConfig.addLibraryPath(libraryPath);
    }

    // Main class
    if (!incrementalCompilation) {
      flexConfig.addFileSpecPathElement(compilerSettings.getMainClass());
    }

    // Target player version
    flexConfig.setTargetPlayerVersion(compilerSettings.getTargetPlayerVersion());

    // Include classes (SWC)
    if (incrementalCompilation) {
      for (ASSourceFile changedSourceFile : changedFiles) {
        flexConfig.addClass(changedSourceFile.getFqName());
      }
    }

    // RSL
    flexConfig.setRuntimeSharedLibrary(compilerSettings.useFrameworkAsRSL());

    // Locale
    if (compilerSettings.useNonDefaultLocale()) {
      flexConfig.setLocale(compilerSettings.getLocaleOptions());
    }

    // Custom metadata
    for (LiveCodingAnnotation lca : LiveCodingAnnotation.values()) {
      flexConfig.addCustomMetadata(lca.name());
    }

    return flexConfig;
  }

  public static void addLibraryClasses(FlexConfig flexConfig, List<String> sourcePaths) throws BuildException {
    COLTAsProject currentProject = COLTAsProject.getCurrentProject();
    COLTAsProjectBuildSettings compilerSettings = currentProject.getProjectBuildSettings();
    List<String> excludedClasses = compilerSettings.getExcludedClasses();
    
    for (String sourcePath : sourcePaths) {
      File sourceDir = new File(sourcePath);
      if (!sourceDir.exists() || !sourceDir.isDirectory()) {
        continue;
      }

      List<File> sourceFiles = FileUtils.listFileRecursively(sourceDir, new FileFilter() {
        @Override
        public boolean accept(File file) {
          String filenameLowerCase = file.getName().toLowerCase();
          return filenameLowerCase.endsWith(ASSourceFile.DOT_AS) || filenameLowerCase.endsWith(ASSourceFile.DOT_MXML);
        }
      });

      for (File sourceFile : sourceFiles) {
        String relativePath = FileUtils.getRelativePath(sourceFile.getPath(), sourceDir.getPath(), File.separator);

        if (relativePath.toLowerCase().endsWith(ASSourceFile.DOT_AS)) {
          relativePath = relativePath.substring(0, relativePath.length() - ASSourceFile.DOT_AS.length());
        } else if (relativePath.toLowerCase().endsWith(ASSourceFile.DOT_MXML)) {
          relativePath = relativePath.substring(0, relativePath.length() - ASSourceFile.DOT_MXML.length());
        }
        
        if (!StringUtils.isEmpty(relativePath)) {
          String fqName = NameUtil.namespaceFromPath(relativePath);
            List<String> usedClassesFqNames = null;
            try {
                usedClassesFqNames = new LinkReportReader(currentProject.getLinkReportFile()).fetchUsedClassesFqNames();
            } catch (LinkReportException e) {
                throw new BuildException(e);
            }

            if (!IgnoredSources.isIgnoredTrait(fqName) && !excludedClasses.contains(fqName)) {
            if (!usedClassesFqNames.contains(fqName) && compilerSettings.excludeUnusedCode()) {
              continue;
            }
            flexConfig.addClass(fqName);
          }
        }
      }
    }
  }

}
