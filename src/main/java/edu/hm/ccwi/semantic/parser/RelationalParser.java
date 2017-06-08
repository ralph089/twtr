package edu.hm.ccwi.semantic.parser;

import net.sf.jsefa.Deserializer;
import net.sf.jsefa.csv.CsvIOFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Deserializer deserializer = CsvIOFactory.createFactory(RelationalEntry.class).createDeserializer();
        deserializer.open(reader);

        List<RelationalEntry> relationalEntryList = new ArrayList<>();

        while (deserializer.hasNext()) {
            RelationalEntry entry = deserializer.next();
            if (entry != null && entry.getTweetText() != null && !Objects.equals(entry.getTweetText(), "")) {
                relationalEntryList.add(entry);
            }
        }
        deserializer.close(true);

        return relationalEntryList;
    }
}
