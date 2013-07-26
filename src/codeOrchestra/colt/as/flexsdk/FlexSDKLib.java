package codeOrchestra.actionScript.flexsdk;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import codeOrchestra.lcs.project.LCSProject;
import codeOrchestra.utils.FileUtils;

/**
 * @author Alexander Eliseyev
 */
public enum FlexSDKLib {

  PLAYERGLOBAL_SWC("playerglobal.swc"),
  FRAMEWORK_SWC("framework.swc"),
  RPC_SWC("rpc.swc"),
  SPARK_SWC("spark.swc"),
  OSMF_SWC("osmf.swc"),
  MX_SWC("mx/mx.swc");

  private static final String REGULAR_LIBS_RELATIVE_PATH = "frameworks" + File.separator + "libs";
  private static final String PLAYERLIBS_RELATIVE_PATH = REGULAR_LIBS_RELATIVE_PATH + File.separator + "player";
  
  private String libPath;

  private FlexSDKLib(String libPath) {
    this.libPath = libPath;
  }

  public String getLibRelativePath() {
    return libPath;
  }

  public String getNamespace() {
    return name().toLowerCase();
  }

  public boolean isPlayerGlobal() {
    return PLAYERGLOBAL_SWC == this;
  }

  public static FlexSDKLib get(String namespace) {
    for (FlexSDKLib flexSDKLib : values()) {
      if (flexSDKLib.getNamespace().equals(namespace)) {
        return flexSDKLib;
      }
    }
    return null;
  }
  
  public String getPath() {
    String flexSDKDir = LCSProject.getCurrentProject().getCompilerSettings().getFlexSDKPath();
    if (isPlayerGlobal()) {
      return getMostRecentPlayerglobalSWCPath(new File(flexSDKDir));
    } else {
      String libPath = flexSDKDir + File.separator + REGULAR_LIBS_RELATIVE_PATH + File.separator + getLibRelativePath();
      if (FileUtils.doesExist(libPath)) {
        return libPath;
      }
    }
    return null;
  }
  
  private String getMostRecentPlayerglobalSWCPath(File flexSDKDir) {
    File playerLibsDir = new File(flexSDKDir, PLAYERLIBS_RELATIVE_PATH);
    if (!playerLibsDir.exists()) {
      return null;
    }

    File[] playerDirs = playerLibsDir.listFiles(FileUtils.DIRECTORY_FILTER);
    if (playerDirs ==  null || playerDirs.length == 0) {
      return null;
    }

    File playerDir;
    File playerglobalFile;
    if (playerDirs.length == 1) {
      playerDir = playerDirs[0];
    } else {
      playerDir = pickPlayerPath(playerDirs);
    }

    playerglobalFile = getPlayerglobalSWCFile(playerDir);

    return playerglobalFile.getPath();
  }

  private static File pickPlayerPath(File[] playerDirs) {
    if (playerDirs.length == 0) {
      return null;
    }

    List<File> playersList = new ArrayList<File>();
    for (File playerDir : playerDirs) {
      playersList.add(playerDir);
    }

    Collections.sort(playersList, new Comparator<File>() {
      @Override
      public int compare(File playerPath1, File playerPath2) {
        return getVersion(playerPath2) - getVersion(playerPath1);
      }

      private int getVersion(File playerPath) {
        try {
          String dirName = playerPath.getName();
          if (dirName.contains(".")) {
            int power = 3;
            String[] dirNameSplitted = dirName.split("\\.");
            int versionInt = 0;

            for (int i = 0; i < dirNameSplitted.length; i++) {
              versionInt += (Integer.valueOf(dirNameSplitted[i]) * (Math.pow(10, power)));
              power--;
            }

            return versionInt;
          }
        } catch (Throwable t) {
          return 0;
        }
        return 0;
      }
    });

    return playersList.get(0);
  }
  
  private static File getPlayerglobalSWCFile(File playerDir) {
    File[] swcs = playerDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String s) {
        String fileNameLowerCase = s.toLowerCase();
        return fileNameLowerCase.startsWith("playerglobal") && fileNameLowerCase.endsWith(".swc");
      }
    });
    if (swcs.length > 0) {
      return swcs[0];
    }
    throw new RuntimeException("Can't locate a playerglobal SWC file in the " + playerDir.getPath() + " dir");
  }
  
}
