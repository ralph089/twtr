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
 * Parses the entries of the CSV file into a list of {@link RelationalEntry}.
 */
public class RelationalParser {

    /**
     * Parse CSV-input of URL.
     *
     * @param resourceUrl the resource url
     * @return list
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

        List<RelationalEntry> relationalEntryList = new ArrayList<RelationalEntry>();

        while (deserializer.hasNext()) {
            RelationalEntry entry = deserializer.next();
            relationalEntryList.add(entry);
        }
        deserializer.close(true);

        return relationalEntryList;
    }
}
