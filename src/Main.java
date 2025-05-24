package src;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize the bank
        Bank bank = Bank.getInstance();
        bank.loadData();

        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            BankingGUI gui = new BankingGUI(bank);
            gui.setVisible(true);
        });
    }
}
