package se.nrm.samlingar.data.process.logic.util;

/**
 *
 * @author idali
 */
public class CommonString {

    private final String processKey = "process";
    private final String fileNameKey = "fileName";
    private final String separtorKey = "separtor";
    private final String encodingKey = "encoding";
    private final String addImagesKey = "addImages";

    private static CommonString instance = null;

    public static CommonString getInstance() {
        synchronized (CommonString.class) {
            if (instance == null) {
                instance = new CommonString();
            }
        }
        return instance;
    }

    public String getProcessKey() {
        return processKey;
    }

    public String getFileNameKey() {
        return fileNameKey;
    }

    public String getSepartorKey() {
        return separtorKey;
    }

    public String getEncodingKey() {
        return encodingKey;
    }

    public String getAddImagesKey() {
        return addImagesKey;
    }
}
