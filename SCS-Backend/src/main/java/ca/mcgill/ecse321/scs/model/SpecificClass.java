package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;
import java.sql.Time;

// line 43 "model.ump"
// line 133 "model.ump"
public class SpecificClass
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificClass Attributes
  private int classId;
  private String description;
  private Date date;
  private Time startTime;
  private int hourDuration;
  private int maxCapacity;
  private int currentCapacity;
  private double registrationFee;

  //SpecificClass Associations
  private ClassType classType;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificClass(int aClassId, String aDescription, Date aDate, Time aStartTime, int aHourDuration, int aMaxCapacity, int aCurrentCapacity, double aRegistrationFee, ClassType aClassType)
  {
    classId = aClassId;
    description = aDescription;
    date = aDate;
    startTime = aStartTime;
    hourDuration = aHourDuration;
    maxCapacity = aMaxCapacity;
    currentCapacity = aCurrentCapacity;
    registrationFee = aRegistrationFee;
    if (!setClassType(aClassType))
    {
      throw new RuntimeException("Unable to create SpecificClass due to aClassType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setClassId(int aClassId)
  {
    boolean wasSet = false;
    classId = aClassId;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setHourDuration(int aHourDuration)
  {
    boolean wasSet = false;
    hourDuration = aHourDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxCapacity(int aMaxCapacity)
  {
    boolean wasSet = false;
    maxCapacity = aMaxCapacity;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentCapacity(int aCurrentCapacity)
  {
    boolean wasSet = false;
    currentCapacity = aCurrentCapacity;
    wasSet = true;
    return wasSet;
  }

  public boolean setRegistrationFee(double aRegistrationFee)
  {
    boolean wasSet = false;
    registrationFee = aRegistrationFee;
    wasSet = true;
    return wasSet;
  }

  public int getClassId()
  {
    return classId;
  }

  public String getDescription()
  {
    return description;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public int getHourDuration()
  {
    return hourDuration;
  }

  public int getMaxCapacity()
  {
    return maxCapacity;
  }

  public int getCurrentCapacity()
  {
    return currentCapacity;
  }

  public double getRegistrationFee()
  {
    return registrationFee;
  }
  /* Code from template association_GetOne */
  public ClassType getClassType()
  {
    return classType;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setClassType(ClassType aNewClassType)
  {
    boolean wasSet = false;
    if (aNewClassType != null)
    {
      classType = aNewClassType;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    classType = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "classId" + ":" + getClassId()+ "," +
            "description" + ":" + getDescription()+ "," +
            "hourDuration" + ":" + getHourDuration()+ "," +
            "maxCapacity" + ":" + getMaxCapacity()+ "," +
            "currentCapacity" + ":" + getCurrentCapacity()+ "," +
            "registrationFee" + ":" + getRegistrationFee()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "classType = "+(getClassType()!=null?Integer.toHexString(System.identityHashCode(getClassType())):"null");
  }
}