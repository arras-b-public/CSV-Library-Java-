package CSVLibrary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public abstract class WritingTemplate {

    /**
     * main appending algorithm. Can either write one row, or multiple ones at a time
     * Note that, if any error occur during any step, the database will NOT be written to
     * and will therefore be in the same state as before the method call
     * @param filepath the path of the csv database
     * @return {@code true} if successfully written to, {@code false} otherwise
     */
    public final boolean append(Path filepath) {//make it final so no child can override it
        Path tmp = step1_MakeTempFile(filepath);

        if (!step2_CopyFile(filepath, tmp)){return false;}//cannot safely write to the file

        String stringToWrite = step3_BuildStringToWrite();
        boolean success = false;

        if (!stringToWrite.isEmpty()) {//if the string is valid, try to write it. Otherwise, skip this.
            success = step4_WriteStringToFile(filepath, stringToWrite);

            if (!success) step5_Recover(filepath, tmp);
        }

        step6_DeleteTemporaryFile(tmp);

        return success;
    }//end of append






    private Path step1_MakeTempFile(Path filepath){
        return filepath.resolveSibling(filepath.getFileName() + ".tmp");
    }//end of step1_makeTempFile

    private static boolean step2_CopyFile(Path source, Path dest){
            try{
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                return true;
            }catch(IOException _){
                return false;
            }

    }//end of twoCopyFile


    abstract String step3_BuildStringToWrite();


    private static boolean step4_WriteStringToFile(Path filepath, String content){
        try {
            Files.writeString(filepath, content, StandardOpenOption.APPEND);
        }catch (IOException _){
            return false;
        }

        return true;
    }//end of threeWriteRowToFile


    private static void step5_Recover(Path original, Path temp){//make original the same as before, i.e. the same as temp
        step2_CopyFile(temp, original);
    }//end of step5_Recover


    private static void step6_DeleteTemporaryFile(Path tmpFile){
        try{
            Files.delete(tmpFile);
        }catch(IOException _) {}//do nothing
        //worst case-scenario, the file stays, but will get overwritten (instead of making a new tmp file) during
        //the next addRow call, it doesn't really matter
    }//end of fourDeleteTemporaryFile

}//end of WritingTemplate class
