package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;
import java.sql.Time;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// line 73 "model.ump"
// line 157 "model.ump"
@Entity
public class CustomHours
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CustomHours Attributes
  @Id
  private String name;          // if multiple days exist for same reason, "Christmas Break Day 1, ... etc"
  private String description;
  private Date date;
  private Time openTime;
  private Time closeTime;

  //CustomHours Associations
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Schedule schedule;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public CustomHours() {}
  public CustomHours(String aName, String aDescription, Date aDate, Time aOpenTime, Time aCloseTime, Schedule aSchedule)
  {
    name = aName;
    description = aDescription;
    date = aDate;
    openTime = aOpenTime;
    closeTime = aCloseTime;
    if (!setSchedule(aSchedule))
    {
      throw new RuntimeException("Unable to create CustomHours due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
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

  public boolean setOpenTime(Time aOpenTime)
  {
    boolean wasSet = false;
    openTime = aOpenTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setCloseTime(Time aCloseTime)
  {
    boolean wasSet = false;
    closeTime = aCloseTime;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getOpenTime()
  {
    return openTime;
  }

  public Time getCloseTime()
  {
    return closeTime;
  }
  /* Code from template association_GetOne */
  public Schedule getSchedule()
  {
    return schedule;
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

  public void delete()
  {
    schedule = null;
  }
}