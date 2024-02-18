package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Time;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;

// line 67 "model.ump"
// line 152 "model.ump"
@Entity
public class OpeningHours
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayOfWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //OpeningHours Attributes
  @Id
  @Enumerated(EnumType.STRING)
  @Column(unique = true)
  private DayOfWeek dayOfWeek;
  private Time openTime;
  private Time closeTime;

  //OpeningHours Associations
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Schedule schedule;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  public OpeningHours() {}
  public OpeningHours(DayOfWeek aDayOfWeek, Time aOpenTime, Time aCloseTime, Schedule aSchedule)
  {
    dayOfWeek = aDayOfWeek;
    openTime = aOpenTime;
    closeTime = aCloseTime;
    if (!setSchedule(aSchedule))
    {
      throw new RuntimeException("Unable to create OpeningHours due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDayOfWeek(DayOfWeek aDayOfWeek)
  {
    boolean wasSet = false;
    dayOfWeek = aDayOfWeek;
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

  public DayOfWeek getDayOfWeek()
  {
    return dayOfWeek;
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