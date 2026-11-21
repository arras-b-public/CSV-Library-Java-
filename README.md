# CSV Library (Java)

A small Java library for using CSV files as a simple flat-file database. It handles creating files, appending rows, and reading rows back either as raw strings or as typed objects.

This is a small project built to practice object-oriented design in Java, mainly the Builder, Singleton, and Template Method patterns.

## Overview

The library works with three built-in row types:

- **StudentOption**: last name, first name, degree, CGPA
- **ScheduleOption**: day, start time, end time, class
- **CustomOption**: up to three user-defined string fields

A database is opened (or created if it does not exist) with a file name and a row type. From then on, all writes must match that type, and reads can return rows as objects of that type.

## Features

- Create a new CSV file with the correct header, or open an existing one
- Append a single row or a list of rows
- Read the file one line at a time, all at once, or as typed objects
- Fluent builders for constructing rows
- Safe appends: the file is backed up to a temporary copy before writing and restored if the write fails
- Custom rows only write the columns that are actually used (1 to 3)

## Design

- **Builder**: each row type has a nested builder class implementing `CSVFieldFormatBuilder`. The row types have different fields, so the interface only requires `build()` and each builder defines its own setters.
- **Singleton**: `ConnectionCSVFile` allows only one open file at a time. It checks that the file exists and is readable and writable, and the connection must be closed before opening another file.
- **Template Method**: `WritingTemplate.append()` defines the write procedure (back up, build the string, write, recover on failure, clean up). `WriteOneRow`, `WriteMultipleRows`, and `WriteOneRowString` only supply the string to write.
- **Abstract base class**: `CSVObject` defines the shared behavior for all row types (formatting to a CSV line, validating a row).
- **In-memory cache**: `DatabaseInteractions` loads the file into memory when opened, so reads do not touch the disk. Writes update both the file and the cached rows.

## Project structure

```
CSVLibrary/
    CSVObject.java               abstract base for all row types
    CSVFieldFormatBuilder.java   builder interface
    StudentOption.java
    ScheduleOption.java
    CustomOption.java
    ConnectionCSVFile.java       singleton file connection
    DatabaseInteractions.java    main entry point for the library
    WritingTemplate.java         safe append algorithm
    WriteOneRow.java
    WriteMultipleRows.java
    WriteOneRowString.java
    program.java                 demo
```

## Requirements

- Java 22 or newer (the code uses unnamed variables, `catch (IOException _)`)
- No external dependencies

## Build and run

From the directory that contains the `CSVLibrary` folder:

```
javac -d out CSVLibrary/*.java
java -cp out CSVLibrary.program
```

The demo creates `testFileStudent.csv`, `testFileSchedule.csv`, and `testFileCustom.csv` in the working directory, writes ten rows to each, and prints a few of them.

## Usage

```java
// Open the database, or create it if it does not exist
DatabaseInteractions db =
        DatabaseInteractions.openOrCreate("students.csv", StudentOption.getDummyObject());

// Build a row
StudentOption student = new StudentOption.StudentOptionBuilder()
        .firstName("Jane")
        .lastName("Doe")
        .degree("CS")
        .CGPA(3.8f)
        .build();

// Write
db.appendRow(student);
// db.appendMultipleRows(listOfRows);

// Read
String line = db.readLine();                              // next line, header skipped
StudentOption row = (StudentOption) db.readLineAsObject(); // next line as an object
List<String> all = db.readAllLines();                      // everything, including header

// Close before opening a different file
db.closeCSVFile();
```

For a custom database, pass the number of columns you want to use:

```java
DatabaseInteractions db =
        DatabaseInteractions.openOrCreate("custom.csv", CustomOption.getDummyObject(2));
```

`program.java` contains a full example for all three row types.

## Limitations

- Values cannot contain commas, and spaces are removed from all input. Quoted fields are not supported.
- Only one file can be open at a time.
- The whole file is loaded into memory, so this is not meant for large files.
- Row types are fixed to the three above. 
