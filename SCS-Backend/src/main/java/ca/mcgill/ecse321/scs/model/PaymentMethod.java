package ca.mcgill.ecse321.scs.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 29 "model.ump"
// line 129 "model.ump"
@Entity
public class PaymentMethod
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentMethod Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int paymentId;
  private long cardNumber;
  private int expiryMonth;
  private int expiryYear;
  private int securityCode;

  //PaymentMethod Associations
  @OneToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public PaymentMethod() {}

  public PaymentMethod(long aCardNumber, int aExpiryMonth, int aExpiryYear, int aSecurityCode, int aPaymentId, Customer aCustomer)
  {
    cardNumber = aCardNumber;
    expiryMonth = aExpiryMonth;
    expiryYear = aExpiryYear;
    securityCode = aSecurityCode;
    paymentId = aPaymentId;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create PaymentMethod due to aCustomer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCardNumber(long aCardNumber)
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

  public boolean setPaymentId(int aPaymentId)
  {
    boolean wasSet = false;
    paymentId = aPaymentId;
    wasSet = true;
    return wasSet;
  }

  public long getCardNumber()
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

  public int getPaymentId()
  {
    return paymentId;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
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

  public void delete()
  {
    customer = null;
  }

  public String toString()
  {
    return super.toString() + "["+
            "cardNumber" + ":" + getCardNumber()+ "," +
            "expiryMonth" + ":" + getExpiryMonth()+ "," +
            "expiryYear" + ":" + getExpiryYear()+ "," +
            "securityCode" + ":" + getSecurityCode()+ "," +
            "paymentId" + ":" + getPaymentId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}