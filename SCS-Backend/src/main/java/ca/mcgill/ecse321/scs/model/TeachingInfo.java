package ca.mcgill.ecse321.scs.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 81 "model.ump"
// line 162 "model.ump"
@Entity
public class TeachingInfo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TeachingInfo Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int teachingInfoId;

  //TeachingInfo Associations
  @ManyToOne
  private Instructor instructor;
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private SpecificClass specificClass;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public TeachingInfo() {}
  public TeachingInfo(int aTeachingInfoId, Instructor aInstructor, SpecificClass aSpecificClass)
  {
    teachingInfoId = aTeachingInfoId;
    if (!setInstructor(aInstructor))
    {
      throw new RuntimeException("Unable to create TeachingInfo due to aInstructor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setSpecificClass(aSpecificClass))
    {
      throw new RuntimeException("Unable to create TeachingInfo due to aSpecificClass. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTeachingInfoId(int aTeachingInfoId)
  {
    boolean wasSet = false;
    teachingInfoId = aTeachingInfoId;
    wasSet = true;
    return wasSet;
  }

  public int getTeachingInfoId()
  {
    return teachingInfoId;
  }
  /* Code from template association_GetOne */
  public Instructor getInstructor()
  {
    return instructor;
  }
  /* Code from template association_GetOne */
  public SpecificClass getSpecificClass()
  {
    return specificClass;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setInstructor(Instructor aNewInstructor)
  {
    boolean wasSet = false;
    if (aNewInstructor != null)
    {
      instructor = aNewInstructor;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setSpecificClass(SpecificClass aNewSpecificClass)
  {
    boolean wasSet = false;
    if (aNewSpecificClass != null)
    {
      specificClass = aNewSpecificClass;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    instructor = null;
    specificClass = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "teachingInfoId" + ":" + getTeachingInfoId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "instructor = "+(getInstructor()!=null?Integer.toHexString(System.identityHashCode(getInstructor())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "specificClass = "+(getSpecificClass()!=null?Integer.toHexString(System.identityHashCode(getSpecificClass())):"null");
  }
}