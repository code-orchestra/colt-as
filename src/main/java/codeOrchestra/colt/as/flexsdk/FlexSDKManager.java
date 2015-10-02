package codeOrchestra.colt.as.flexsdk;

import codeOrchestra.util.FileUtils;

import java.io.File;
import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class FlexSDKManager {

    private static FlexSDKManager instance;

    public static FlexSDKManager getInstance() {
        if (instance == null) {
            instance = new FlexSDKManager();
        }
        return instance;
    }

    private static final String REGULAR_LIBS_RELATIVE_PATH = "frameworks" + File.separator + "libs";
    private static final String PLAYERLIBS_RELATIVE_PATH = REGULAR_LIBS_RELATIVE_PATH + File.separator + "player";

    private static final String DEFAULT_PLAYER_VERSION = "10.2.0";

    private Map<FlexSDKLibWrapper, String> libPathCache = new HashMap<>();

    private String playerDirName;

    public void checkIsValidFlexSDKPath(String flexSDKPath) throws FlexSDKNotPresentException {
        for (FlexSDKLib flexSDKLib : FlexSDKLib.values()) {
            getLibPath(flexSDKPath, flexSDKLib.getNamespace());
        }
    }

    public boolean isFlexLib(String namespace) {
        return FlexSDKLib.get(namespace) != null;
    }

    public boolean isAirPresent(String flexSDKPath) {
        return new File(getAdlExecutablePath(flexSDKPath)).exists() && new File(getAdtExecutablePath(flexSDKPath)).exists();
    }

    public String getAdtExecutablePath(String flexSDKPath) {
        String result = new File(flexSDKPath, "bin" + File.separator + "adt").getPath();

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            return result;
        } else if (osName.startsWith("Windows")) {
            return result + ".bat";
        }

        return result;
    }

    public String getAdlExecutablePath(String flexSDKPath) {
        String result = new File(flexSDKPath, "bin" + File.separator + "adl").getPath();

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            return result;
        } else if (osName.startsWith("Windows")) {
            return result + ".exe";
        }

        return result;
    }

    private String getLibPath(String flexSDKPath, String namespace) throws FlexSDKNotPresentException {
        if (flexSDKPath == null) {
            throw new FlexSDKNotPresentException("Flex SDK path not specified");
        }

        // Try to get the cached path
        FlexSDKLibWrapper flexLibKey = new FlexSDKLibWrapper(flexSDKPath, namespace);
        String cachedLibPath = libPathCache.get(flexLibKey);
        if (cachedLibPath != null) {
            return cachedLibPath;
        }

        if (!isFlexLib(namespace)) {
            throw new IllegalArgumentException(namespace + " is not a Flex SDK lib");
        }

        File flexSDKDir = new File(flexSDKPath);
        if (!flexSDKDir.exists() || !flexSDKDir.isDirectory()) {
            throw new FlexSDKNotPresentException("Flex SDK path " + flexSDKDir + " is not valid");
        }

        FlexSDKLib flexSDKLib = FlexSDKLib.get(namespace);

        if (flexSDKLib.isPlayerGlobal()) {
            String playerglobalSWCPath = getMostRecentPlayerglobalSWCPath(flexSDKDir);
            cacheLibPath(flexLibKey, playerglobalSWCPath);
            return playerglobalSWCPath;
        } else {
            String libPath = flexSDKDir.getPath() + File.separator + REGULAR_LIBS_RELATIVE_PATH + File.separator + flexSDKLib.getLibRelativePath();
            if (FileUtils.doesExist(libPath)) {
                cacheLibPath(flexLibKey, libPath);
                return libPath;
            }
            throw new FlexSDKNotPresentException("Can't locate the " + flexSDKLib.getLibRelativePath() + " in the Flex SDK path configured");
        }
    }

    public List<String> getAvailablePlayerVersions(File flexSDKDir) throws FlexSDKNotPresentException {
        List<String> result = new ArrayList<>();

        File playerLibsDir = new File(flexSDKDir, PLAYERLIBS_RELATIVE_PATH);
        if (!playerLibsDir.exists()) {
            throw new FlexSDKNotPresentException("Can't locate player libs in the Flex SDK path configured");
        }

        File[] playerDirs = playerLibsDir.listFiles(FileUtils.DIRECTORY_FILTER);
        if (playerDirs == null || playerDirs.length == 0) {
            throw new FlexSDKNotPresentException("Can't locate player libs in the Flex SDK path configured");
        }

        for (File playerDir : playerDirs) {
            String playerVersion = playerDir.getName();
            if (!playerVersion.contains(".")) {
                continue;
            }

            if (playerVersion.indexOf(".") == playerVersion.lastIndexOf(".")) {
                playerVersion += ".0";
            }

            result.add(playerVersion);
        }
        result.sort((o1, o2) -> getPlayerVersion(o2) - getPlayerVersion(o1));

        return result;
    }

    private static int getPlayerVersion(String version) {
        int power = 3;
        String[] versionSpited = version.split("\\.");
        int versionInt = 0;

        for (String str : versionSpited) {
            versionInt += (Integer.valueOf(str) * (Math.pow(10, power)));
            power--;
        }

        return versionInt;
    }

    public static void main(String[] args) throws FlexSDKNotPresentException {
        FlexSDKManager manager = new FlexSDKManager();
        for (String playerVersion : manager.getAvailablePlayerVersions(new File("/Users/buildserver/TMP/CodeOrchestra.app/flex_sdk/"))) {
            System.out.println("[" + playerVersion + "]");
        }
    }

    public String getMostRecentPlayerglobalSWCPath(File flexSDKDir) throws FlexSDKNotPresentException {
        File playerLibsDir = new File(flexSDKDir, PLAYERLIBS_RELATIVE_PATH);
        if (!playerLibsDir.exists()) {
            throw new FlexSDKNotPresentException("Can't locate player libs in the Flex SDK path configured");
        }

        File[] playerDirs = playerLibsDir.listFiles(FileUtils.DIRECTORY_FILTER);
        if (playerDirs == null || playerDirs.length == 0) {
            throw new FlexSDKNotPresentException("Can't locate player libs in the Flex SDK path configured");
        }

        File playerDir;
        File playerglobalFile;
        if (playerDirs.length == 1) {
            playerDir = playerDirs[0];
        } else {
            playerDir = pickPlayerPath(playerDirs);
        }

        playerglobalFile = getPlayerglobalSWCFile(playerDir);
        playerDirName = playerDir.getName();

        return playerglobalFile.getPath();
    }

    private static File getPlayerglobalSWCFile(File playerDir) {
        File[] swcs = playerDir.listFiles((file, s) -> {
            String fileNameLowerCase = s.toLowerCase();
            return fileNameLowerCase.startsWith("playerglobal") && fileNameLowerCase.endsWith(".swc");
        });
        if (swcs.length > 0) {
            return swcs[0];
        }
        throw new RuntimeException("Can't locate a playerglobal SWC file in the " + playerDir.getPath() + " dir");
    }

    private static File pickPlayerPath(File[] playerDirs) {
        if (playerDirs.length == 0) {
            return null;
        }

        List<File> playersList = new ArrayList<>();
        for (File playerDir : playerDirs) {
            playersList.add(playerDir);
        }

        Collections.sort(playersList, (playerPath1, playerPath2) -> getPlayerVersion(playerPath2) - getPlayerVersion(playerPath1));

        return playersList.get(0);
    }

    private static int getPlayerVersion(File playerPath) {
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

    private static class FlexSDKLibWrapper {
        private String flexSDKPath;
        private String namespace;

        public FlexSDKLibWrapper(String flexSDKPath, String namespace) {
            this.flexSDKPath = flexSDKPath;
            this.namespace = namespace;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            FlexSDKLibWrapper that = (FlexSDKLibWrapper) o;

            if (flexSDKPath != null ? !flexSDKPath.equals(that.flexSDKPath) : that.flexSDKPath != null)
                return false;
            if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = flexSDKPath != null ? flexSDKPath.hashCode() : 0;
            result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
            return result;
        }
    }

    private void cacheLibPath(FlexSDKLibWrapper libKey, String libPath) {
        if (libPath == null) {
            return;
        }
        libPathCache.put(libKey, libPath);
    }

}