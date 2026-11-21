package CSVLibrary;

public class WriteOneRowString extends WritingTemplate{
    private final String row;


    public WriteOneRowString(String row){
        this.row = row;
    }//end of WriteOneRowString constructor


    @Override
    String step3_BuildStringToWrite() {
        return row;
    }//end of step3_BuildStringToWrite
}//end of WriteOneRowString class
