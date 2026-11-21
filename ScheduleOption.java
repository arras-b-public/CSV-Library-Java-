package CSVLibrary;


import java.util.ArrayList;
import java.util.List;

public class ScheduleOption extends CSVObject {
    private final String day;
    private final String startTime;
    private final String endTime;
    private final String course;
    private final int nbColumns = 4;
    private final static String header = "day,startTime,endTime,class" + lineSeparator;

    public ScheduleOption(ScheduleOptionBuilder builder){
        this.day = builder.day;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.course = builder.course;
    }//end of ScheduleOption constructor


    private ScheduleOption(String[] fields){
        this.day = fields[0];
        this.startTime = fields[1];
        this.endTime = fields[2];
        this.course = fields[3];
    }//end of ScheduleOption constructor


    public static ScheduleOption getDummyObject(){
        return new ScheduleOption.ScheduleOptionBuilder().build();
    }//end of getDummyObject


    String getHeader(){
        return header;
    }//end of getHeader


    int getNbColumns(){return nbColumns;}


    List<String> getRow(){
        List<String> row = new ArrayList<>(nbColumns);
        row.add(day);
        row.add(startTime);
        row.add(endTime);
        row.add(course);
        return row;
    }//end of getRow


    ScheduleOption convertFromStringToObject(String csvRow){
        String[] fields = csvRow.split(",");
        return new ScheduleOption(fields);
    }//end of convertFromStringToObject


    public String toString(){
        return "Day: " + day + " ,Start Time: " + startTime + " ,End Time: " + endTime + " ,Class: " + course;
    }//end of toString

    /**
     * Builder for a ScheduleOption object.<p>
     * Each column (entry in the csv file) has a default value.
     * For {@code day}, {@code startTime} and {@code endTime}, and {@code course}, it is the empty string
     */
    public static class ScheduleOptionBuilder implements CSVFieldFormatBuilder{
        private String day = "";
        private String startTime = "";
        private String endTime = "";
        private String course = "";


        public ScheduleOptionBuilder day(String day){
            this.day = removeSpaces(day);
            return this;
        }//end of day


        public ScheduleOptionBuilder startTime(String startTime){
            this.startTime = removeSpaces(startTime);
            return this;
        }//end of startTime


        public ScheduleOptionBuilder endTime(String endTime){
            this.endTime = removeSpaces(endTime);
            return this;
        }//end of endTime


        public ScheduleOptionBuilder course(String course){
            this.course = removeSpaces(course);
            return this;
        }//end of course


        public ScheduleOption build(){
            return new ScheduleOption(this);
        }//end of build

    }//end of ScheduleOptionBuilder nested class

}//end of ScheduleOption class
