package codeOrchestra.colt.as.security;

import codeOrchestra.util.FileUtils;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.SystemInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class TrustedLocations {

  private static final int MAX_LOCATIONS_COUNT = 100;
  
  private static final TrustedLocations instance = new TrustedLocations();

  public static TrustedLocations getInstance() {
    return instance;
  }

  public void addTrustedLocation(String trustedLocation) {
    addTrustedLocation(trustedLocation, false);
  }
  
  public void addTrustedLocation(String trustedLocation, boolean stop) {
    List<String> trustedLocations = getTrustedLocations();
    if (!trustedLocations.contains(trustedLocation)) {
      trustedLocations.add(0, trustedLocation);
    }
    
    if (trustedLocations.size() > MAX_LOCATIONS_COUNT) {
      trustedLocations.remove(trustedLocations.size() - 1);
    }

    saveTrustedLocations(trustedLocations);
    
    // CFB-5
    if (!stop) {
      addTrustedLocation(new File(trustedLocation).getParent(), true);
    }
  }

  private void saveTrustedLocations(List<String> trustedLocations) {
    StringBuilder sb = new StringBuilder();
    for (String trustedLocationToSave : trustedLocations) {
      sb.append(trustedLocationToSave).append("\n");
    }
    FileUtils.write(getOrCreateTrustConfigurationFile(), sb.toString());
  }

  public List<String> getTrustedLocations() {
    List<String> trustedLocations = new ArrayList<String>();

    String trustConfigurationContent = FileUtils.read(getOrCreateTrustConfigurationFile());
    String[] trustLocationsSplit = trustConfigurationContent.split("\\n");
    if (trustLocationsSplit != null && trustLocationsSplit.length > 0) {
      for (String token : trustLocationsSplit) {
        if (token != null && StringUtils.isNotEmpty(token.trim())) {
          trustedLocations.add(token.trim());          
        }                
      }
    }
    
    return trustedLocations;
  }

  public File getOrCreateTrustConfigurationFile() {
    String userHome = System.getProperty("user.home");
    File flashPlayerTrustDir;
    if (SystemInfo.isWindows) {
      flashPlayerTrustDir = new File(userHome, "Application Data\\Macromedia\\Flash Player\\#Security\\FlashPlayerTrust");
    } else if (SystemInfo.isMac) {
      flashPlayerTrustDir = new File(userHome, "Library/Preferences/Macromedia/Flash Player/#Security/FlashPlayerTrust");
    } else {
      return null;
    }

    if (!flashPlayerTrustDir.exists()) {
      flashPlayerTrustDir.mkdirs();
    }

    File trustConfigurationFile = new File(flashPlayerTrustDir, "colt.cfg");
    if (!trustConfigurationFile.exists()) {
      try {
        trustConfigurationFile.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("Can't create Flash Player trust configuration file", e);
      }
    }

    return trustConfigurationFile;
  }

}
