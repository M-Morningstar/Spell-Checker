package gui;

import javax.swing.*;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// Helper class to create menu bar
public class MenuBar {

    // Variables
    private static JMenuBar menuBar;
    private static JMenu fileMenu;
    private static JMenuItem editUserDic;
    private static JCheckBoxMenuItem ignoreTags;

    public static JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editUserDic = new JMenuItem("Edit User Dictionary");
        ignoreTags = new JCheckBoxMenuItem("Ignore HTML/XML Tags");
        ignoreTags.setState(true);
        fileMenu.add(editUserDic);
        fileMenu.add(ignoreTags);
        menuBar.add(fileMenu);
        editUserDicFunctionality();
        ignoreTagsFunctionality();

        return menuBar;
    }

    private static void editUserDicFunctionality() {
        // Add action listener to the menu item
        editUserDic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Launcher.cards.add(UserDictCard.createUserDictCard(), "USER_DICT");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                CardLayout cardLayout = (CardLayout) Launcher.cards.getLayout();
                cardLayout.show(Launcher.cards, "USER_DICT");
            }
        });
    }

    private static void ignoreTagsFunctionality() {
        // Add action listener for the JCheckBoxMenuItem
        ignoreTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorCard.ignTags = ignoreTags.isSelected();
            }
        });
    }
}