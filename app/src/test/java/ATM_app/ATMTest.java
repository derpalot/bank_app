package ATM_app;

import java.io.IOException;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {
    private ATM emptyATM;
    private Card card;
    private ArrayList<Card> cards;
    private HashMap<Double, Integer> maintenanceMoney;

    @BeforeEach
    public void setup() throws IOException, ParseException {
        emptyATM = new ATM("src/test/resources/TestCards.json",
                      "src/test/resources/EmptyATM.json");

        //emptyATM = new ATM("src/test/resources/AfterTest/cardInfo.json",
        //              "src/test/resources/AfterTest/ATMInfo.json");

        cards = new ArrayList<Card>();

        card = emptyATM.checkCardNum("12345");

        //Expected Card List
        //Normal card
        Card cardOne = new Card("12345", "1234", 1000, "2019-01-01", "2022-01-01");
        Card cardTwo = new Card("54321", "8321", 20, "2019-02-02", "2022-01-01");

        //Expired card
        Card cardFour = new Card("19876", "6789", 558.15, "2013-04-04", "2016-02-02");

        //Bad start date
        Card cardFive = new Card("29876", "6289", 512.80, "2025-04-04", "2016-02-02");

        //Bad Expiry date
        Card cardThree = new Card("67891", "1987", 2000, "2022-03-03", "2025-03-03");

        //stolen card
        Card cardSix = new Card("89022", "8902", 300.50, "2019-07-07", "2023-01-01");
        cardSix.report();

        //OutDated, stolen card
        Card cardSeven = new Card("35981", "9314", 17.50, "2019-07-07", "2022-01-01");
        cardSeven.report();
        cardSeven.block();

        cards.add(cardOne);
        cards.add(cardTwo);
        cards.add(cardThree);
        cards.add(cardFour);
        cards.add(cardFive);
        cards.add(cardSix);
        cards.add(cardSeven);

        //maintenanceMoney 17,385
        maintenanceMoney = new HashMap<Double, Integer>();
        maintenanceMoney.put(100.00, 50);
        maintenanceMoney.put(50.00, 100);
        maintenanceMoney.put(20.00, 200);
        maintenanceMoney.put(10.00, 200);
        maintenanceMoney.put(5.00, 200);
        maintenanceMoney.put(2.00, 100);
        maintenanceMoney.put(1.00, 100);
        maintenanceMoney.put(0.50, 100);
        maintenanceMoney.put(0.20, 100);
        maintenanceMoney.put(0.10, 100);
        maintenanceMoney.put(0.05, 100);
    }

    @Test
    public void removeCard() {
        //card llist equals to emptyATM card list
        assertEquals(cards, emptyATM.getCardList());

        //remove card from emptyATM cards list
        assertTrue(emptyATM.removeCard("12345"));

        //cards not equals
        assertFalse(cards.equals(emptyATM.getCardList()));

        //remove card from cards list
        cards.remove(card);

        //cards and emptyATm card list equal
        assertEquals(cards, emptyATM.getCardList());

        //nonexistent card
        assertFalse(emptyATM.removeCard("99999"));
    }

    @Test
    public void constructorTest() {
        //Expected HashMap of ATM notes
        HashMap<Double, Integer> noMoney = new HashMap<Double, Integer>();
        noMoney.put(100.00, 0);
        noMoney.put(50.00, 0);
        noMoney.put(20.00, 0);
        noMoney.put(10.00, 0);
        noMoney.put(5.00, 0);
        noMoney.put(2.00, 0);
        noMoney.put(1.00, 0);
        noMoney.put(0.50, 0);
        noMoney.put(0.20, 0);
        noMoney.put(0.10, 0);
        noMoney.put(0.05, 0);

        //Incorrect HashMap of ATM Notes
        HashMap<Double, Integer> falseMoney = new HashMap<Double, Integer>();
        falseMoney.put(100.00, 10);
        falseMoney.put(50.00, 7);
        falseMoney.put(20.00, 7);
        falseMoney.put(10.00, 3);
        falseMoney.put(5.00, 0);
        falseMoney.put(2.00, 8);
        falseMoney.put(1.00, 1);
        falseMoney.put(0.50, 23);
        falseMoney.put(0.20, 3);
        falseMoney.put(0.10, 0);
        falseMoney.put(0.05, 0);

        //Initialise wtih EmptyATM.json with no money
        assertEquals(noMoney, emptyATM.getMoneyAmounts());

        //Incorrect HashMap of money
        assertFalse(falseMoney.equals(emptyATM.getMoneyAmounts()));

        ArrayList<Card> wrongList = new ArrayList<Card>();
        Card cardOne = new Card("12346", "6289", 512.80, "2025-04-04", "2016-02-02");
        cardOne.report();
        Card cardTwo = new Card("89022", "8902", 300.50, "2019-07-07", "2023-01-01");
        Card cardThree = new Card("67891", "1987", 2000, "2022-03-03", "2025-03-03");

        wrongList.add(cardOne);
        wrongList.add(cardTwo);
        wrongList.add(cardThree);

        //Initialise with TestCards.json
        assertEquals(cards, emptyATM.getCardList());

        //Incorecct List
        assertFalse(wrongList.equals(emptyATM.getCardList()));
    }

    @Test
    public void getCardListTest() {
        //array list same card details
        assertEquals(cards, emptyATM.getCardList());

        //Incorrect cards array
        ArrayList<Card> wrongList = new ArrayList<Card>();

        Card cardOne = new Card("12346", "6289", 512.80, "2025-04-04", "2016-02-02");
        cardOne.report();
        Card cardTwo = new Card("89022", "8902", 300.50, "2019-07-07", "2023-01-01");
        Card cardThree = new Card("67891", "1987", 2000, "2022-03-03", "2025-03-03");

        wrongList.add(cardOne);
        wrongList.add(cardTwo);
        wrongList.add(cardThree);

        //array list different cards
        assertFalse(cards.equals(wrongList));
        assertFalse(cards.equals(null));
    }

    @Test
    public void depositTest() {
        //User initial funds
        assertEquals(1000, card.getCurrentAmount());

        //User deposit
        HashMap<Double, Integer> userDeposit = new HashMap<Double, Integer>();
        userDeposit.put(100.00, 1);
        userDeposit.put(50.00, 2);
        userDeposit.put(20.00, 4);
        userDeposit.put(10.00, 1);
        userDeposit.put(5.00, 2);
        userDeposit.put(2.00, 0);
        userDeposit.put(1.00, 0);
        userDeposit.put(0.50, 0);
        userDeposit.put(0.20, 0);
        userDeposit.put(0.10, 0);
        userDeposit.put(0.05, 0);
        //total 300

        //receipt
        String receipt = "Transaction number: 1\n" + "Transaction Type: Deposit\n"
                + "Amount Deposited: $300.00\n" + "New Account Balance: $1300.00";

        //deposit transactions
        assertEquals(receipt, emptyATM.deposit(card, userDeposit));

        //User final funds
        assertEquals(1300, card.getCurrentAmount());

        //ATM receive correct notes
        assertEquals(userDeposit, emptyATM.getMoneyAmounts());
        assertEquals(300, emptyATM.getBalance());

        //second deposit
        HashMap<Double, Integer> secDeposit = new HashMap<Double, Integer>();
        secDeposit.put(100.00, 0);
        secDeposit.put(50.00, 1);
        secDeposit.put(20.00, 2);
        secDeposit.put(10.00, 0);
        secDeposit.put(5.00, 2);
        //total 100

        //negative deposit
        HashMap<Double, Integer> negDeposit = new HashMap<Double, Integer>();
        negDeposit.put(100.00, -2);
        negDeposit.put(50.00, -1);
        negDeposit.put(20.00, -4);

        assertEquals("Note quantities should not be negative. ", emptyATM.deposit(card, negDeposit));

        //receipt
        String receiptOne = "Transaction number: 2\n" + "Transaction Type: Deposit\n"
                + "Amount Deposited: $100.00\n" + "New Account Balance: $1400.00";

        //deposit transactions
        assertEquals(receiptOne, emptyATM.deposit(card, secDeposit));

        //User final funds
        assertEquals(1400, card.getCurrentAmount());

        HashMap<Double, Integer> overallDeposit = new HashMap<Double, Integer>();
        overallDeposit.put(100.00, 1);
        overallDeposit.put(50.00, 3);
        overallDeposit.put(20.00, 6);
        overallDeposit.put(10.00, 1);
        overallDeposit.put(5.00, 4);
        overallDeposit.put(2.00, 0);
        overallDeposit.put(1.00, 0);
        overallDeposit.put(0.50, 0);
        overallDeposit.put(0.20, 0);
        overallDeposit.put(0.10, 0);
        overallDeposit.put(0.05, 0);
        //total 400

        //ATM receive correct notes
        assertEquals(overallDeposit, emptyATM.getMoneyAmounts());
        assertEquals(400, emptyATM.getBalance());
    }

    @Test
    public void withdrawTest() {
        //User initial funds
        assertEquals(1000, card.getCurrentAmount());

        //withdraw negative money
        assertEquals("Please enter a positive withdrawal amount. ", emptyATM.withdraw(-10, card));

        //insufficient atm funds
        assertEquals("ATM has insufficient funds, please call maintenance or try again later. ", emptyATM.withdraw(10, card));

        HashMap<Double, Integer> brokeATM = new HashMap<Double, Integer>();
        brokeATM.put(100.00, 0);
        brokeATM.put(50.00, 2);
        brokeATM.put(20.00, 0);
        brokeATM.put(10.00, 0);
        brokeATM.put(5.00, 0);
        brokeATM.put(2.00, 0);
        brokeATM.put(1.00, 0);
        brokeATM.put(0.50, 0);
        brokeATM.put(0.20, 0);
        brokeATM.put(0.10, 0);
        brokeATM.put(0.05, 0);

        //maintenance Empty to have cash for withdraw
        emptyATM.maintenance(brokeATM, "9342");

        //insufficient banks notes to get withdraw amount
        assertEquals("Could not dispense the amount specified, please try again later. ", emptyATM.withdraw(60, card));

        //maintenance EmptyAtm ot have more cash
        emptyATM.maintenance(maintenanceMoney, "9342");

        //insufficient funds in account
        assertEquals("This card has insufficient funds. Your balance is: " + card.getCurrentAmount(), emptyATM.withdraw(1001, card));

        //receipt
        String receipt = "Transaction number: 1\n" + "Transaction Type: Withdraw\n"
                + "Amount Withdrawn: $100.00\n" + "New Account Balance: $900.00";

        //normal withdraw transaction
        assertEquals(receipt, emptyATM.withdraw(100, card));
        assertEquals(900, card.getCurrentAmount());
        assertEquals(emptyATM.getBalance(), 17385);

        //receipt
        String receiptOne = "Transaction number: 2\n" + "Transaction Type: Withdraw\n"
                + "Amount Withdrawn: $56.15\n" + "New Account Balance: $843.85";

        //precision withdraw transaction
        assertEquals(receiptOne, emptyATM.withdraw(56.15, card));
        assertEquals(843.85, card.getCurrentAmount());
        assertEquals(emptyATM.getBalance(), 17328.85);
    }

    @Test
    public void getBalanceTest() {
        //Empty ATM, no money
        assertEquals(0, emptyATM.getBalance());

        //User deposit
        HashMap<Double, Integer> userDeposit = new HashMap<Double, Integer>();
        userDeposit.put(100.00, 1);
        userDeposit.put(50.00, 2);
        userDeposit.put(20.00, 4);
        userDeposit.put(10.00, 1);
        userDeposit.put(5.00, 2);
        userDeposit.put(2.00, 0);
        userDeposit.put(1.00, 0);
        userDeposit.put(0.50, 0);
        userDeposit.put(0.20, 0);
        userDeposit.put(0.10, 0);
        userDeposit.put(0.05, 0);
        //total 300

        //deposit transactions
        emptyATM.deposit(card, userDeposit);

        //balance after deposit
        assertEquals(300, emptyATM.getBalance());

        //withdraw transactions
        emptyATM.withdraw(100, card);

        //balance after withdraw
        assertEquals(200, emptyATM.getBalance());

        //bad withdraw transactions
        emptyATM.withdraw(2000, card);

        //balance after withdraw
        assertEquals(200, emptyATM.getBalance());

        //maintenance
        emptyATM.maintenance(maintenanceMoney, "9342");

        //balance after withdraw
        assertEquals(17585, emptyATM.getBalance());
    }

    @Test
    public void getNumTransactionsTest() {
        //Initialise ATM, no transactiosn
        assertEquals(0, emptyATM.getNumTransactions());

        //User deposit
        HashMap<Double, Integer> userDeposit = new HashMap<Double, Integer>();
        userDeposit.put(100.00, 1);
        userDeposit.put(50.00, 2);
        userDeposit.put(20.00, 4);
        userDeposit.put(10.00, 1);
        userDeposit.put(5.00, 2);

        //Deposit transaction
        emptyATM.deposit(card, userDeposit);

        //Transaction number after deposit
        assertEquals(1, emptyATM.getNumTransactions());

        //Withdraw transaction
        emptyATM.withdraw(70, card);

        //Transaction number after withdraw;
        assertEquals(2, emptyATM.getNumTransactions());

        //Invalid withdraw transaction
        emptyATM.withdraw(20.50, card);
        emptyATM.withdraw(-20, card);
        emptyATM.withdraw(1000, card);

        //Transaction number after withdraw;
        assertEquals(2, emptyATM.getNumTransactions());
    }

    @Test
    public void maintenanceTest() {
        //empty maintenance
        HashMap<Double, Integer> atmEmpty = new HashMap<Double, Integer>();
        atmEmpty.put(100.00, 0);
        atmEmpty.put(50.00, 0);
        atmEmpty.put(20.00, 0);
        atmEmpty.put(10.00, 0);
        atmEmpty.put(5.00, 0);
        atmEmpty.put(2.00, 0);
        atmEmpty.put(1.00, 0);
        atmEmpty.put(0.50, 0);
        atmEmpty.put(0.20, 0);
        atmEmpty.put(0.10, 0);
        atmEmpty.put(0.05, 0);

        //empty default atm
        assertEquals(emptyATM.getMoneyAmounts(), atmEmpty);

        //empty maintenance
        assertEquals("Value added must be greater than $0.00. ", emptyATM.maintenance(atmEmpty, "9342"));

        //neg maintenance
        HashMap<Double, Integer> negMain = new HashMap<Double, Integer>();
        negMain.put(100.00, 100);
        negMain.put(50.00, 0);
        negMain.put(20.00, -4);
        negMain.put(10.00, 10);
        negMain.put(5.00, -12);

        //neg maintenance error message
        assertEquals("Note quantities should not be negative. ", emptyATM.maintenance(negMain, "9342"));

        //atm money amount after neg maintenace
        assertEquals(emptyATM.getMoneyAmounts(), atmEmpty);

        //maintenace input money
        //$17,385.00
        //maintenance add money to atm with correct password
        assertEquals("Success ", emptyATM.maintenance(this.maintenanceMoney, "9342"));

        //atm balance after maintenance
        assertEquals(emptyATM.getBalance(), 17385.00);

        //maintenace notes added
        assertEquals(emptyATM.getMoneyAmounts(), this.maintenanceMoney);
        assertFalse(emptyATM.getMoneyAmounts().equals(atmEmpty));

        //wrong password maintenance
        assertEquals("Incorrect Maintenance Pin. ", emptyATM.maintenance(maintenanceMoney, "8903"));

        //No money added besides from first maintenance
        assertEquals(emptyATM.getMoneyAmounts(), this.maintenanceMoney);
        assertEquals(emptyATM.getBalance(), 17385.00);

        //second top off
        emptyATM.maintenance(maintenanceMoney, "9342");

        HashMap<Double, Integer> secondTop = new HashMap<Double, Integer>();
        secondTop.put(100.00, 100);
        secondTop.put(50.00, 200);
        secondTop.put(20.00, 400);
        secondTop.put(10.00, 400);
        secondTop.put(5.00, 400);
        secondTop.put(2.00, 200);
        secondTop.put(1.00, 200);
        secondTop.put(0.50, 200);
        secondTop.put(0.20, 200);
        secondTop.put(0.10, 200);
        secondTop.put(0.05, 200);

        //second maintenance added
        assertEquals(emptyATM.getMoneyAmounts(), secondTop);

        //balance after second maintenance
        assertEquals(emptyATM.getBalance(), 34770.00);
    }

    @Test
    public void saveATMInfoTest() throws IOException, ParseException{
        //good path
        assertTrue(emptyATM.saveATMInfo("src/test/resources/AfterTest/EmptyATMInfo.json"));

        //bad path
        assertFalse(emptyATM.saveATMInfo("/src/test/resources/AfterTest/EmptyATMInfo.json"));

        //ATM reading from the saved json file of emptyATM
        ATM newATM = new ATM("src/test/resources/AfterTest/cardInfo.json",
                             "src/test/resources/AfterTest/EmptyATMInfo.json");

        //emptyATM and newATM same balance, transactions number and money notes
        assertEquals(emptyATM.getBalance(), newATM.getBalance());
        assertEquals(emptyATM.getNumTransactions(), newATM.getNumTransactions());
        assertEquals(emptyATM.getMoneyAmounts(), newATM.getMoneyAmounts());

        //deposit money
        HashMap<Double, Integer> userDeposit = new HashMap<Double, Integer>();
        userDeposit.put(100.00, 1);
        userDeposit.put(0.50, 1);
        userDeposit.put(0.20, 1);
        userDeposit.put(0.05, 1);

        //transactions done on newATM
        newATM.maintenance(maintenanceMoney, "9342");
        newATM.withdraw(500.50, card);
        newATM.deposit(card, userDeposit);

        //newATM new balance and transaction
        assertEquals(16985.25, newATM.getBalance());
        assertEquals(2,newATM.getNumTransactions());

        //save newATM info
        assertTrue(newATM.saveATMInfo("src/test/resources/AfterTest/saveATMInfo.json"));

        //atm extract data from new json file
        ATM testing = new ATM("src/test/resources/AfterTest/saveCardInfo.json",
                              "src/test/resources/AfterTest/saveATMInfo.json");

        //testing and newATM should have the same balance, transactions number and notes
        assertEquals(testing.getBalance(), newATM.getBalance());
        assertEquals(testing.getNumTransactions(), newATM.getNumTransactions());
        assertEquals(testing.getMoneyAmounts(), newATM.getMoneyAmounts());
    }

    @Test
    public void saveCardInfoTest() throws IOException, ParseException{
        //good path
        assertTrue(emptyATM.saveCardInfo("src/test/resources/AfterTest/cardInfo.json"));

        //bad path
        assertFalse(emptyATM.saveCardInfo("/src/test/resources/AfterTest/cardInfo.json"));

        //ATM to read the saved files
        ATM newATM = new ATM("src/test/resources/AfterTest/cardInfo.json",
                             "src/test/resources/ATMInfo.json");

        //Expected valid card list
        ArrayList<Card> newCardList = new ArrayList<Card>();
        Card cardOne = new Card("12345", "1234", 1000.00, "2019-01-01", "2022-01-01");
        Card cardTwo = new Card("54321", "8321", 20.0, "2019-02-02", "2022-01-01");
        Card cardThree = new Card("67891", "1987", 2000.0, "2022-03-03", "2025-03-03");
        Card cardFour = new Card("19876", "6789", 558.15, "2013-04-04", "2016-02-02");
        Card cardFive = new Card("29876", "6289", 512.8, "2025-04-04", "2016-02-02");
        Card cardSix = new Card("89022", "8902", 300.5, "2019-07-07", "2023-01-01");
        cardSix.report();
        Card cardSeven = new Card("35981", "9314", 17.5, "2019-07-07", "2022-01-01");
        cardSeven.block();
        cardSeven.report();

        newCardList.add(cardOne);
        newCardList.add(cardTwo);
        newCardList.add(cardThree);
        newCardList.add(cardFour);
        newCardList.add(cardFive);
        newCardList.add(cardSix);
        newCardList.add(cardSeven);

        //correct card list
        assertEquals(newCardList, newATM.getCardList());

        //new ATM to test transactions and saving the data
        ATM transATM = new ATM("src/test/resources/saveCardInfo.json",
                               "src/test/resources/ATMInfo.json");

        Card user = transATM.checkCardNum("12345");
        Card userOne = transATM.checkCardNum("19876");
        Card userTwo = transATM.checkCardNum("35981");

        //check card details
        assertEquals(cardOne,user);
        assertEquals(cardFour,userOne);
        System.out.println(cardSeven.isBlocked());
        System.out.println(userTwo.isBlocked());
        assertEquals(cardSeven,userTwo);

        //transactions
        user.withdraw(25.70);
        userOne.report();
        userTwo.deposit(1200);

        //save data
        assertTrue(transATM.saveCardInfo("src/test/resources/AfterTest/saveCardInfo.json"));

        //construct the saved data
        ATM equalsATM = new ATM("src/test/resources/AfterTest/saveCardInfo.json",
                               "src/test/resources/ATMInfo.json");

        //incorrect card list
        assertFalse(transATM.getCardList().equals(emptyATM.getCardList()));

        //correct card list
        assertEquals(transATM.getCardList(), equalsATM.getCardList());
    }

    @Test
    public void compareCardTest() {
        //incorrect card number
        Card cardOne = new Card("62335", "1234", 1000, "2019-01-01", "2022-01-01");

        //incorrect pin number
        Card cardTwo = new Card("12345", "3234", 1000, "2019-01-01", "2022-01-01");

        //incorrect funds
        Card cardThree = new Card("12345", "1234", 500, "2019-01-01", "2022-01-01");

        //incorrect stolen state
        Card cardFour = new Card("12345", "1234", 1000, "2019-01-01", "2022-01-01");
        cardFour.report();

        //incorrect expiry date
        Card cardFive = new Card("12345", "1234", 1000, "2019-01-01", "2029-01-01");

        //incorrect start date
        Card cardSix = new Card("12345", "1234", 1000, "2014-01-01", "2022-01-01");

        assertEquals(card, emptyATM.checkCardNum("12345"));
        assertFalse(card.equals(null));
        assertFalse(card.equals(cardOne));
        assertFalse(card.equals(cardTwo));
        assertFalse(card.equals(cardThree));
        assertFalse(card.equals(cardFour));
        assertFalse(card.equals(cardFive));
        assertFalse(card.equals(cardSix));

        Card cardSeven = new Card("12345", "1234", 1000, "2019-01-01", "2022-01-01");
        cardSeven.block();

        assertFalse(card.equals(cardSeven));
    }

    @Test
    public void generalUsageTest() throws IOException, ParseException {
        //initialising ATM
        //starting balance 13865
        ATM generalATM = new ATM("src/test/resources/saveCardInfo.json",
                                 "src/test/resources/ATMInfo.json");

        //all users in system
        Card user = generalATM.checkCardNum("12345");
        Card userOne = generalATM.checkCardNum("19876");
        Card userTwo = generalATM.checkCardNum("35981");

        //check if all cards are valid
        assertTrue(user.inDate() && !user.isStolenOrLost());
        assertFalse(userOne.inDate() && !userOne.isStolenOrLost());
        assertFalse(userTwo.inDate() && !userTwo.isStolenOrLost());

        //remove invalid cards
        assertTrue(generalATM.removeCard("19876"));
        assertTrue(generalATM.removeCard("35981"));

        //expected cardList after removal
        ArrayList<Card> oneCard = new ArrayList<Card>();
        oneCard.add(user);

        assertEquals(oneCard, generalATM.getCardList());

        //bad withdraw
        assertEquals("Could not dispense the amount specified, please try again later. ",
                     generalATM.withdraw(125.13, user));

        //expected receipt
        String receipt = "Transaction number: 1\n" + "Transaction Type: Withdraw\n"
                + "Amount Withdrawn: $125.10\n" + "New Account Balance: $874.90";

        assertEquals(receipt, generalATM.withdraw(125.10, user));

        //expected receiptOne
        String receiptOne = "Transaction number: 2\n" + "Transaction Type: Withdraw\n"
                + "Amount Withdrawn: $500.95\n" + "New Account Balance: $373.95";

        assertEquals(receiptOne, generalATM.withdraw(500.95, user));

        //expected receiptTwo
        String receiptTwo = "Transaction number: 3\n" + "Transaction Type: Deposit\n"
                + "Amount Deposited: $300.00\n" + "New Account Balance: $673.95";

        //amount deposit
        HashMap<Double, Integer> userDeposit = new HashMap<Double, Integer>();
        userDeposit.put(100.00, 3);
        userDeposit.put(50.00, 0);
        userDeposit.put(20.00, 0);
        userDeposit.put(10.00, 0);
        userDeposit.put(5.00, 0);

        assertEquals(receiptTwo, generalATM.deposit(user, userDeposit));

        //neg deposit
        HashMap<Double, Integer> negDeposit = new HashMap<Double, Integer>();
        negDeposit.put(20.00, 2);
        negDeposit.put(10.00, -2);

        //neg deposit
        assertEquals("Note quantities should not be negative. ",
                     generalATM.deposit(user, negDeposit));

        //zero deposit
        HashMap<Double, Integer> zeroDeposit = new HashMap<Double, Integer>();
        HashMap<Double, Integer> zeroDepositOne = new HashMap<Double, Integer>();
        zeroDeposit.put(100.00, 0);
        zeroDeposit.put(50.00, 0);
        zeroDeposit.put(20.00, 0);
        zeroDeposit.put(10.00, 0);
        zeroDeposit.put(5.00, 0);

        //neg deposit
        assertEquals("You must deposit more than $0.00. ",
                     generalATM.deposit(user, zeroDeposit));
        assertEquals("You must deposit more than $0.00. ",
                     generalATM.deposit(user, zeroDepositOne));

        //bad maintenance
        generalATM.maintenance(maintenanceMoney, "92340");

        //maintenance
        generalATM.maintenance(maintenanceMoney, "82340");

        //ATM balance after maintenace and transactions
        assertEquals(30923.95, generalATM.getBalance());

        //save both atm info and card info to file
        assertTrue(generalATM.saveCardInfo("src/test/resources/AfterTest/generalUseCardInfo.json"));
        assertTrue(generalATM.saveATMInfo("src/test/resources/AfterTest/generalUseATMInfo.json"));

        //construct the saved data
        ATM equalsATM = new ATM("src/test/resources/AfterTest/generalUseCardInfo.json",
                               "src/test/resources/AfterTest/generalUseATMInfo.json");

        //correct card info
        assertEquals(generalATM.getCardList(), equalsATM.getCardList());

        //corect ATM info
        assertEquals(generalATM.getBalance(), equalsATM.getBalance());
        assertEquals(generalATM.getNumTransactions(), equalsATM.getNumTransactions());
        assertEquals(generalATM.getMoneyAmounts(), equalsATM.getMoneyAmounts());
    }
}
