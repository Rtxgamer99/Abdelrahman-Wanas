import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BankingGUI extends JFrame {
    private Bank bank;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel loginPanel;
    private JPanel dashboardPanel;
    private JPanel accountPanel;
    private JPanel transferPanel;
    private JPanel transactionPanel;

    public BankingGUI(Bank bank) {
        this.bank = bank;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize card layout for different panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels
        createLoginPanel();
        createDashboardPanel();
        createAccountPanel();
        createTransferPanel();
        createTransactionPanel();

        // Add panels to main panel
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(accountPanel, "ACCOUNT");
        mainPanel.add(transferPanel, "TRANSFER");
        mainPanel.add(transactionPanel, "TRANSACTION");

        // Show login panel first
        cardLayout.show(mainPanel, "LOGIN");

        // Add main panel to frame
        add(mainPanel);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Welcome to Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (bank.login(username, password)) {
                cardLayout.show(mainPanel, "DASHBOARD");
                updateDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        });

        registerButton.addActionListener(e -> {
            // Show registration dialog
            showRegistrationDialog();
        });
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("Account");
        JMenu transactionMenu = new JMenu("Transaction");
        JMenu userMenu = new JMenu("User");

        JMenuItem viewAccountsItem = new JMenuItem("View Accounts");
        JMenuItem createAccountItem = new JMenuItem("Create Account");
        JMenuItem transferFundsItem = new JMenuItem("Transfer Funds");
        JMenuItem viewTransactionsItem = new JMenuItem("View Transactions");
        JMenuItem logoutItem = new JMenuItem("Logout");

        accountMenu.add(viewAccountsItem);
        accountMenu.add(createAccountItem);
        transactionMenu.add(transferFundsItem);
        transactionMenu.add(viewTransactionsItem);
        userMenu.add(logoutItem);

        menuBar.add(accountMenu);
        menuBar.add(transactionMenu);
        menuBar.add(userMenu);

        // Add menu bar to panel
        dashboardPanel.add(menuBar, BorderLayout.NORTH);

        // Create main content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add account summary cards
        contentPanel.add(createSummaryCard("Total Balance", "$0.00"));
        contentPanel.add(createSummaryCard("Checking Accounts", "0"));
        contentPanel.add(createSummaryCard("Savings Accounts", "0"));
        contentPanel.add(createSummaryCard("Recent Transactions", "0"));

        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
        viewAccountsItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "ACCOUNT");
            updateAccountPanel();
        });

        createAccountItem.addActionListener(e -> {
            showCreateAccountDialog();
        });

        transferFundsItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "TRANSFER");
            updateTransferPanel();
        });

        viewTransactionsItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "TRANSACTION");
            updateTransactionPanel();
        });

        logoutItem.addActionListener(e -> {
            bank.logout();
            cardLayout.show(mainPanel, "LOGIN");
        });
    }

    private void createAccountPanel() {
        accountPanel = new JPanel(new BorderLayout());
        
        // Create table for accounts
        String[] columnNames = {"Account ID", "Type", "Balance", "Status"};
        javax.swing.table.DefaultTableModel accountTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0);
        JTable accountTable = new JTable(accountTableModel);
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        accountPanel.add(scrollPane, BorderLayout.CENTER);

        // Add back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));
        accountPanel.add(backButton, BorderLayout.SOUTH);
    }

    private void createTransferPanel() {
        transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Transfer Funds");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        transferPanel.add(titleLabel, gbc);

        JComboBox<String> fromAccountCombo = new JComboBox<>();
        JComboBox<String> toAccountCombo = new JComboBox<>();
        JTextField amountField = new JTextField(20);
        JButton transferButton = new JButton("Transfer");
        JButton backButton = new JButton("Back to Dashboard");

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        transferPanel.add(new JLabel("From Account:"), gbc);
        gbc.gridx = 1;
        transferPanel.add(fromAccountCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        transferPanel.add(new JLabel("To Account:"), gbc);
        gbc.gridx = 1;
        transferPanel.add(toAccountCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        transferPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        transferPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        transferPanel.add(transferButton, gbc);

        gbc.gridy = 5;
        transferPanel.add(backButton, gbc);

        transferButton.addActionListener(e -> {
            String fromAccount = (String) fromAccountCombo.getSelectedItem();
            String toAccount = (String) toAccountCombo.getSelectedItem();
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (bank.transfer(fromAccount, toAccount, amount)) {
                    JOptionPane.showMessageDialog(this, "Transfer successful!");
                    updateTransferPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Transfer failed!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));
    }

    private void createTransactionPanel() {
        transactionPanel = new JPanel(new BorderLayout());
        
        // Create table for transactions
        String[] columnNames = {"Date", "Type", "Amount", "From", "To", "Status"};
        javax.swing.table.DefaultTableModel transactionTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0);
        JTable transactionTable = new JTable(transactionTableModel);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        transactionPanel.add(scrollPane, BorderLayout.CENTER);

        // Add back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));
        transactionPanel.add(backButton, BorderLayout.SOUTH);
    }

    private JPanel createSummaryCard(String title, String value) {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register New User", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JTextField nameField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        dialog.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        dialog.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String email = emailField.getText();
            String name = nameField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match!");
                return;
            }

            User newUser = new User(username, password, "Customer");
            bank.addUser(newUser);
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showCreateAccountDialog() {
        JDialog dialog = new JDialog(this, "Create New Account", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"Checking", "Savings"});
        JTextField initialDepositField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(accountTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Initial Deposit:"), gbc);
        gbc.gridx = 1;
        dialog.add(initialDepositField, gbc);

        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(createButton, gbc);

        createButton.addActionListener(e -> {
            String accountType = (String) accountTypeCombo.getSelectedItem();
            try {
                double initialDeposit = Double.parseDouble(initialDepositField.getText());
                Account newAccount;
                if ("Checking".equals(accountType)) {
                    newAccount = new checkingAccount();
                } else {
                    newAccount = new savingAccount();
                }
                newAccount.deposit(initialDeposit);
                bank.addAccount(newAccount);
                dialog.dispose();
                updateDashboard();
                JOptionPane.showMessageDialog(this, "Account created successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount!");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateDashboard() {
        // Update dashboard with current account information
        List<Account> accounts = bank.getAccountsForCurrentUser();
        double totalBalance = 0;
        int checkingCount = 0;
        int savingsCount = 0;

        for (Account account : accounts) {
            totalBalance += account.getBalance();
            if (account instanceof checkingAccount) {
                checkingCount++;
            } else if (account instanceof savingAccount) {
                savingsCount++;
            }
        }

        // Update summary cards
        Component[] components = ((JPanel) dashboardPanel.getComponent(1)).getComponents();
        ((JLabel) ((JPanel) components[0]).getComponent(1)).setText(String.format("$%.2f", totalBalance));
        ((JLabel) ((JPanel) components[1]).getComponent(1)).setText(String.valueOf(checkingCount));
        ((JLabel) ((JPanel) components[2]).getComponent(1)).setText(String.valueOf(savingsCount));
    }

    private void updateAccountPanel() {
        // Update account table with current accounts
        JTable accountTable = (JTable) ((JScrollPane) accountPanel.getComponent(0)).getViewport().getView();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) accountTable.getModel();
        model.setRowCount(0);

        List<Account> accounts = bank.getAccountsForCurrentUser();
        for (Account account : accounts) {
            model.addRow(new Object[]{
                account.getAccountId(),
                account.getAccountType(),
                String.format("$%.2f", account.getBalance()),
                account.isActive() ? "Active" : "Inactive"
            });
        }
    }

    private void updateTransferPanel() {
        // Update transfer panel with current accounts
        JComboBox<String> fromAccountCombo = (JComboBox<String>) transferPanel.getComponent(2);
        JComboBox<String> toAccountCombo = (JComboBox<String>) transferPanel.getComponent(4);
        fromAccountCombo.removeAllItems();
        toAccountCombo.removeAllItems();

        List<Account> accounts = bank.getAccountsForCurrentUser();
        for (Account account : accounts) {
            fromAccountCombo.addItem(account.getAccountId());
            toAccountCombo.addItem(account.getAccountId());
        }
    }

    private void updateTransactionPanel() {
        // Update transaction table with current transactions
        JTable transactionTable = (JTable) ((JScrollPane) transactionPanel.getComponent(0)).getViewport().getView();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        List<Account> accounts = bank.getAccountsForCurrentUser();
        for (Account account : accounts) {
            for (Transaction transaction : account.getTransactionHistory()) {
                model.addRow(new Object[]{
                    transaction.getTimestamp(),
                    transaction.getType(),
                    String.format("$%.2f", transaction.getAmount()),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getStatus()
                });
            }
        }
    }
} 