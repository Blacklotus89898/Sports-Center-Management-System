package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;


// line 11 "model.ump"
// line 105 "model.ump"
public abstract class Staff extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Staff() {}
  public Staff(int aAccountId, Date aCreationDate, String aName, String aEmail, String aPassword, byte[] image)
  {
    super(aAccountId, aCreationDate, aName, aEmail, aPassword, image);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}