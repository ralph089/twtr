package edu.hm.ccwi.semantic.parser.relational;

import net.sf.jsefa.Deserializer;
import net.sf.jsefa.csv.CsvIOFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a CSV file.
 */
public class RelationalParser {

    /**
     * Parses the entries of the CSV file into a list of {@link RelationalEntry}.
     *
     * @param resourceUrl the resource url of the CSV file.
     * @return list of {@link RelationalEntry}.
     * @author Max Auch
     */
    public List<RelationalEntry> parseRelationalExportedData(String resourceUrl) {

        final File file = new File(resourceUrl);
        Reader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Deserializer deserializer = CsvIOFactory.createFactory(RelationalEntry.class).createDeserializer();
        deserializer.open(reader);

        List<RelationalEntry> relationalEntryList = new ArrayList<>();

        while (deserializer.hasNext()) {
            RelationalEntry entry = deserializer.next();
            relationalEntryList.add(entry);
        }
        deserializer.close(true);

        return relationalEntryList;
    }
}
