package se.nrm.samlingar.data.process.logic.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable; 
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat; 
import org.apache.commons.csv.CSVRecord; 
import se.nrm.samlingar.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class CSVFileProcessor implements Serializable {

    public CSVFileProcessor() {

    }

    public Iterable<CSVRecord> read1(String filePath, String fileName, char delimiter, String encoding) {

        Reader in;
        try {
            in = new FileReader(Util.getInstance().buildCsvFilePath(filePath, fileName));
            return CSVFormat.RFC4180.builder()
                    .setDelimiter(delimiter)
                    .build().parse(in);
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public List<CSVRecord> read(String filePath, String fileName, char delimiter, String encoding) {
        File file = new File(Util.getInstance().buildCsvFilePath(filePath, fileName));
        if (file.exists()) {
            log.info("file exist....");
            return processFile(file, delimiter, encoding);
        } else {
            log.error("file not exist : {}", file.getName());
        }
        return null;
    }
    
    public List<CSVRecord> read(String filePath, char delimiter, String encoding) {
        log.info("read : {}", filePath);

        File file = new File(filePath);
        if (file.exists()) {
            log.info("file exist : {}", file.getName());
            return processFile(file, delimiter, encoding);
        } else {
            log.error("file not exist : {}", file.getName());
        }
        return null;
    }

    private List<CSVRecord> processFile(File file, char delimiter, String encoding) { 

        InputStream in;
        Reader reader = null;
        try {
            in = new FileInputStream(file);
            log.info("here...");
            if (encoding == null) {
                reader = new InputStreamReader(in);
            } else {
                reader = new InputStreamReader(in, encoding);
            }

            return getCsvFormat(delimiter)
                    .parse(reader).getRecords();

        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return null;
    }

    private CSVFormat getCsvFormat(char delimiter) {

        return CSVFormat.RFC4180.builder()
                .setDelimiter(delimiter)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build(); 
    }
}
