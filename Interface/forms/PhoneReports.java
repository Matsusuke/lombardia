/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import lombardia2014.generators.LombardiaLogger;
import static lombardia2014.Interface.MainInterface.userSurename;

/**
 *
 * @author Mateusz
 */
public class PhoneReports extends Forms {

    //Strings
    String frameTitle = "Zgłoszenia telefoniczne";
    String tablePanelTitle = "Zgłoszenia telefoniczne";
    String Title;
    String Content;
    int ID;
    String Number;
    String Name;
    String Date;
    String userSurname = userSurename;

    int selectRow = -1;

    //Frames, Panels and Table
    JFrame othersFormFrame = new JFrame();
    JScrollPane scrollPane = null;
    JTable listPhoneReports = null;
    DefaultTableModel model;
    JPanel buttonPanel;
    JPanel tablePanel;
    //Buttons
    JButton cancel = null;
    JButton add = null;
    JButton delete = null;

    //Database
    QueryDB setQuerry = null;
    private ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    SwingWorker worker = null;

    //Date and Time usage
    Date now = new Date();

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    String date = DATE_FORMAT.format(now);

    @Override
    public void generatePanels(GridBagConstraints c) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        listPhoneReports = new JTable(model);
        model.addColumn("Identyfikator");
        model.addColumn("Tytuł");
        model.addColumn("Treść");
        model.addColumn("Imię i nazwisko");
        model.addColumn("Data zgłoszenia");
        model.addColumn("Numer telefonu");
        model.addTableModelListener(listPhoneReports);
        getReportContent();

        listPhoneReports.addMouseListener(new GetSelectRow());
        listPhoneReports.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(listPhoneReports);
        listPhoneReports.setFillsViewportHeight(true);
        listPhoneReports.setPreferredSize(new Dimension(650, 600));

        scrollPane.setPreferredSize(new Dimension(650, 600));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 650;
        c.ipady = 600;
        mainPanel.add(tablePanel, c);
    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();
        add = new JButton();
        add.setText("Dodaj");
        add.addActionListener(new addPhoneReport());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 0;
        buttonPanel.add(add, c2);

        delete = new JButton();
        delete.setText("Usuń");
        delete.addActionListener(new deletePhoneReport());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 1;
        c2.gridy = 0;
        buttonPanel.add(delete, c2);

        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 2;
        c2.gridy = 0;
        buttonPanel.add(cancel, c2);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 10;
        mainPanel.add(buttonPanel, c);
    }

    @Override
    public void generateGui() {
        formFrame.setSize(800, 800);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(frameTitle);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(800, 800));
        titleBorder = BorderFactory.createTitledBorder(blackline, frameTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(750, 700));
        titleBorder = BorderFactory.createTitledBorder(blackline, tablePanelTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        tablePanel.setBorder(titleBorder);

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(100, 700));

        generatePanels(c);
        generatePanels2();
        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    public void getReportContent() {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT PhoneReports.* , "
                    + "Customers.NAME as Name, Customers.SURNAME as Surename"
                    + " FROM PhoneReports, Customers "
                    + "WHERE PhoneReports.ID_CUSTOMER = Customers.ID;");

            while (queryResult.next()) {
                Object[] data = {
                    queryResult.getInt("ID"),
                    queryResult.getString("TITLE"),
                    queryResult.getString("CONTENT"),
                    queryResult.getString("Name") + " "
                    + queryResult.getString("Surename"),
                    queryResult.getString("DATE"),
                    "+" + queryResult.getString("NUMBER")};
                model.addRow(data);
            }
            setQuerry.closeDB();
        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
    }

    private void updateReportTable() {
        model.removeTableModelListener(listPhoneReports);
        model.setRowCount(0);
        getReportContent();
        model.addTableModelListener(listPhoneReports);
        formFrame.repaint();
    }

    public class addPhoneReport implements ActionListener {

        NewPhoneReport generateReport = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            generateReport = new NewPhoneReport();
            generateReport.generateGui();
            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    while (true) {
                        if (generateReport.isClose() == true) {
                            updateReportTable();
                            break;
                        }
                        Thread.sleep(100);
                    }
                    return null;
                }

            };
            worker.execute();
        }

    }

    public class deletePhoneReport implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (selectRow >= 0 && selectRow < model.getRowCount()) {
                try {

                    int selectedOption = JOptionPane.showConfirmDialog(formFrame,
                            "Czy na pewno cheszu usunąć tego użytkownika ?",
                            "uwaga unuwanie!",
                            JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        setQuerry = new QueryDB();
                        conDB = setQuerry.getConnDB();
                        stmt = conDB.createStatement();
                        queryResult = setQuerry.dbSetQuery("DELETE FROM PhoneReports"
                                + " WHERE ID = " + ID + ";");
                        setQuerry.closeDB();
                        model.removeTableModelListener(listPhoneReports);
                        model.removeRow(selectRow);
                        model.addTableModelListener(listPhoneReports);
                    }
                } catch (SQLException ex) {
                    LombardiaLogger startLogging = new LombardiaLogger();
                    String text = startLogging.preparePattern("Error", ex.getMessage()
                            + "\n" + Arrays.toString(ex.getStackTrace()));
                    startLogging.logToFile(text);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Musisz zaznaczyć zgłoszenie",
                        "Zgłoszenie nie zostałow wybrane!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            selectRow = table.getSelectedRow();
            if (selectRow >= 0) {
                int row = table.rowAtPoint(p);

                ID = (int) listPhoneReports.getModel().getValueAt(
                        listPhoneReports.convertRowIndexToView(selectRow), 0);
                JTable target = (JTable) e.getSource();
            }
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

    }

}
