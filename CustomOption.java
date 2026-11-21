package CSVLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomOption extends CSVObject {
    private final List<String> fields;
    private final int nbColumns;
    private final String header;


    public CustomOption (CustomOptionBuilder builder){
        this.fields = new ArrayList<>();

        //no need to add non-used fields, which having default value the empty string, by our builder
        if(!builder.fieldOne.isEmpty()) {fields.add(builder.fieldOne);}
        if(!builder.fieldTwo.isEmpty()) {fields.add(builder.fieldTwo);}
        if(!builder.fieldThree.isEmpty()) {fields.add(builder.fieldThree);}

        this.nbColumns = builder.nbFields;
        this.header = buildHeader();
    }//end of CustomOption constructor


    private CustomOption(List<String> fields){
        this.fields = fields;
        this.nbColumns = fields.size();
        this.header = buildHeader();
    }//end of CustomOption constructor


    public static CustomOption getDummyObject(int nbCustomFields){
        return new CustomOption.CustomOptionBuilder().setNbCustomFields(nbCustomFields).build();
    }//end of getDummyObject


    private String buildHeader(){
        //build header dynamically, depending on how much custom fields the user is using
        String[] s = {"One", "Two", "Three"};
        String header = "";
        for (int i = 0; i < this.nbColumns; i++) {
            header += "customField" + s[i] + ",";
        }
        header  = header.replaceFirst(",$", lineSeparator);//replace the last "," by a line separator
        return header;
    }//end of buildHeader


    public String getHeader(){
        return header;
    }//end of getHeader


    public int getNbColumns(){return nbColumns;}


    public List<String> getRow(){
        //return only the correct amount of fields
        return fields.subList(0, nbColumns);
    }//end of getRow


    public CustomOption convertFromStringToObject(String csvRow){
        List<String> fields = new ArrayList<>(Arrays.asList(csvRow.split(",")));
        return new CustomOption(fields);
    }//end of convertFromStringToObject


    public String toString(){
        String s = "";
        for (int i = 0; i < nbColumns; i++) {
            s += "Custom Field " + (i+1) + ": " + fields.get(i) + " ,";
        }
        s = s.replaceFirst(" ,$", "");//remove the last space
        return s;
    }//end of toString


    /**
     * Builder for a CustomOption.<p>
     * Each column (entry in the csv file) has a default value of an empty string.
     */
    public static class CustomOptionBuilder implements CSVFieldFormatBuilder{
        //default values, instead of null
        private String fieldOne = "";
        private String fieldTwo = "";
        private String fieldThree = "";
        private int nbFields = 0;


        public CustomOption.CustomOptionBuilder customFieldOne(String fieldOne){
            this.fieldOne = removeSpaces(fieldOne);
            this.nbFields ++;
            return this;
        }//end of customFieldOne


        public CustomOption.CustomOptionBuilder customFieldTwo(String fieldTwo){
            this.fieldTwo = removeSpaces(fieldTwo);
            this.nbFields++;
            return this;
        }//end of customFieldTwo


        public CustomOption.CustomOptionBuilder customFieldThree(String fieldThree){
            this.fieldThree = removeSpaces(fieldThree);
            this.nbFields++;
            return this;
        }//end of customFieldThree


        private CustomOption.CustomOptionBuilder setNbCustomFields(int n){
            this.nbFields = n;
            return this;
        }//end of setNbCustomFields

        public CustomOption build(){
            return new CustomOption(this);
        }//end of build

    }//end of CustomOptionBuilder nested class

}//end of CustomOption class

