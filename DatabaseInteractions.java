package CSVLibrary;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static CSVLibrary.CSVObject.lineSeparator;

/**
 * This class holds all useful methods for interacting with the database. It includes 
 * methods such as {@link DatabaseInteractions#appendRow(CSVObject)}, 
 * {@link DatabaseInteractions#appendMultipleRows(List)}, {@link DatabaseInteractions#readLine()},
 * and {@link DatabaseInteractions#readAllLines()}. 
 *<p>
 *
 * During instantiation, this class reads the content of the csv file and stores its content into memory.
 * This allows for fast reading methods.
 */
public class DatabaseInteractions {
    private int readLineCounter;
    private List<String> CSVrows;
    private Path filePath;
    private CSVObject option;


    /**
     * will open the database or make a new one
     * @param filename name of the database
     * @param databaseType type of the database, either Student, Schedule or Custom. Can specify the type
     *                     simply by using the method {@code CSV.getDummyObject()} with the type needed
     * @return  a DatabaseInteractions object used to interact with the database
     * @throws Exception if any error occurred when accessing the file
     */
    public static DatabaseInteractions openOrCreate(String filename, CSVObject databaseType) throws Exception {
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            makeCSVFile(filename, databaseType);
        }

        ConnectionCSVFile.setFileName(filename);//initialize instance

        return new DatabaseInteractions(databaseType);
    }


    /**
     * makes a DatabaseInteraction object.
     * @param option an object representing what type of csv database we are manipulating
     * @throws IOException if any error occurs while accessing the database
     */
    private DatabaseInteractions(CSVObject option) throws IOException {
        this.filePath = ConnectionCSVFile.getInstance().getFile();
        this.option = option;
        this.readLineCounter = 1;//ignore header
        CSVrows = Files.readAllLines(filePath);//load csv file content into memory
    }//end of DatabaseInteractions constructor
    


    public void openCSVFile (String filename) throws Exception{
        ConnectionCSVFile.setFileName(filename);
        ConnectionCSVFile conn = ConnectionCSVFile.getInstance();
    }//end of OpenCSVFile


    public void closeCSVFile(){
        ConnectionCSVFile.getInstance().close();
        //do not allow the user to do anything with this object, we have closed the connection
        this.CSVrows = null;
        this.filePath = null;
        this.option = null;
    }//end of CloseCSVFile


    /**
     * reads all the file at once and stores the content in memory
     * returns each line sequentially at each method call and skips the header
     * If there are no more lines to read, will return an empty string
     * @return current line from the file or an empty string if there are no more lines
     */
    public String readLine(){
        if (readLineCounter >= CSVrows.size()){
            //no more lines to read
            return "";
        }
        String line = (CSVrows.get(readLineCounter)).replace(lineSeparator, "");//remove new line char;
        readLineCounter++;
        return  line;
    }//end of readLine


    public CSVObject readLineAsObject(){
        String line = readLine();
        if (!option.isValidCSVEntry(line)){return null;}//what if we have a wrong input in the db????
        return option.convertFromStringToObject(line);
    }


    public List<String> readAllLines(){
        return new ArrayList<>(CSVrows);//give new reference
    }//end of readAllLines

    
    public void resetReadLineCounter(){
        this.readLineCounter = 1;
    }//end of resetReadLineCounter


    public List<CSVObject> readAllLinesAsObject(){
        List<CSVObject> list = new ArrayList<>(CSVrows.size());
        boolean first = true;
        for (String curLine : CSVrows){
            if (first) { firs = false; continue; } // skip first element, i.e. the header
            list.add(option.convertFromStringToObject(curLine));
        }
        return list;
    }//end of readAllLinesAsObject


    private void updateRowsInMemory(String row){
        this.CSVrows.add(row);
    }//end of updateRowsInMemory


    public boolean appendRow(CSVObject toAppend){
        if (toAppend.getClass() != option.getClass()){return false;}//incompatible types

        boolean success = new WriteOneRow(toAppend).append(filePath);
        if (success){
            updateRowsInMemory(toAppend.formatToCSV());
        }
        return success;
    }//end of appendRow


    public boolean appendMultipleRows(List<CSVObject> listToAppend){
        for (CSVObject cur : listToAppend){
            if (cur.getClass() != option.getClass()){return false;}//incompatible types
        }

        boolean success = new WriteMultipleRows(listToAppend).append(filePath);

        if (success){
            for (CSVObject curCSV : listToAppend){
                updateRowsInMemory(curCSV.formatToCSV());
            }
        }
        return success;
    }//end of appendMultipleRows



    public static void makeCSVFile(String filename, CSVObject option) throws Exception {
        Path newFile = FileSystems.getDefault().getPath(filename);

        if (Files.exists(newFile)) {
            throw new Exception(String.format("File \"%s\" already exists", filename));
        }

        if (!verifyValidCSVExtension(filename)) {
            throw new Exception(String.format("File \"%s\" is not of the correct format, it should have extension \".csv\"", filename));
        }

        Files.createFile(newFile);

        //write header into file
        boolean write = new WriteOneRowString(option.getHeader())
                .append(newFile);

        if (!write){
            throw new IOException("Cannot open file");
        }
    }//end of makeCSVFile


    private static boolean verifyValidCSVExtension(String filename){
        return filename.endsWith(".csv");
    }//end of verifyValidCSVExtension


}//end of DatabaseInteractions class
