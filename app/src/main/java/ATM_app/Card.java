package ATM_app;

import java.time.LocalDate;

public class Card {
    private String cardNumber;
    private String pinNumber;
    private boolean lostOrStolen;
    private boolean blocked;
    private double fundsAvailable;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private int numAttempts = 3;

    public Card(String cardNumber, String pinNumber, double startAmount, String start, String end) {
        this.cardNumber = cardNumber;
        this.pinNumber = pinNumber;
        this.fundsAvailable = startAmount;
        this.lostOrStolen = false;
        this.blocked = false;
        this.startDate = LocalDate.parse(start);
        this.expiryDate = LocalDate.parse(end);
    }

    public double getCurrentAmount() { return this.fundsAvailable; }

    public String getCardNumber() { return this.cardNumber; }

    public String getPinNumber() { return this.pinNumber; }

    public LocalDate getStartDate() { return this.startDate; }

    public LocalDate getExpiryDate() { return this.expiryDate; }

    public int getNumAttempts() { return this.numAttempts; }

    private double setFunds(double amount) {
        // amount is pos for a deposit and neg for withdrawal
        amount = (double) Math.round(amount *100);
        amount = amount/100;
        this.fundsAvailable += amount;
        return this.fundsAvailable;
    }

    public void report() {
        this.lostOrStolen = true;
    }


    public boolean isStolenOrLost() {
        return this.lostOrStolen;
    }

    public boolean inDate() {
        LocalDate current = LocalDate.now();
        //check if start date is before current date
        //and if expiry is before current date
        if(current.isBefore(this.startDate) || current.isAfter(this.expiryDate)) {
            return false;
        }
        return true;
    }

    public boolean checkDetails(String num, String pin) {
        if(this.getCardNumber().equals(num)
           && this.getPinNumber().equals(pin) && numAttempts >= 1) {
            this.numAttempts = 3;
            return true;
        } else {
            numAttempts -= 1;
            if(numAttempts == 0) this.block();
            return false;
        }
    }

    public void block() {
        this.blocked = true;
    }

    public double withdraw(double amount) {
        if(amount < 0){
            //negative amount
            return -1;
        } else if (amount > this.fundsAvailable){
            //insufficient funds in account
            return -2;
        } else if (Math.round(amount*1000)%50 != 0) {
            //invalid amount of money
            return -3;
        } else {
            return this.setFunds(-1 * amount);
        }
    }

    public double deposit(double amount) {
        if(amount < 0){
            //negative amount
            return -1;
        } else if (amount % 5 != 0) {
            //coins not accepted
            return -2;
        } else {
            return this.setFunds(amount);
        }
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        } else if (o == null) {
            return false;
        }
        Card card = (Card) o;
        return this.cardNumber.equals(card.getCardNumber()) &&
            this.pinNumber.equals(card.getPinNumber()) &&
            this.fundsAvailable == card.getCurrentAmount() &&
            this.blocked == card.isBlocked() &&
            this.lostOrStolen == card.isStolenOrLost() &&
            this.startDate.toString().equals(card.getStartDate().toString()) &&
            this.expiryDate.toString().equals(card.getExpiryDate().toString());
    }
}
