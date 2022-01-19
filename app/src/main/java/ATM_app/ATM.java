package ATM_app;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.text.DecimalFormat;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ATM {
    private ArrayList<Card> cardList;
    private HashMap<Double, Integer> moneyAmounts;
    private Double balance;
    private Integer numTransactions;
    private String maintenancePin;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public ATM(String cardFile, String ATMFile) throws IOException, ParseException{
        this.cardList = new ArrayList<Card>();
        this.moneyAmounts = new HashMap<Double, Integer>();
        this.balance = 0.00;
        this.numTransactions = 0;
        this.constructCards(cardFile);
        this.moneyAmountsSetUp(ATMFile);
    }

    public ArrayList<Card> getCardList() {
        return this.cardList;
    }

    public Double getBalance() {
      return this.balance;
    }

    public HashMap<Double, Integer> getMoneyAmounts() {
        return this.moneyAmounts;
    }

    public Integer getNumTransactions() {
        return this.numTransactions;
    }

    private void setNumTransactions() {
        this.numTransactions++;
    }

    private void setBalance(Double amount) {
        // amount is pos for a deposit and neg for withdrawal
        amount = (double) Math.round(amount *100);
        amount = amount/100;
        this.balance += amount;
        this.balance = (double) Math.round(this.balance *100);
        this.balance = this.balance/100;
    }

    private void setMoneyAmounts(Double key, Integer num) {
        // num is pos for a deposit and neg for withdrawal

        for (Map.Entry<Double, Integer> entry : this.moneyAmounts.entrySet()) {
            Double k = entry.getKey();
            if (k.equals(key)) {
                //entry.put(val, entry.get(val) + num);
                entry.setValue(entry.getValue() + num);
            }
        }
    }

    public Card checkCardNum(String num) {
        for (Card card : cardList) {
            if(num.equals(card.getCardNumber())) {
                return card;
            }
        }
        return null;
    }

    public boolean removeCard(String num) {
        if(this.checkCardNum(num) != null){
            cardList.remove(this.checkCardNum(num));
            return true;
        }
        return false;
    }

    // Reads in json file of cards and constructs Card objects of valid cards
    // and adds to ATM validCards list
    private void constructCards(String cardsFile) throws IOException, ParseException {
        // Read in JSON file
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(cardsFile);
        JSONArray cards = (JSONArray) jsonParser.parse(reader);

        for (Object obj : cards) {
            JSONObject cardObj = (JSONObject) obj;

            String num = (String) cardObj.get("cardNumber");
            String pin = (String) cardObj.get("pinNumber");
            boolean lost = (boolean) cardObj.get("lostOrStolen");
            boolean blocked = (boolean) cardObj.get("blocked");
            double funds = (double) cardObj.get("fundsAvailable");
            String start = (String) cardObj.get("startDate");
            String expiry = (String) cardObj.get("expiryDate");

            try{
                Card card = new Card(num, pin, funds, start, expiry);

                if(lost) {
                    card.report();
                }
                if(blocked) {
                    card.block();
                }

                if(pin.length() == 4 && num.length() == 5) {
                    if(checkCardNum(card.getCardNumber()) == null){
                        cardList.add(card);
                    }
                }
            } catch (Exception ex) {
                continue;
            }
        }

    }

    private void moneyAmountsSetUp(String moneyFile) throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader(moneyFile));
        JSONObject moneyObj = (JSONObject) obj;

        Long hundred = (Long) moneyObj.get("100.0");
        Long fifty = (Long) moneyObj.get("50.0");
        Long twenty = (Long) moneyObj.get("20.0");
        Long ten = (Long) moneyObj.get("10.0");
        Long five = (Long) moneyObj.get("5.0");
        Long two = (Long) moneyObj.get("2.0");
        Long one = (Long) moneyObj.get("1.0");
        Long fiftyCent = (Long) moneyObj.get("0.5");
        Long twentyCent = (Long) moneyObj.get("0.2");
        Long tenCent = (Long) moneyObj.get("0.1");
        Long fiveCent = (Long) moneyObj.get("0.05");
        Long numTransactions = (Long) moneyObj.get("Transaction Number");
        double balance = (double) moneyObj.get("Balance");
        String maintenancePin = (String) moneyObj.get("Maintenance Pin");

        this.balance = balance;
        this.numTransactions = numTransactions.intValue();
        this.maintenancePin = maintenancePin;

        this.moneyAmounts.put(100.00, hundred.intValue());
        this.moneyAmounts.put(50.00, fifty.intValue());
        this.moneyAmounts.put(20.00, twenty.intValue());
        this.moneyAmounts.put(10.00, ten.intValue());
        this.moneyAmounts.put(5.00, five.intValue());
        this.moneyAmounts.put(2.00, two.intValue());
        this.moneyAmounts.put(1.00, one.intValue());
        this.moneyAmounts.put(0.50, fiftyCent.intValue());
        this.moneyAmounts.put(0.20, twentyCent.intValue());
        this.moneyAmounts.put(0.10, tenCent.intValue());
        this.moneyAmounts.put(0.05, fiveCent.intValue());
    }

    public String deposit(Card card, HashMap<Double, Integer> noteQuantities) {
        Double amount = 0.00;

        //check for negative quantities
        for (Map.Entry<Double, Integer> entry : noteQuantities.entrySet()) {
            Integer quant = entry.getValue();
            if (quant < 0) {
                return "Note quantities should not be negative. ";
            }
        }

        // calculating amount being deposited
        for (Map.Entry<Double, Integer> entry : noteQuantities.entrySet()) {
            Double val = entry.getKey();
            Integer quant = entry.getValue();
            this.setMoneyAmounts(val, quant);
            amount += (val * quant);
        }

        if (amount <= 0) {
            return "You must deposit more than $0.00. ";
        }

        card.deposit(amount);
        this.setBalance(amount);
        this.setNumTransactions();

        // add \n if neccessary for outputting to GUI
        // this is a very basic recipt format for the time being
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transaction number: " + this.getNumTransactions() + "\n");
        stringBuilder.append("Transaction Type: Deposit\n");
        stringBuilder.append("Amount Deposited: $" + df.format(amount) + "\n");
        stringBuilder.append("New Account Balance: $" + df.format(card.getCurrentAmount()));

        return stringBuilder.toString();

    }

    public String withdraw(double totalAmount, Card card) {
        if(totalAmount < 0) {
            return "Please enter a positive withdrawal amount. ";
        }

        Double amountLeft = totalAmount;

        HashMap<Double, Integer> moneyUsed = new HashMap<Double, Integer>();
        // ATM insufficient funds
        if (totalAmount > this.getBalance()) {
            // Error msg here
            return "ATM has insufficient funds, please call maintenance or try again later. ";
        }

        TreeMap<Double, Integer> sortMoney = new TreeMap<Double, Integer>(this.moneyAmounts);
        Map<Double, Integer> sortedMoney = sortMoney.descendingMap();

        // calculate the notes and coins to give the user
        for (Map.Entry<Double, Integer> entry : sortedMoney.entrySet()){
            while (true) {
                amountLeft = (double) Math.round(amountLeft *100);
                amountLeft = amountLeft/100;
                if(amountLeft >= entry.getKey() && entry.getValue() > 0) {
                    amountLeft -= entry.getKey();
                    this.setMoneyAmounts(entry.getKey(), -1);
                    if (moneyUsed.containsKey(entry.getKey())) {
                        Integer prevAmount = moneyUsed.get(entry.getKey());
                        moneyUsed.put(entry.getKey(), prevAmount + 1);
                    }
                    else {
                        moneyUsed.put(entry.getKey(), 1);
                    }
                } else {
                  break;
                }
            }
        }

        if(!df.format(amountLeft).equals("0.00")) {
          // Could not withdraw with notes and coins
          // Need to undo the setMoneyAmounts done in above for loop
          // Need to add some kind of error message to GUI
          for (Map.Entry<Double, Integer> entry : moneyUsed.entrySet()) {
                this.setMoneyAmounts(entry.getKey(), entry.getValue());
          }
          return "Could not dispense the amount specified, please try again later. ";
        }

        double errno = card.withdraw(totalAmount);
        if (errno == -2) {
            for (Map.Entry<Double, Integer> entry : moneyUsed.entrySet()) {
                  this.setMoneyAmounts(entry.getKey(), entry.getValue());
            }
            return "This card has insufficient funds. Your balance is: " + card.getCurrentAmount();
        }
        this.setBalance((-1) * totalAmount);
        this.setNumTransactions();

        // withdrawal complete return receipt
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transaction number: " + this.getNumTransactions() + "\n");
        stringBuilder.append("Transaction Type: Withdraw\n");
        stringBuilder.append("Amount Withdrawn: $" + df.format(totalAmount) + "\n");
        stringBuilder.append("New Account Balance: $" + df.format(card.getCurrentAmount()));

        return stringBuilder.toString();
    }

    public String maintenance(HashMap<Double, Integer> money, String pin){
        if(!pin.equals(this.maintenancePin)) {
            return "Incorrect Maintenance Pin. ";
        }
        Double amount = 0.0;

        for (Map.Entry<Double, Integer> entry : money.entrySet()) {
            Integer val = entry.getValue();
            if (val < 0) {
                return "Note quantities should not be negative. ";
            }
        }

        // calculating amount being deposited
        for (Map.Entry<Double, Integer> entry : money.entrySet()) {
            Double key = entry.getKey();
            Integer val = entry.getValue();
            this.setMoneyAmounts(key, val);
            amount += (key * val);
        }

        if (amount <= 0.00) {
            return "Value added must be greater than $0.00. ";
        }

        this.setBalance(amount);
        return "Success ";
        //this.setNumTransactions();
    }

    public boolean saveATMInfo(String ATMFile) throws IOException{
        try {
            File saveFile = new File(ATMFile);
            saveFile.createNewFile();
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<Double, Integer> entry : moneyAmounts.entrySet()){
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            jsonObject.put("Transaction Number", this.numTransactions);
            jsonObject.put("Maintenance Pin", this.maintenancePin);
            jsonObject.put("Balance", this.balance);
            FileWriter file = new FileWriter(ATMFile);
            file.write(jsonObject.toJSONString());
            file.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
     }

    public boolean saveCardInfo(String cardFile) throws IOException{
        try {
            File saveFile = new File(cardFile);
            saveFile.createNewFile();
            JSONArray jsonArray = new JSONArray();
            for (Card card: this.cardList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cardNumber", card.getCardNumber());
                jsonObject.put("pinNumber", card.getPinNumber());
                jsonObject.put("lostOrStolen", card.isStolenOrLost());
                jsonObject.put("blocked", card.isBlocked());
                jsonObject.put("fundsAvailable",card.getCurrentAmount());
                jsonObject.put("startDate", card.getStartDate().toString());
                jsonObject.put("expiryDate", card.getExpiryDate().toString());
                jsonArray.add(jsonObject);
            }
            FileWriter file = new FileWriter(cardFile);
            file.write(jsonArray.toJSONString());
            file.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
