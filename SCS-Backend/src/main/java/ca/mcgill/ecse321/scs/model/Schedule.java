package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;
import java.sql.Time;
import java.sql.Date;

// line 61 "model.ump"
// line 144 "model.ump"
public class Schedule
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayOfWeek { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Schedule Associations
  private List<OpeningHours> openingHours;
  private List<CustomHours> customHours;
  private List<SpecificClass> specificClasses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Schedule()
  {
    openingHours = new ArrayList<OpeningHours>();
    customHours = new ArrayList<CustomHours>();
    specificClasses = new ArrayList<SpecificClass>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public OpeningHours getOpeningHour(int index)
  {
    OpeningHours aOpeningHour = openingHours.get(index);
    return aOpeningHour;
  }

  public List<OpeningHours> getOpeningHours()
  {
    List<OpeningHours> newOpeningHours = Collections.unmodifiableList(openingHours);
    return newOpeningHours;
  }

  public int numberOfOpeningHours()
  {
    int number = openingHours.size();
    return number;
  }

  public boolean hasOpeningHours()
  {
    boolean has = openingHours.size() > 0;
    return has;
  }

  public int indexOfOpeningHour(OpeningHours aOpeningHour)
  {
    int index = openingHours.indexOf(aOpeningHour);
    return index;
  }
  /* Code from template association_GetMany */
  public CustomHours getCustomHour(int index)
  {
    CustomHours aCustomHour = customHours.get(index);
    return aCustomHour;
  }

  public List<CustomHours> getCustomHours()
  {
    List<CustomHours> newCustomHours = Collections.unmodifiableList(customHours);
    return newCustomHours;
  }

  public int numberOfCustomHours()
  {
    int number = customHours.size();
    return number;
  }

  public boolean hasCustomHours()
  {
    boolean has = customHours.size() > 0;
    return has;
  }

  public int indexOfCustomHour(CustomHours aCustomHour)
  {
    int index = customHours.indexOf(aCustomHour);
    return index;
  }
  /* Code from template association_GetMany */
  public SpecificClass getSpecificClass(int index)
  {
    SpecificClass aSpecificClass = specificClasses.get(index);
    return aSpecificClass;
  }

  public List<SpecificClass> getSpecificClasses()
  {
    List<SpecificClass> newSpecificClasses = Collections.unmodifiableList(specificClasses);
    return newSpecificClasses;
  }

  public int numberOfSpecificClasses()
  {
    int number = specificClasses.size();
    return number;
  }

  public boolean hasSpecificClasses()
  {
    boolean has = specificClasses.size() > 0;
    return has;
  }

  public int indexOfSpecificClass(SpecificClass aSpecificClass)
  {
    int index = specificClasses.indexOf(aSpecificClass);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOpeningHours()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfOpeningHours()
  {
    return 7;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addOpeningHour(OpeningHours aOpeningHour)
  {
    boolean wasAdded = false;
    if (openingHours.contains(aOpeningHour)) { return false; }
    if (numberOfOpeningHours() < maximumNumberOfOpeningHours())
    {
      openingHours.add(aOpeningHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeOpeningHour(OpeningHours aOpeningHour)
  {
    boolean wasRemoved = false;
    if (openingHours.contains(aOpeningHour))
    {
      openingHours.remove(aOpeningHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setOpeningHours(OpeningHours... newOpeningHours)
  {
    boolean wasSet = false;
    ArrayList<OpeningHours> verifiedOpeningHours = new ArrayList<OpeningHours>();
    for (OpeningHours aOpeningHour : newOpeningHours)
    {
      if (verifiedOpeningHours.contains(aOpeningHour))
      {
        continue;
      }
      verifiedOpeningHours.add(aOpeningHour);
    }

    if (verifiedOpeningHours.size() != newOpeningHours.length || verifiedOpeningHours.size() > maximumNumberOfOpeningHours())
    {
      return wasSet;
    }

    openingHours.clear();
    openingHours.addAll(verifiedOpeningHours);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOpeningHourAt(OpeningHours aOpeningHour, int index)
  {  
    boolean wasAdded = false;
    if(addOpeningHour(aOpeningHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOpeningHours()) { index = numberOfOpeningHours() - 1; }
      openingHours.remove(aOpeningHour);
      openingHours.add(index, aOpeningHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOpeningHourAt(OpeningHours aOpeningHour, int index)
  {
    boolean wasAdded = false;
    if(openingHours.contains(aOpeningHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOpeningHours()) { index = numberOfOpeningHours() - 1; }
      openingHours.remove(aOpeningHour);
      openingHours.add(index, aOpeningHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOpeningHourAt(aOpeningHour, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomHours()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfCustomHours()
  {
    return 366;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addCustomHour(CustomHours aCustomHour)
  {
    boolean wasAdded = false;
    if (customHours.contains(aCustomHour)) { return false; }
    if (numberOfCustomHours() < maximumNumberOfCustomHours())
    {
      customHours.add(aCustomHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeCustomHour(CustomHours aCustomHour)
  {
    boolean wasRemoved = false;
    if (customHours.contains(aCustomHour))
    {
      customHours.remove(aCustomHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setCustomHours(CustomHours... newCustomHours)
  {
    boolean wasSet = false;
    ArrayList<CustomHours> verifiedCustomHours = new ArrayList<CustomHours>();
    for (CustomHours aCustomHour : newCustomHours)
    {
      if (verifiedCustomHours.contains(aCustomHour))
      {
        continue;
      }
      verifiedCustomHours.add(aCustomHour);
    }

    if (verifiedCustomHours.size() != newCustomHours.length || verifiedCustomHours.size() > maximumNumberOfCustomHours())
    {
      return wasSet;
    }

    customHours.clear();
    customHours.addAll(verifiedCustomHours);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomHourAt(CustomHours aCustomHour, int index)
  {  
    boolean wasAdded = false;
    if(addCustomHour(aCustomHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomHours()) { index = numberOfCustomHours() - 1; }
      customHours.remove(aCustomHour);
      customHours.add(index, aCustomHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomHourAt(CustomHours aCustomHour, int index)
  {
    boolean wasAdded = false;
    if(customHours.contains(aCustomHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomHours()) { index = numberOfCustomHours() - 1; }
      customHours.remove(aCustomHour);
      customHours.add(index, aCustomHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomHourAt(aCustomHour, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSpecificClasses()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSpecificClass(SpecificClass aSpecificClass)
  {
    boolean wasAdded = false;
    if (specificClasses.contains(aSpecificClass)) { return false; }
    specificClasses.add(aSpecificClass);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSpecificClass(SpecificClass aSpecificClass)
  {
    boolean wasRemoved = false;
    if (specificClasses.contains(aSpecificClass))
    {
      specificClasses.remove(aSpecificClass);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSpecificClassAt(SpecificClass aSpecificClass, int index)
  {  
    boolean wasAdded = false;
    if(addSpecificClass(aSpecificClass))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecificClasses()) { index = numberOfSpecificClasses() - 1; }
      specificClasses.remove(aSpecificClass);
      specificClasses.add(index, aSpecificClass);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSpecificClassAt(SpecificClass aSpecificClass, int index)
  {
    boolean wasAdded = false;
    if(specificClasses.contains(aSpecificClass))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecificClasses()) { index = numberOfSpecificClasses() - 1; }
      specificClasses.remove(aSpecificClass);
      specificClasses.add(index, aSpecificClass);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSpecificClassAt(aSpecificClass, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    openingHours.clear();
    customHours.clear();
    specificClasses.clear();
  }

}