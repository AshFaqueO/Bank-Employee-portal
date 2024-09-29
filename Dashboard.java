import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;

public class Dashboard extends JFrame {
    private JPanel navigationPanel;
    private JPanel mainPanel;
    private JLabel dateTimeLabel;
    private ArrayList<Account> accounts;
    private boolean isAdmin;
    private static final String TRANSACTION_FILE = "transaction_history.txt"; // Transaction file for history

    public Dashboard(boolean isAdmin) {
        this.isAdmin = isAdmin;

        setTitle("Employee Portal");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // dummy customers
        accounts = new ArrayList<>();
        accounts.add(new Account("Asib Hossain", 1200.50));
        accounts.add(new Account("Akram Hossain", 1350.00));
        accounts.add(new Account("Nigar Sultana", 2500.75));
        accounts.add(new Account("Rokib Hossain", 3100.20));
        accounts.add(new Account("Asman Rahman", 4000.10));

        // Title Bar
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel("                                                                Bank Employee Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dateTimeLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(dateTimeLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setLayout(new GridLayout(0, 1));
        navigationPanel.setBackground(new Color(52, 73, 94));
        navigationPanel.setPreferredSize(new Dimension(250, 0));
        navigationPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 81, 89), 30));

        // main menus
        addNavigationButton("Account Management", e -> showAccountManagement());
        addNavigationButton("Display Accounts", e -> displayAccounts());
        addNavigationButton("To-Do List Today", e -> showToDoList());
        addNavigationButton("Emergency Contact", e -> showEmergencyContact());

        // transaction history for admin only
        if (isAdmin) {
            addNavigationButton("View Transaction History", e -> viewTransactionHistory());
        }

        add(navigationPanel, BorderLayout.WEST);

        // Main dashboard panel
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(34, 45, 50));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 81, 89), 2));
        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setPreferredSize(new Dimension(0, 30));
        footerPanel.add(new JLabel("© 2024 Bank of Imagination | Contact: support@bankofimagination.com"));
        footerPanel.setForeground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 81, 89), 1));
        add(footerPanel, BorderLayout.SOUTH);

        // Timer
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        // Show display accounts by default
        displayAccounts();
    }

    private void addNavigationButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setOpaque(true); // Ensure background color is visible
        button.setBackground(new Color(67, 81, 89));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        button.addActionListener(action);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(button);
    }

    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm:ss a");
        Date currentDate = new Date();
        dateTimeLabel.setText(formatter.format(currentDate));
    }

    private void displayAccounts() {
        mainPanel.removeAll();

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); // list

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 700));

        for (Account account : accounts) {
            JPanel accountPanel = new JPanel();
            accountPanel.setBackground(new Color(52, 73, 94));
            accountPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 81, 89), 2));
            accountPanel.setLayout(new GridLayout(2, 1));

            JLabel accountNameLabel = new JLabel("Account Holder: " + account.getName());
            accountNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
            accountNameLabel.setForeground(Color.WHITE);

            JLabel balanceLabel = new JLabel("Balance: TK " + account.getBalance());
            balanceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            balanceLabel.setForeground(Color.GREEN);

            accountPanel.add(accountNameLabel);
            accountPanel.add(balanceLabel);

            listPanel.add(accountPanel);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showAccountManagement() {
        mainPanel.removeAll();
        mainPanel.add(new AccountManagementPanel(accounts, isAdmin));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showEmergencyContact() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        JLabel emergencyLabel = new JLabel("Emergency Contact Information", SwingConstants.CENTER);
        emergencyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        emergencyLabel.setForeground(Color.RED);
        mainPanel.add(emergencyLabel, BorderLayout.NORTH);

        JTextArea emergencyDetails = new JTextArea(
                "For emergencies, please contact:\n" +
                "Name: Anisur Rahman\n" +
                "Phone: +8801716274658\n" +
                "Email: emergency@bankofimagination.com\n" +
                "Available 24/7 for urgent queries."
        );
        emergencyDetails.setFont(new Font("Arial", Font.PLAIN, 16));
        emergencyDetails.setForeground(Color.WHITE);
        emergencyDetails.setBackground(new Color(34, 45, 50));
        emergencyDetails.setEditable(false);
        mainPanel.add(new JScrollPane(emergencyDetails), BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showToDoList() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        JLabel todoLabel = new JLabel("To-Do List for Today", SwingConstants.CENTER);
        todoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        todoLabel.setForeground(Color.YELLOW);
        mainPanel.add(todoLabel, BorderLayout.NORTH);

        DefaultListModel<String> todoModel = new DefaultListModel<>();
        todoModel.addElement("Review new account applications");
        todoModel.addElement("Approve pending transactions");
        todoModel.addElement("Prepare monthly report");
        todoModel.addElement("Schedule team meeting");

        JList<String> todoList = new JList<>(todoModel);
        todoList.setFont(new Font("Arial", Font.PLAIN, 16));
        todoList.setBackground(new Color(52, 73, 94));
        todoList.setForeground(Color.WHITE);
        todoList.setSelectionBackground(new Color(44, 62, 80));

        mainPanel.add(new JScrollPane(todoList), BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void viewTransactionHistory() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        JTextArea transactionHistoryArea = new JTextArea();
        transactionHistoryArea.setEditable(false);
        transactionHistoryArea.setBackground(new Color(34, 45, 50));
        transactionHistoryArea.setForeground(Color.YELLOW);
        transactionHistoryArea.setFont(new Font("Arial", Font.PLAIN, 16));

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactionHistoryArea.append(line + "\n");
            }
        } catch (IOException e) {
            transactionHistoryArea.setText("Error loading transaction history.");
        }

        mainPanel.add(new JScrollPane(transactionHistoryArea), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Account management panel
    class AccountManagementPanel extends JPanel {
        private JTextField accountNameField;
        private JTextField amountField;
        private JTextField newAccountNameField;
        private JTextArea transactionHistoryArea;

        public AccountManagementPanel(ArrayList<Account> accounts, boolean isAdmin) {
            setLayout(new BorderLayout());

            // Input Panel for managing existing accounts
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 2));

            inputPanel.add(new JLabel("                                                           Account Name:"));
            accountNameField = new JTextField();
            inputPanel.add(accountNameField);

            inputPanel.add(new JLabel("                                                          Amount to Add/Deduct:"));
            amountField = new JTextField();
            inputPanel.add(amountField);

            JButton addButton = createSimpleButton("Add Cash");
            addButton.addActionListener(new AddCashAction(accounts));
            inputPanel.add(addButton);

            JButton deductButton = createSimpleButton("Deduct Cash");
            deductButton.addActionListener(new DeductCashAction(accounts));
            inputPanel.add(deductButton);

            add(inputPanel, BorderLayout.NORTH);

            // New Account Panel
            if (isAdmin) {
                JPanel newAccountPanel = new JPanel();
                newAccountPanel.setLayout(new GridLayout(3, 1));

                newAccountPanel.add(new JLabel("                                                              Account Name:"));
                newAccountNameField = new JTextField();
                newAccountPanel.add(newAccountNameField);

                JButton createButton = createSimpleButton("Create New Account");
                createButton.addActionListener(new CreateAccountAction(accounts));
                newAccountPanel.add(createButton);

                JButton deleteButton = createSimpleButton("Delete Account");
                deleteButton.addActionListener(new DeleteAccountAction(accounts));
                newAccountPanel.add(deleteButton);

                add(newAccountPanel, BorderLayout.SOUTH);
            }

            // Transaction history box
            JPanel historyPanel = new JPanel(new BorderLayout());
            historyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 4), "Transaction History"));
            transactionHistoryArea = new JTextArea();
            transactionHistoryArea.setEditable(false);
            transactionHistoryArea.setBackground(Color.DARK_GRAY); 
            transactionHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
            transactionHistoryArea.setForeground(Color.YELLOW);

            JScrollPane historyScrollPane = new JScrollPane(transactionHistoryArea);
            historyPanel.add(historyScrollPane, BorderLayout.CENTER);
            historyPanel.setPreferredSize(new Dimension(400, 300));
            add(historyPanel, BorderLayout.CENTER);
        }

        private JButton createSimpleButton(String text) {
            JButton button = new JButton(text);
            button.setOpaque(true); // Ensure background color is visible
            button.setContentAreaFilled(true); // Ensures the content area is filled with color
            button.setFocusPainted(false);
            button.setBackground(new Color(0, 51, 102)); 
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 51), 4), 
                    BorderFactory.createEmptyBorder(15, 10, 15, 10)));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }
        

        // Actions for buttons
        class AddCashAction implements ActionListener {
            private ArrayList<Account> accounts;

            public AddCashAction(ArrayList<Account> accounts) {
                this.accounts = accounts;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String accountName = accountNameField.getText();
                String amountStr = amountField.getText();

                for (Account account : accounts) {
                    if (account.getName().equalsIgnoreCase(accountName)) {
                        double amount = Double.parseDouble(amountStr);
                        account.setBalance(account.getBalance() + amount);
                        transactionHistoryArea.append("Added TK " + amount + " to " + accountName + "\n");
                        logTransaction("Added TK " + amount + " to " + accountName);
                        return;
                    }
                }
                transactionHistoryArea.append("Account not found.\n");
            }
        }

        class DeductCashAction implements ActionListener {
            private ArrayList<Account> accounts;

            public DeductCashAction(ArrayList<Account> accounts) {
                this.accounts = accounts;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String accountName = accountNameField.getText();
                String amountStr = amountField.getText();

                for (Account account : accounts) {
                    if (account.getName().equalsIgnoreCase(accountName)) {
                        double amount = Double.parseDouble(amountStr);
                        if (account.getBalance() >= amount) {
                            account.setBalance(account.getBalance() - amount);
                            transactionHistoryArea.append("Deducted TK " + amount + " from " + accountName + "\n");
                            logTransaction("Deducted TK " + amount + " from " + accountName);
                        } else {
                            transactionHistoryArea.append("Insufficient balance for " + accountName + "\n");
                        }
                        return;
                    }
                }
                transactionHistoryArea.append("Account not found.\n");
            }
        }

        class CreateAccountAction implements ActionListener {
            private ArrayList<Account> accounts;

            public CreateAccountAction(ArrayList<Account> accounts) {
                this.accounts = accounts;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String accountName = newAccountNameField.getText();
                accounts.add(new Account(accountName, 0.0));
                transactionHistoryArea.append("Created new account: " + accountName + "\n");
                logTransaction("Created new account: " + accountName);
            }
        }

        class DeleteAccountAction implements ActionListener {
            private ArrayList<Account> accounts;

            public DeleteAccountAction(ArrayList<Account> accounts) {
                this.accounts = accounts;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String accountName = newAccountNameField.getText();
                accounts.removeIf(account -> account.getName().equalsIgnoreCase(accountName));
                transactionHistoryArea.append("Deleted account: " + accountName + "\n");
                logTransaction("Deleted account: " + accountName);
            }
        }

        // log transactions to a file
        private void logTransaction(String message) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
                writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": " + message);
                writer.newLine();
            } catch (IOException e) {
                transactionHistoryArea.append("Error logging transaction.\n");
            }
        }
    }

    // Account class for storing basic account information
    class Account {
        private String name;
        private double balance;

        public Account(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
}
