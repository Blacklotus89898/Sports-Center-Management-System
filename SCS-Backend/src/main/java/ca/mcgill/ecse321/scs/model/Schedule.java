package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// line 62 "model.ump"
// line 146 "model.ump"
@Entity
public class Schedule
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayOfWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Schedule Attributes
  @Id
  private int year;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  public Schedule() {}
  public Schedule(int aYear)
  {
    year = aYear;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setYear(int aYear)
  {
    boolean wasSet = false;
    year = aYear;
    wasSet = true;
    return wasSet;
  }

  public int getYear()
  {
    return year;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "year" + ":" + getYear()+ "]";
  }
}


