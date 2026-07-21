import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;

class BankAccountss {
    private String accountHolder;
    private double balance;

    private ArrayList<String> transactions = new ArrayList<>();

    BankAccountss(String accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public static void saveAccounts(HashMap<String, BankAccountss> accounts){
        try{
            FileWriter writer = new FileWriter("accounts.txt");

            for (BankAccountss acc : accounts.values()){

                StringBuilder tx = new StringBuilder();

                for (String t : acc.getTransactions()) {
                    tx.append(t).append(";");
                }

                writer.write(acc.getName() + "," + acc.getBalance() + "," + tx + "\n");
            }

            writer.close();
            System.out.println("Accounts saved sucessfully");
        } catch (IOException e){
            System.out.println("Error saving accounts.");
        }
    }

    public static void loadAccounts(HashMap<String, BankAccountss> accounts){
        try {
            File file = new File("accounts.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length < 2) continue;

                String name = parts[0];
                double balance = Double.parseDouble(parts[1].trim());

                BankAccountss acc = new BankAccountss(name, balance);

                if (parts.length > 2) {
                    String[] txParts = parts[2].split(";");

                    for (String t : txParts) {
                        if (!t.isEmpty()) {
                            acc.getTransactions().add(t);
                        }
                    }
                }
                accounts.put(name.toLowerCase(), acc);
            }

            fileScanner.close();


        } catch (FileNotFoundException e){
            System.out.println("No saved data found.");
        }

    }

    public static void createAccount(Scanner sc, HashMap<String, BankAccountss> accounts) {
        System.out.println("What is your name?");
        String nameInput = sc.nextLine();
        String key = nameInput.toLowerCase();

        if (accounts.containsKey(key)) {
            System.out.println("Account already exists!");
        } else {
            accounts.put(key, new BankAccountss(nameInput, 1000));
            System.out.println("Account created!");
            System.out.println("Total accounts: " + accounts.size());
        }
    }

    public static void depositFlow(Scanner sc, HashMap<String, BankAccountss> accounts) {
        System.out.println("Enter your name: ");
        String key = sc.nextLine().toLowerCase();

        BankAccountss acc = accounts.get(key);

        if (acc != null) {
            System.out.println("Enter amount to be deposited: ");

            try {
                double amount = sc.nextDouble();
                sc.nextLine();
                acc.deposit(amount);
            } catch (Exception e) {
                System.out.println("Invalid amount!");
                sc.nextLine();
            }

        } else {
            System.out.println("Account not found");
        }

    }

    public static void withdrawFlow(Scanner sc, HashMap<String, BankAccountss> accounts) {
        System.out.println("Enter your name: ");
        String key = sc.nextLine().toLowerCase();

        BankAccountss acc = accounts.get(key);
        if (acc != null) {
            System.out.println("Enter the amount to be withdrawn");

            double amount;

            try {
                amount = sc.nextDouble();
                sc.nextLine();
                acc.withdraw(amount);
            } catch (Exception e) {
                System.out.println("Invalid amount!");
                sc.nextLine();
            }

        } else {
            System.out.println("Account not found");
        }
    }

    public static void showBalanceFlow(Scanner sc, HashMap<String, BankAccountss> accounts) {
        System.out.println("Enter your name: ");
        String key = sc.nextLine().toLowerCase();

        BankAccountss acc = accounts.get(key);

        if (acc != null) {
            acc.showBalance();
        } else {
            System.out.println("This account does not exist");
        }
    }

    public static void showTransactionsFlow(Scanner sc, HashMap<String, BankAccountss> accounts) {
        System.out.println("Enter your name: ");
        String name = sc.nextLine().toLowerCase();

        BankAccountss acc = accounts.get(name);

        if (acc != null) {
            System.out.println("Transaction History: ");
            acc.showTransactions();
        } else {
            System.out.println("Account not found");
        }

    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            System.out.println("Deposited: " + amount);
            transactions.add("Deposited " + amount + " EUR at " + java.time.LocalTime.now().withNano(0));
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
            transactions.add("Withdrew " + amount + " EUR at " + java.time.LocalTime.now().withNano(0));
        } else {
            System.out.println("Invalid or insufficient balance!");
        }
    }

    public void showBalance() {

        System.out.println(accountHolder + ", your balance is: " + balance + " EUR");
    }

    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet");
        } else {
            for (String s : transactions) {
                System.out.println(s);
            }
        }
    }

    public String getName() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }
}

public class Updated2BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        int choice = 0;
        HashMap<String, BankAccountss> accounts = new HashMap<>();
        BankAccountss.loadAccounts(accounts);

        do {
            System.out.println("Welcome to Sargam bank of Rajab (Enter 1-6 to validate response)");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show Balance");
            System.out.println("5. Show Transactions");
            System.out.println("6. Exit");

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e){
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); //clears wrong input
                continue; // restart loop
            }
            switch (choice) {
                case 1 -> BankAccountss.createAccount(sc, accounts);
                case 2 -> BankAccountss.depositFlow(sc, accounts);
                case 3 -> BankAccountss.withdrawFlow(sc, accounts);
                case 4 -> BankAccountss.showBalanceFlow(sc, accounts);
                case 5 -> BankAccountss.showTransactionsFlow(sc, accounts);
                case 6 -> {
                    System.out.println("Thank you for visiting Sargam bank of Rajab");
                    BankAccountss.saveAccounts(accounts);
                    loop = false;
                }
                default -> System.out.println("Invalid option.");
            }

        } while(loop);
    }
}
