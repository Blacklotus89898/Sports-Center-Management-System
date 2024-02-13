package ca.mcgill.ecse321.scs.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 29 "model.ump"
// line 128 "model.ump"
public class PaymentMethod
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentMethod Attributes
  private int cardNumber;
  private int expiryMonth;
  private int expiryYear;
  private int securityCode;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentMethod(int aCardNumber, int aExpiryMonth, int aExpiryYear, int aSecurityCode)
  {
    cardNumber = aCardNumber;
    expiryMonth = aExpiryMonth;
    expiryYear = aExpiryYear;
    securityCode = aSecurityCode;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCardNumber(int aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiryMonth(int aExpiryMonth)
  {
    boolean wasSet = false;
    expiryMonth = aExpiryMonth;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiryYear(int aExpiryYear)
  {
    boolean wasSet = false;
    expiryYear = aExpiryYear;
    wasSet = true;
    return wasSet;
  }

  public boolean setSecurityCode(int aSecurityCode)
  {
    boolean wasSet = false;
    securityCode = aSecurityCode;
    wasSet = true;
    return wasSet;
  }

  public int getCardNumber()
  {
    return cardNumber;
  }

  public int getExpiryMonth()
  {
    return expiryMonth;
  }

  public int getExpiryYear()
  {
    return expiryYear;
  }

  public int getSecurityCode()
  {
    return securityCode;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "cardNumber" + ":" + getCardNumber()+ "," +
            "expiryMonth" + ":" + getExpiryMonth()+ "," +
            "expiryYear" + ":" + getExpiryYear()+ "," +
            "securityCode" + ":" + getSecurityCode()+ "]";
  }
}