package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import io.FileHandler;
import dictionary.UserDictionary;

public class UserDictCard {
    public static DefaultListModel<String> listModel;
    private static UserDictionary userDictionary;

    public static JPanel createUserDictCard() throws IOException {
        JPanel wordListPanel = new JPanel();
        wordListPanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);

        userDictionary = new UserDictionary();
        listModel.addAll(userDictionary.getWords());

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton saveButton = new JButton("Save");
        JButton goBackButton = new JButton("Go Back");

        JTextField inputField = new JTextField(20);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newItem = inputField.getText();
                if (!newItem.isEmpty()) {
                    try {
                        userDictionary.addWord(newItem);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    listModel.addElement(newItem);
                    inputField.setText("");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                String wordToChange = listModel.get(selectedIndex);
                if (selectedIndex != -1) {
                    String editedItem = inputField.getText();
                    if (!editedItem.isEmpty()) {
                        userDictionary.removeWord(wordToChange);
                        try {
                            userDictionary.addWord(editedItem);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        listModel.setElementAt(editedItem, selectedIndex);
                        inputField.setText("");
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    userDictionary.removeWord(listModel.get(selectedIndex));
                    listModel.remove(selectedIndex);
                    inputField.setText("");
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveListToFile(userDictionary);
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) Launcher.cards.getLayout();
                cardLayout.show(Launcher.cards, "EDITOR_CARD");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(goBackButton);


        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter/edit your word:"));
        inputPanel.add(inputField);

        wordListPanel.add(scrollPane, BorderLayout.CENTER);
        wordListPanel.add(buttonPanel, BorderLayout.SOUTH);
        wordListPanel.add(inputPanel, BorderLayout.NORTH);

        return wordListPanel;


    }

    private static void saveListToFile(UserDictionary userDictionary) {
        try {
            //String content = getListContent();
            userDictionary.saveUserDictionary();
            JOptionPane.showMessageDialog(null, "List saved successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving the list!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}