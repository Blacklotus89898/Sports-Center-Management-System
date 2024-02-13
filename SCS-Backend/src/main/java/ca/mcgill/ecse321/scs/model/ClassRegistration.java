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



// line 36 "model.ump"
// line 170 "model.ump"
@Entity
public class ClassRegistration
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ClassRegistration Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int registrationId;

  //ClassRegistration Associations
  @ManyToOne
  private Customer customer;                    // TODO: ClassRegistration not deleted for logging purposes, not sure if needed.
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private SpecificClass specificClass;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ClassRegistration(int aRegistrationId, Customer aCustomer, SpecificClass aSpecificClass)
  {
    registrationId = aRegistrationId;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create ClassRegistration due to aCustomer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setSpecificClass(aSpecificClass))
    {
      throw new RuntimeException("Unable to create ClassRegistration due to aSpecificClass. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRegistrationId(int aRegistrationId)
  {
    boolean wasSet = false;
    registrationId = aRegistrationId;
    wasSet = true;
    return wasSet;
  }

  public int getRegistrationId()
  {
    return registrationId;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public SpecificClass getSpecificClass()
  {
    return specificClass;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
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
    customer = null;
    specificClass = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "registrationId" + ":" + getRegistrationId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "specificClass = "+(getSpecificClass()!=null?Integer.toHexString(System.identityHashCode(getSpecificClass())):"null");
  }
}