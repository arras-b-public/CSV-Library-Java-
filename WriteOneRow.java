package CSVLibrary;

public class WriteOneRow extends WritingTemplate{
    private final CSVObject row;

    public WriteOneRow(CSVObject row){
        this.row = row;
    }//end of WritingOneRow constructor


    @Override
    String step3_BuildStringToWrite(){
        return row.formatToCSV();
    }//end of step3_BuildStringToWrite


}//end of WriteOneRow class
