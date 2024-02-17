package ca.mcgill.ecse321.scs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 55 "model.ump"
// line 139 "model.ump"
@Entity
public class ClassType
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ClassType Attributes
  @Id
  private String className;
  private String description;
  private boolean isApproved;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ClassType() {}

  public ClassType(String aClassName, String aDescription, boolean aIsApproved)
  {
    className = aClassName;
    description = aDescription;
    isApproved = aIsApproved;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setClassName(String aClassName)
  {
    boolean wasSet = false;
    className = aClassName;
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

  public boolean setIsApproved(boolean aIsApproved)
  {
    boolean wasSet = false;
    isApproved = aIsApproved;
    wasSet = true;
    return wasSet;
  }

  public String getClassName()
  {
    return className;
  }

  public String getDescription()
  {
    return description;
  }

  public boolean getIsApproved()
  {
    return isApproved;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsApproved()
  {
    return isApproved;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "className" + ":" + getClassName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "isApproved" + ":" + getIsApproved()+ "]";
  }
}