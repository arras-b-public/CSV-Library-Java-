package CSVLibrary;

import java.util.List;

public abstract class CSVObject {
    static final String lineSeparator = System.lineSeparator();


    abstract String getHeader();

    abstract int getNbColumns();


    /**
     * a way to get all columns of a CSVFormat object. Each entry is in order
     * @return a {@link List} of {@link String}, each representing one column
     */
    abstract List<String> getRow();

    /**
     * @precondition THIS METHOD ASSUMES THAT THE GIVEN STRING IS VALID
     * @param csvRow csv row that contains all the fields. Must be comma separated
     * @return new StudentOption object with the fields stored in the input string
     */
    abstract CSVObject convertFromStringToObject(String csvRow);


    /**
     * converts a CSV object into valid csv string that includes commas as separator
     * and ends each row with a line separator according to the user's OS
     * @return valid csv row ready to be written into the csv file
     */
    String formatToCSV(){
        return String.join(",", this.getRow()) + lineSeparator;//row to append inside file
    }//end of formatToCSV


    /**
     * verifies if the row is a valid csv entry. This means that it includes no spaces,
     * is separated only by commas, and ends with the appropriate line terminator
     * @param row the row to verify
     * @return {@code true} if is valid, {@code false} otherwise.
     */
    boolean isValidCSVEntry(String row){
        boolean noSpaces = !row.contains(" ");
        boolean commas = countNbCommas(row) == this.getNbColumns() - 1;

        return noSpaces && commas;
    }//end of isValidCSVEntry


    static int countNbCommas(String row){
        if (row == null || row.isEmpty()){return 0;}
        int commas = 0;

        for (int i = 0; i < row.length(); i++){
            if (row.charAt(i) == ','){commas++;}
        }

        return commas;
    }//end of countNbCommas

}//end of CSVFormat class