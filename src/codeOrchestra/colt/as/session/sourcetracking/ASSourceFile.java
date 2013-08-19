package codeOrchestra.colt.as.session.sourcetracking;

import codeOrchestra.colt.core.session.sourcetracking.SourceFile;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.NameUtil;
import codeOrchestra.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASSourceFile implements SourceFile {

    private static final List<String> assetExtensions = new ArrayList<String>() {{
        String[] extensions = new String[] { "css",
                "swf",
                "htm",
                "html",
                "png",
                "jpg",
                "jpeg",
                "gif",
                "wav",
                "mp3",
                "ogg",
                "txt",
                "mpg",
                "mpeg",
                "avi",
                "mp4",
                "mpeg4",
                "xml",
                "3ds",
                "plan",
                "decor",
                "potr",
                "matr"};

        for (String extension : extensions) {
            add(extension);
        }
    }};

    public static final String DOT_MXML = ".mxml";
    public static final String DOT_AS = ".as";

    private File file;
    private String fqName;
    private String relativePath;

    public ASSourceFile(File file, String sourceDir) {
        this.file = file;

        relativePath = FileUtils.getRelativePath(file.getPath(), sourceDir, File.separator);

        if (!StringUtils.isEmpty(relativePath)) {
            if (relativePath.toLowerCase().endsWith(DOT_AS)) {
                fqName = NameUtil.namespaceFromPath(relativePath.substring(0, relativePath.length() - DOT_AS.length()));
            } else if (relativePath.toLowerCase().endsWith(DOT_MXML)) {
                fqName = NameUtil.namespaceFromPath(relativePath.substring(0, relativePath.length() - DOT_MXML.length()));
            }
        } else {
            throw new IllegalArgumentException(file.getPath());
        }
    }

    public boolean isAsset() {
        for (String extension : assetExtensions) {
            if (relativePath.toLowerCase().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCompilable() {
        return fqName != null;
    }

    public File getFile() {
        return file;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getFqName() {
        return fqName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + ((fqName == null) ? 0 : fqName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ASSourceFile other = (ASSourceFile) obj;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        if (fqName == null) {
            if (other.fqName != null)
                return false;
        } else if (!fqName.equals(other.fqName))
            return false;
        return true;
    }


}
