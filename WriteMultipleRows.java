package CSVLibrary;

import java.util.List;

public class WriteMultipleRows extends WritingTemplate{
    private final List<CSVObject> rows;

        public WriteMultipleRows(List<CSVObject> rows){
        this.rows = rows;
    }//end of WritingMultipleRows constructor


    @Override
    String step3_BuildStringToWrite(){
        StringBuilder sb = new StringBuilder();

        for (CSVObject curRow : rows){
            sb.append(curRow.formatToCSV());
        }

        return sb.toString();
    }//end of step3_BuildStringToWrite

}//end of WriteMultipleRows class