package CSVLibrary;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;


/**
 * The {@code ConnectionCSVFile} class represents a Singleton connection manager for a CSV file.
 * It ensures that only one file connection exists at any given time, providing controlled
 * access and initialization.
 * <p>
 * This class is responsible for verifying file existence, readability, and writability before
 * establishing a connection. It prevents multiple simultaneous connections to different files.
 * <p>
 * The first call to {@link #setFileName(String)} establishes the connection and
 * initializes the Singleton. All subsequent calls to {@link #getInstance()} return the same
 * instance. To connect to a different file, the current connection must be closed
 * using {@link #close()}.
 * <p>
 * Trying to establish a new connection before closing the previous one will result in
 * an error thrown. Similarly, not having access to the file and not being able to write/read
 * to/from it will throw an error.
 */
public class ConnectionCSVFile {
    private static ConnectionCSVFile instance = null;
    private Path csvPath = null;



    private ConnectionCSVFile() {//private constructor to make the class "static"
    }//end of ConnectionCSVFile constructor


    /**
     * Returns the current instance of {@code ConnectionCSVFile} once it has been
     * initialized using {@link #setFileName(String)}. If no file has been set et, it
     * throws an exception error.

     * @return the singleton {@code ConnectionCSVFile} instance
     */
    public static ConnectionCSVFile getInstance(){
        if (instance == null){
            throw new IllegalStateException("Need to set the file name with \"setFileName()\" before calling \"getInstance()\"" +
                    "If no csv database currently exists, you can make a new one with: DatabaseInteractions.MakeDatabase.makeCSVFile()");
        }
        
        return instance;
    }//end of getInstance


    /**
     * Returns the CSV file currently associated with this connection.
     * This method ensures that the CSV file has been initialized before access.
     * It throws an exception if {@link #setFileName(String)} has not yet been called.
     *
     * @return the {@code File} object representing the current CSV database
     */
    public Path getFile(){
        if (instance == null) {throw new IllegalStateException("File not initialized. Call \"setFileName()\" first");}
        //is this check really need?
        return csvPath;
    }//end of getFile


    /**
     * Closes the current CSV file connection and resets the singleton instance.
     * It allows for a new CSV database to be opened via {@link #setFileName(String)}.
     */
    public void close() {
        instance = null;
        csvPath = null;
    }//end of close


   /**
     * This method verifies if the csv file exists and validates read/write access
     * to the file. It also initializes the Singleton {@code ConnectionCSVFile} instance.
     * <p>
     * This method will throw an exception if an instance is already active. Use {@link #close()}
     * to reset before opening a new database.
     *
     * @param databaseName the name (or path) of the CSV database file
     * @throws IOException if the instance is already initialized, or if the file cannot be created, read, or written
     */
    public static void setFileName(String databaseName) throws IOException{
        if (instance != null){
            throw new IOException("File already set. Close before opening another csv database");
        }

        Path csvPath = FileSystems.getDefault().getPath(databaseName);

        if(!Files.exists(csvPath)) {
            throw new NoSuchFileException( String.format("THE CSV FILE \"%s\" DOES NOT EXIST", csvPath ));
        }
        if (!Files.isReadable(csvPath)) {
            throw new IOException(String.format("COULD NOT OPEN THE CSV FILE \"%s\"", csvPath));
        }
        if(!Files.isWritable(csvPath)){
            throw new IOException(String.format("CANNOT WRITE TO THE CSV FILE \"%s\"", csvPath));
        }

        instance = new ConnectionCSVFile();
        instance.csvPath = csvPath;
    }//end of setFileName

}//end of ConnectionCSVFile class
