
package CSVLibrary;


import java.util.ArrayList;
import java.util.List;


/**
 * represents one entry of a Student in the csv database
*/
public class StudentOption extends CSVObject {
    private final String lastName;
    private final String firstName;
    private final String degree;
    private final float CGPA;
    private final int nbColumns = 4;
    private final static String header = "lastName,firstName,degree,CGPA" + lineSeparator;


    public StudentOption(StudentOptionBuilder builder){
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.degree = builder.degree;
        this.CGPA = builder.CGPA;
    }//end of StudentOption constructor


    private StudentOption(String[] fields){
        this.lastName = fields[0];
        this.firstName = fields[1];
        this.degree = fields[2];
        this.CGPA = Float.parseFloat(fields[3]);
    }//end of StudentOption constructor


    public static StudentOption getDummyObject(){
        return new StudentOption.StudentOptionBuilder().build();
    }//end of getDummyObject


    String getHeader(){
        return header;
    }//end of getHeader


    int getNbColumns(){return nbColumns;}


    List<String> getRow(){
        List<String> row = new ArrayList<>(nbColumns);
        String cgpa = String.format("%.3f", CGPA);

        row.add(lastName);
        row.add(firstName);
        row.add(degree);
        row.add(cgpa);
        return row;
    }//end of getRow



    StudentOption convertFromStringToObject(String csvRow){
        String[] fields = csvRow.split(",");
        return new StudentOption(fields);
    }//end of convertFromStringToObject


    public String toString() {
         return "Last Name: " + lastName + ",First Name: " + firstName +
                " ,Degree: " + degree + " ,CGPA: " + String.format("%.3f", CGPA);
    }//end of toString

    /**
     * Builder for a StudentObject.<p>
     * Each column (entry in the csv file) has a default value.
     * For {@code lastName}, {@code firstName} and {@code degree}, it is the empty string
     * For {@code CGPA}, it is -1.0
    */
    public static class StudentOptionBuilder implements CSVFieldFormatBuilder{
        private String lastName = "";
        private String firstName = "";
        private String degree = "";
        private float CGPA = (float) -1.0;

        public StudentOptionBuilder lastName(String lastName){
            this.lastName = removeSpaces(lastName);
            return this;
        }//end of lastName


        public StudentOptionBuilder firstName(String firstName){
            this.firstName = removeSpaces(firstName);
            return this;
        }//end of firstName


        public StudentOptionBuilder degree(String degree){
            this.degree = removeSpaces(degree);
            return this;
        }//end of degree


        public StudentOptionBuilder CGPA(float CGPA){
            this.CGPA = CGPA;
            return this;
        }//end of CGPA


        public StudentOption build(){
            return new StudentOption(this);
        }//end of Build

    }//end of StudentOptionBuilder nested class

}//end of StudentOption class

