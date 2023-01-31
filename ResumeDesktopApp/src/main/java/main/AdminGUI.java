package main;

import javax.swing.*;

public class AdminGUI {

    private JTable adminTable;
    private JPanel adminPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTable adminTable = createTable();
        JScrollPane scrollPane = new JScrollPane(adminTable);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static JTable createTable() {
        String[] columnNames = {"Name", "Surname", "Address"};
        Object[][] data = {{"Erturk", "Memmedli", "Baku"},{"Adil", "Adilli", "Washington"}};
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        return table;
    }
}