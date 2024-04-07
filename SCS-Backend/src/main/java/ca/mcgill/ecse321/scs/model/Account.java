package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
// line 2 "model.ump"
// line 100 "model.ump"
@MappedSuperclass
public abstract class Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Account Attributes
  @Id
  @GeneratedValue(generator = "account_id_sequence_generator", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "account_id_sequence_generator", sequenceName = "account_id_seq", allocationSize = 1)
  private int accountId;        // seq generator to ensure all users accross tables will have unique ids!
  private Date creationDate;
  private String name;
  private String email;
  private String password;
  
  @Lob
  private byte[] image;

  //------------------------
  // CONSTRUCTOR
  public Account () {
    
  }
  //------------------------

  public Account(int aAccountId, Date aCreationDate, String aName, String aEmail, String aPassword, byte[] aImage)
  {
    accountId = aAccountId;
    creationDate = aCreationDate;
    name = aName;
    email = aEmail;
    password = aPassword;
    image = aImage;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAccountId(int aAccountId)
  {
    boolean wasSet = false;
    accountId = aAccountId;
    wasSet = true;
    return wasSet;
  }

  public boolean setCreationDate(Date aCreationDate)
  {
    boolean wasSet = false;
    creationDate = aCreationDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }
  
  public boolean setImage(byte[] aImage)
  {
    boolean wasSet = false;
    image = aImage;
    wasSet = true;
    return wasSet;
  }

  public int getAccountId()
  {
    return accountId;
  }

  public Date getCreationDate()
  {
    return creationDate;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }
  
  public byte[] getImage()
  {
    return image;
  }

  public void delete()
  {}
}