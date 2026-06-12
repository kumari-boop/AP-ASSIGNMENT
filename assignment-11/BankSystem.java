import java.util.*;

// Base Class
class Account {
    private String accountNumber;
    private String ownerName;
    private double balance;

    // Constructor 1 (default)
    public Account() {
        this("0000", "Unknown", 0.0); // constructor chaining
    }

    // Constructor 2 (parameterized)
    public Account(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;

        if (balance >= 0) {

            this.balance = balance;
        } else {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    // Getters & Setters (Encapsulation)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Account Type (to be overridden)
    public String getAccountType() {
        return "Generic Account";
    }

    // Deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be positive");
        }
        balance += amount;
    }

    // Withdraw
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
    }

    // Display
    public void display() {
        System.out.println("Account Type: " + getAccountType()); // polymorphism
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Owner Name: " + ownerName);
        System.out.println("Balance: " + balance);
    }
}

// Savings Account
class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accNo, String owner, double balance, double interestRate) {
        super(accNo, owner, balance);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    public double calculateInterest() {
        return getBalance() * interestRate / 100;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Calculated Interest: " + calculateInterest());
        System.out.println("-----------------------------------");
    }
}

// Current Account
class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accNo, String owner, double balance, double overdraftLimit) {
        super(accNo, owner, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "Current Account";
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {

            throw new IllegalArgumentException("Invalid withdrawal amount");
        }

        if (amount > (getBalance() + overdraftLimit)) {
            throw new IllegalArgumentException("Overdraft limit exceeded");
        }

        setBalance(getBalance() - amount);
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Overdraft Limit: " + overdraftLimit);
        System.out.println("-----------------------------------");
    }
}

// Main Class
public class BankSystem {
    public static void main(String[] args) {

        List<Account> accounts = new ArrayList<>();

        // Polymorphism
        accounts.add(new SavingsAccount("101", "Rupam", 5000, 5));
        accounts.add(new CurrentAccount("102", "Ankit", 3000, 2000));

        // Operations
        for (Account acc : accounts) {
            acc.deposit(1000);

            try {
                acc.withdraw(2000);
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

            acc.display(); // runtime polymorphism
        }
    }
}