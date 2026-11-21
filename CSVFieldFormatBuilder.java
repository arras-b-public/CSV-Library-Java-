package CSVLibrary;

public interface CSVFieldFormatBuilder {

    //FOR THR TA READING:
    /*
    I decided to implement builders for each CSV types in nested classes of their respective type. This way we don't have to
    have a class with all possible fields, if we are only going to use a small portion of them. Moreover, since all 3 types
    have nothing in common, it is hard to implement an interface that defines all methods. For instance, a student
    needs a lastName, thus we need a builder method for it, but a schedule object doesn't, so it does not make sense
    to enforce having it in an interface. Instead, all nested Builder must only implement the build method (which the same
    for all types) and they are free to have any setField method they want.
     */

    CSVObject build();

    default String removeSpaces(String input){
        return input.replaceAll(" ","");
    }//end of removeSpaces


}//end of CSVFieldBuilder class
