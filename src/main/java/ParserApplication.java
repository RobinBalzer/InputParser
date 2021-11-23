import Parser.Database.DatabaseParser;
import Parser.OutputBuilder;
import Parser.Query.SPARQLParser;
import Parser.Transducer.TransducerParser;
import Settings.Settings;

import java.io.IOException;

public class ParserApplication {
    /**
     * this main is the entry for the InputParser application. It outputs a .txt file that is consistent with the format that is required by the approximateQueries application.
     * An example of the format is stored under resources/StructureOfAQInput.
     * @param args arguments consisting of 3 input files.
     */
    public static void main(String[] args) throws Exception {

        // store the given arguments globally for easier access
        Settings.inputQueryFile = args[0];
        Settings.inputTransducerFile = args[1];
        Settings.inputDatabaseFile = args[2];
        Settings.fileName = args[0].split("\\.")[0];

        // process query part
        SPARQLParser sparqlParser = new SPARQLParser();
        sparqlParser.readQuery(Settings.inputFileDirectory + Settings.inputQueryFile);

        // process transducer part
        TransducerParser transducerParser = new TransducerParser();
        if (Settings.inputTransducerFile.equals("gmark")) {
            transducerParser.gMarkEditDistance();
        } else {
            transducerParser.emptyTransducer();
        }

        // process database part
        DatabaseParser databaseParser = new DatabaseParser();
        databaseParser.readDatabase();

        // write results to a single .txt file
        OutputBuilder outputBuilder = new OutputBuilder(sparqlParser, transducerParser, databaseParser);
        outputBuilder.writeToFile();

    }
}
