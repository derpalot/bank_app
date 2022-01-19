package ATM_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private Card card;

    @BeforeEach
    public void setup() {
        //functional card
        card = new Card("12345", "1234", 100.00, "2019-01-01", "2029-02-02");
    }

    @Test
    public void getCurrentAmountTest() {
        //check current balance in card
        assertEquals(100, card.getCurrentAmount());

        //incorrent current balance
        assertFalse(50 == card.getCurrentAmount());

        Card brokeCard = new Card("78234", "7394", -10.00, "2018-03-02", "2022-02-12");

        //check negative balance
        assertEquals(-10, brokeCard.getCurrentAmount());

        //incorrect balance
        assertFalse(10 == brokeCard.getCurrentAmount());
    }

    @Test
    public void getCardNumberTest() {
        //correct card number
        assertEquals("12345", card.getCardNumber());

        //incorrect card number
        assertFalse(card.getCardNumber().equals("54321"));
    }

    @Test
    public void getPinNumberTest() {
        //correct pin number
        assertEquals("1234", card.getPinNumber());

        //incorrect pin number
        assertFalse(card.getPinNumber().equals("4321"));
    }

    @Test
    public void getStartDateTest() {
        //correct start date
        assertEquals("2019-01-01", card.getStartDate().toString());

        //incorrect start date
        assertFalse(card.getStartDate().toString().equals("2021-02-02"));
    }

    @Test
    public void getExpiryDateTest() {
        //correct expiry date
        assertEquals("2029-02-02", card.getExpiryDate().toString());

        //incorrect expiry date
        assertFalse(card.getExpiryDate().toString().equals("2025-05-05"));
    }

    @Test
    public void isStolenOrLostAndReportTest() {
        //card not stolen
        assertFalse(card.isStolenOrLost());

        //report card stolen
        card.report();

        //card is stolen
        assertTrue(card.isStolenOrLost());
    }

    @Test void inDateTest() {
        //card in date
        assertTrue(card.inDate());

        //bad start date card
        Card badStartCard = new Card("78942", "9234", 50, "2023-01-02", "2023-12-06");
        assertFalse(badStartCard.inDate());

        //bad expiry date card
        Card badExpiryCard = new Card("90123", "0104", 1, "2019-08-22", "2017-12-06");
        assertFalse(badExpiryCard.inDate());
    }

    @Test
    public void checkDetailsTest() {
        //correct details
        assertTrue(card.checkDetails("12345","1234"));

        //incorrect pin
        assertFalse(card.checkDetails("12345", "0000"));

        //incorrect card num
        assertFalse(card.checkDetails("00000", "1234"));

        //incorrect pin and card num
        assertFalse(card.checkDetails("47293" ,"3728"));

        //correct details but no attempts
        assertFalse(card.checkDetails("12345", "1234"));

        //incorrect pin and no attempts
        assertFalse(card.checkDetails("12345", "0000"));

        //incorrect card num and no attempts
        assertFalse(card.checkDetails("00000", "1234"));

        //incorrect pin and card length
        assertFalse(card.checkDetails("1234", "00000"));

        //null card num and pin
        assertFalse(card.checkDetails(null, null));

        //empty card num and pin
        assertFalse(card.checkDetails("", ""));
    }

    @Test
    public void loginAttemptsTest() {
        assertEquals(card.getNumAttempts(), 3);
        Card differentCard = new Card("54321", "0000", 100.00, "2019-01-01", "2029-02-02");

        // enter incorrect pin
        card.checkDetails("12345", "7469");

        // has lost one attempt
        assertEquals(card.getNumAttempts(), 2);

        // correct pin
        card.checkDetails("12345", "1234");

        // restart attempts after login in
        assertEquals(card.getNumAttempts(), 3);

        // see if card is blocked before tries
        assertFalse(card.isBlocked());

        // too many tries, card is blocked
        card.checkDetails("12345", "4623");
        card.checkDetails("12345", "0000");
        card.checkDetails("12345", "7293");

        // card is blocked and has no tries
        assertEquals(card.getNumAttempts(), 0);
        assertTrue(card.isBlocked());

        // can still login with a different valid card
        assertEquals(differentCard.getNumAttempts(), 3);
    }

    @Test
    public void withdrawTest() {
        //negative withdraw
        assertEquals(-1 , card.withdraw(-20));
        assertEquals(100, card.getCurrentAmount());

        //withdraw too much
        assertEquals(-2 , card.withdraw(101));
        assertEquals(100, card.getCurrentAmount());

        //notes withdraw
        assertEquals(80.0, card.withdraw(20));
        assertEquals(80, card.getCurrentAmount());

        //coin withdraw
        assertEquals(65.60, card.withdraw(14.40));
        assertEquals(65.60, card.getCurrentAmount());

        //invalid withdraw
        assertEquals(-3, card.withdraw(20.123));
        assertEquals(65.60, card.getCurrentAmount());

        assertEquals(-3, card.withdraw(.509));
        assertEquals(65.60, card.getCurrentAmount());
    }

    @Test
    public void depositTest() {
        //normal deposit
        assertEquals(120, card.deposit(20));
        assertEquals(120, card.getCurrentAmount());

        //coin deposit
        assertEquals(-2, card.deposit(2));
        assertEquals(120, card.getCurrentAmount());

        assertEquals(-2, card.deposit(1));
        assertEquals(120, card.getCurrentAmount());

        assertEquals(-2, card.deposit(0.5));
        assertEquals(120, card.getCurrentAmount());

        assertEquals(-2, card.deposit(0.2));
        assertEquals(120, card.getCurrentAmount());

        assertEquals(-2, card.deposit(0.1));
        assertEquals(120, card.getCurrentAmount());

        assertEquals(-2, card.deposit(0.05));
        assertEquals(120, card.getCurrentAmount());

        //negative deposit
        assertEquals(-1, card.deposit(-20));
        assertEquals(120, card.getCurrentAmount());

        //invalid deposit
        assertEquals(-2, card.deposit(10.20));
        assertEquals(120, card.getCurrentAmount());
    }

    @Test
    public void normalCardUsageTest() {
        //initialise check
        assertTrue(card.inDate());
        assertFalse(card.isStolenOrLost());
        assertTrue(card.checkDetails("12345", "1234"));
        assertEquals(100, card.getCurrentAmount());

        //normal deposit
        assertEquals(135, card.deposit(35));
        assertEquals(135, card.getCurrentAmount());

        //normal withdraw
        assertEquals(86.50, card.withdraw(48.50));
        assertEquals(86.50, card.getCurrentAmount());

        //bad withdraw
        assertEquals(-3, card.withdraw(48.5015));
        assertEquals(86.50, card.getCurrentAmount());

        assertEquals(-2, card.withdraw(200));
        assertEquals(86.50, card.getCurrentAmount());

        assertEquals(-1, card.withdraw(-10.50));
        assertEquals(86.50, card.getCurrentAmount());

        //bad deposit
        assertEquals(-2, card.deposit(24));
        assertEquals(86.50, card.getCurrentAmount());

        assertEquals(-1, card.deposit(-20));
        assertEquals(86.50, card.getCurrentAmount());

        //another withdraw
        assertEquals(84.50, card.withdraw(2));
        assertEquals(84.50, card.getCurrentAmount());

        //another deposit
        assertEquals(164.50, card.deposit(80));
        assertEquals(164.50, card.getCurrentAmount());

        //wrong pin blocked card
        card.checkDetails("12345", "4623");
        card.checkDetails("12345", "0000");
        card.checkDetails("12345", "7293");
        assertTrue(card.isBlocked());

        //lost or stolen
        card.report();
        assertTrue(card.isStolenOrLost());

        //no longer valid to use
        assertTrue(card.isStolenOrLost());
        assertTrue(card.inDate());
    }
}
