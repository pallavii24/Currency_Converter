/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.currencyconvertergui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


public class CurrencyConverterGUI extends JFrame {
    private JTextField amountTextField;
    private JComboBox<String> fromCurrencyComboBox;
    private JComboBox<String> toCurrencyComboBox;
    private JLabel resultLabel;

    private static final String API_KEY = "ee4b720fdb6c9fa9c51cfaaf "; // Replace with your actual API key

    public CurrencyConverterGUI() {
        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(10, 20, 80, 25);
        panel.add(amountLabel);

        amountTextField = new JTextField(20);
        amountTextField.setBounds(100, 20, 165, 25);
        panel.add(amountTextField);

        JLabel fromCurrencyLabel = new JLabel("From Currency:");
        fromCurrencyLabel.setBounds(10, 50, 120, 25);
        panel.add(fromCurrencyLabel);

        // Include INR in the fromCurrencyComboBox
        fromCurrencyComboBox = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "JPY", "CAD", "INR"});
        fromCurrencyComboBox.setBounds(150, 50, 80, 25);
        panel.add(fromCurrencyComboBox);

        JLabel toCurrencyLabel = new JLabel("To Currency:");
        toCurrencyLabel.setBounds(10, 80, 120, 25);
        panel.add(toCurrencyLabel);

        // Include INR in the toCurrencyComboBox
        toCurrencyComboBox = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "JPY", "CAD", "INR"});
        toCurrencyComboBox.setBounds(150, 80, 80, 25);
        panel.add(toCurrencyComboBox);

        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(10, 120, 80, 25);
        panel.add(convertButton);

        resultLabel = new JLabel("");
        resultLabel.setBounds(100, 120, 300, 25);
        panel.add(resultLabel);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }
private void convertCurrency() {
    try {
        String fromCurrency = (String) fromCurrencyComboBox.getSelectedItem();
        String toCurrency = (String) toCurrencyComboBox.getSelectedItem();
        String amountText = amountTextField.getText().trim();

        // Extract the numeric part of the amount using regex
        String numericPart = amountText.replaceAll("[^0-9.]", "");

        // Check if the numeric part is not empty
        if (!numericPart.isEmpty()) {
            double amount = Double.parseDouble(numericPart);

            // Update the API URL
            String url = "https://open.er-api.com/v6/latest/USD";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                JSONObject jsonResponse = new JSONObject(response);
                double baseRate = jsonResponse.getJSONObject("rates").getDouble(fromCurrency);
                double toRate = jsonResponse.getJSONObject("rates").getDouble(toCurrency);

                // Calculate the converted amount
                double convertedAmount = (amount / baseRate) * toRate;

                // Display the result
                resultLabel.setText(amount + " " + fromCurrency + " is equal to " + convertedAmount + " " + toCurrency);
            } else {
                resultLabel.setText("Error: " + responseCode);
            }
        } else {
            resultLabel.setText("Error: Invalid amount");
        }
    } catch (NumberFormatException ex) {
        resultLabel.setText("Error: Invalid amount");
    } catch (Exception ex) {
        resultLabel.setText("Error: " + ex.getMessage());
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CurrencyConverterGUI();
            }
        });
    }
}
