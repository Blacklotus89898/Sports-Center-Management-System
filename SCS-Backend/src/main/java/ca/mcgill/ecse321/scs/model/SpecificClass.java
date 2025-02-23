package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;
import java.sql.Time;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

// line 43 "model.ump"
// line 133 "model.ump"
@Entity
public class SpecificClass
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificClass Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int classId;
  private String specificClassName;
  private String description;
  private Date date;
  private Time startTime;
  private float hourDuration;
  private int maxCapacity;
  private int currentCapacity;
  private double registrationFee;
  
  //SpecificClass Associations
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ClassType classType;
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Schedule schedule;
  
  // Additional field
  @Lob
  private byte[] image;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  public SpecificClass() {}
  public SpecificClass(int aClassId, String aSpecificClassName, String aDescription, Date aDate, Time aStartTime, float aHourDuration, int aMaxCapacity, int aCurrentCapacity, double aRegistrationFee, ClassType aClassType, Schedule aSchedule, byte[] aImage)
  {
    classId = aClassId;
    specificClassName = aSpecificClassName;
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
    if (!setSchedule(aSchedule))
    {
      throw new RuntimeException("Unable to create SpecificClass due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    image = aImage;
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

  public boolean setSpecificClassName(String aSpecificClassName)
  {
    boolean wasSet = false;
    specificClassName = aSpecificClassName;
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

  public boolean setHourDuration(float aHourDuration)
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

  public String getSpecificClassName()
  {
    return specificClassName;
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

  public float getHourDuration()
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
  /* Code from template association_GetOne */
  public Schedule getSchedule()
  {
    return schedule;
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
  /* Code from template association_SetUnidirectionalOne */
  public boolean setSchedule(Schedule aNewSchedule)
  {
    boolean wasSet = false;
    if (aNewSchedule != null)
    {
      schedule = aNewSchedule;
      wasSet = true;
    }
    return wasSet;
  }
  
  public byte[] getImage() {
    return image;
  }
  
  public void setImage(byte[] aImage) {
    image = aImage;
  }

  public void delete()
  {
    classType = null;
    schedule = null;
  }
}