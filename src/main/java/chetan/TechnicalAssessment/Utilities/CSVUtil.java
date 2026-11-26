package chetan.TechnicalAssessment.Utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CSVUtil {
    public List<String[]> importCSV(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
             CSVReader csv = new CSVReader(reader)) {
            return csv.readAll();
        } catch (CsvException e) {
            throw new IOException("Failed to parse CSV: " + file, e);
        }
    }

    /**
     * Writes rows to a CSV file, overwriting it if it exists.
     */
    public void exportToCSV(Path file, List<String[]> rows) throws IOException {
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
             CSVWriter csv = new CSVWriter(writer)) {
            csv.writeAll(rows);
        }
    }

    /**
     * Reads a CSV file with a header row into a list of beans.
     */
    public <T> List<T> importCSVToObj(Path file, Class<T> type) throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (RuntimeException e) {
            throw new IOException("Failed to map CSV to " + type.getSimpleName(), e);
        }
    }

    /**
     * Writes a list of beans to a CSV file with a header row.
     */
    public <T> void exportObjToCSV(Path file, List<T> beans) throws IOException {
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<T>(writer)
                    .build()
                    .write(beans);
        } catch (CsvException e) {
            throw new IOException("Failed to write beans to CSV", e);
        }
    }
}
