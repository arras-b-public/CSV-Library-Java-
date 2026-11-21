package CSVLibrary;

import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class program {
    private static DatabaseInteractions lib;

    public static void main(String[] args) throws Exception {

        /*
        How to use the library:
        open the db (or create it, if it does not already exist). All interactions to it are done via the
        DatabaseInteractions object, which require a filename and a database type to be instantiated.
        The type specifies what type of database we are interacting with and therefore expects input of the same type
        when writing.
        You must close the current database and instantiate a new one if you want to change database
         */



        String fileName = "testFileStudent.csv";//will make a file in the current working directory of the IDE
        lib = DatabaseInteractions.openOrCreate(fileName, StudentOption.getDummyObject());
        //right now, the database is in "Student Mode" it expects student object as input for writing operations
        //and can return StudentOption objects when reading as well as regular strings


        //Making some random objects

        //Students:
        String[] lastNames = {"a","b","c","c","d","e","f","g","h","i"};
        String[] firstNames = {"1","2","3","4","5","6","7","8","9","10"};
        String[] degrees = {"Math", "CS", "Engineering", "Economics", "Education", "Philosophy", "Art", "Environment", "Languages", "Law"};
        double[] cgpas = {4.00, 3.90, 3.80, 3.70, 3.60, 3.50, 3.40, 3.30, 3.20, 3.10};//will convert to float later
        List<CSVObject> studentRows = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            StudentOption student = new StudentOption.StudentOptionBuilder()
                    .firstName(firstNames[i])
                    .lastName(lastNames[i])
                    .degree(degrees[i])
                    .CGPA( (float) cgpas[i])
                    .build();
            studentRows.add(i, student);
        }


        //Schedules:
        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        String[] startTimes = {"1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00"};
        String[] endTimes = {"2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00"};
        String[] courses = {"comp301","comp302","comp303","comp304","comp305","comp306","comp307","comp308","comp309","comp310"};
        List<CSVObject> scheduleRows = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            ScheduleOption schedule = new ScheduleOption.ScheduleOptionBuilder()
                    .day(days[i])
                    .startTime(startTimes[i])
                    .endTime(endTimes[i])
                    .course(courses[i])
                    .build();
            scheduleRows.add(i, schedule);
        }


        //Customs:
        String[] customOne = {"customOne1","customOne2","customOne3","customOne4",
                "customOne5","customOne6","customOne6","customOne7","customOne8", "customOne9", "customOne10"};

        String[] customTwo = {"customTwo1","customTwo2","customTwo3","customTwo4",
                "customTwo5","customTwo6","customTwo6","customTwo7","customTwo8", "customTwo9", "customTwo10"};
        List<CSVObject> customRows = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            CustomOption custom = new CustomOption.CustomOptionBuilder()
                    .customFieldOne(customOne[i])
                    .customFieldTwo(customTwo[i])
                    .build();

            customRows.add(i, custom);
        }




        //Student demo:
        //show how to read/write, the client can then manipulate the data as he wishes

            //write multiple objects at the same time to the DB
            lib.appendMultipleRows(studentRows);
            //note that the user can also write one row with appendOneRow();

            //read all lines
            List<String> allStudentRows = lib.readAllLines();
            //do something with this....

            //read separate line
            String firstLineStudent = lib.readLine();
            String secondLineStudent = lib.readLine();

            System.out.println("First 3 lines of the student database:");
            System.out.println(firstLineStudent);
            System.out.println(secondLineStudent);

            //read separate line as StudentObject
            StudentOption thirdLineStudent = (StudentOption) lib.readLineAsObject();
            System.out.println(thirdLineStudent.toString());
            System.out.println();

            lib.closeCSVFile();//close the current connection, so we can make a new connection to another csv database

            //If we want to access a new database, we have to get a new DatabaseInteraction object
            //(this is because, under the hood, each database Interaction object holds the csv file content
            //in memory, so when we change database, we must also clear it and read the new content)
            //this is because java.nio.Files only allows to read the entire file at once.



        //Schedule demo:
        //show how to use read/write, the client can then manipulate the data as he wishes

        fileName = "testFileSchedule.csv";//this will make a file in the current working directory of the IDE
        lib = DatabaseInteractions.openOrCreate(fileName, ScheduleOption.getDummyObject());
        //interact with the student db

        lib.appendMultipleRows(scheduleRows);

        //read all lines
        List<String> allScheduleRows = lib.readAllLines();
        //do something with it...

        //read separate line
        String firstLineSchedule = lib.readLine();
        String secondLineSchedule = lib.readLine();

        System.out.println("First 3 lines of the schedule database:");
        System.out.println(firstLineSchedule);
        System.out.println(secondLineSchedule);

        //read separate line as StudentObject
        ScheduleOption thirdLineSchedule = (ScheduleOption) lib.readLineAsObject();
        System.out.println(thirdLineSchedule.toString());
        System.out.println();

        lib.closeCSVFile();//close the current connection, so we can make a new connection to another csv database



        //Custom demo:
        //show how to use read/write, the client can then manipulate the data as he wishes

        fileName = "testFileCustom.csv";//this will make a file in the current working directory of the IDE
        lib = DatabaseInteractions.openOrCreate(fileName, CustomOption.getDummyObject(2));
        //for this demo, we will only use 2 fields out of the 3, to show that this library is dynamic, meaning
        //that it only interacts with the correct number of field and does not add necessary empty values in the db
        //if not all 3 custom fields are used

        lib.appendMultipleRows(customRows);

        //read all lines
        List<String> allCustomRows = lib.readAllLines();
        //do something with it...

        //read separate line
        String firstLineCustom = lib.readLine();
        String secondLineCustom = lib.readLine();

        System.out.println("First 3 lines of the custom database:");
        System.out.println(firstLineCustom);
        System.out.println(secondLineCustom);

        //read separate line as CustomObject
        CustomOption thirdLineCustom = (CustomOption) lib.readLineAsObject();
        System.out.println(thirdLineCustom.toString());//print it as a csv row entry
        System.out.println();

        lib.closeCSVFile();//close the current connection, so we can make a new connection to another csv database

        //You can also go and view the database files manually to confirm that all 10 entries for each database was written
    }//end of main
}
