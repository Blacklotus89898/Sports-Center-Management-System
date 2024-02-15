package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;

import jakarta.persistence.Entity;

// line 20 "model.ump"
// line 115 "model.ump"
@Entity
public class Instructor extends Staff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
 
  public Instructor() {}
  public Instructor(int aAccountId, Date aCreationDate, String aName, String aEmail, String aPassword)
  {
    super(aAccountId, aCreationDate, aName, aEmail, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}