/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.dataBaseInterface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombardia2014.Interface.menu.ListUsers;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class MainDBQuierues {

    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;

    //==========================================================================
    // Quueries for Items table
    //==========================================================================
    // items
    // categories 
    public List<String> getCategories() {
        List<String> words = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME FROM Category");
            //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist

            while (queryResult.next()) {
                words.add(queryResult.getString("NAME"));
            }

            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return words;
    }

    public void insertItem(String item) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery(item);

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();
    }

    public Integer getCatID(String category) {
        int catID = 0;
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT ID FROM Category WHERE"
                    + " NAME LIKE '" + category + "';");

            while (queryResult.next()) {
                catID = queryResult.getInt("ID");
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }
        setQuerry.closeDB();

        return catID;
    }

    //==========================================================================
    // Quueries for users 
    //==========================================================================
    //get list of users
    public List<String> getUsersByNameAndSurname() {
        List<String> words = new ArrayList<>();

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT NAME,SURNAME"
                    + " FROM Customers;");

            while (queryResult.next()) {
                words.add(queryResult.getString("NAME") + " "
                        + queryResult.getString("SURNAME"));
            }
            setQuerry.closeDB();

            //addToDictionary("bye");//adds a single word
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return words;
    }

    // check if user exist 
    public boolean checkUser(String name, String surname) {
        int customer = 0;

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT Count(*) AS CustomerCount "
                    + "FROM Customers WHERE NAME LIKE '"
                    + name + "' AND SURNAME LIKE '"
                    + surname + "';");

            while (queryResult.next()) {
                customer = queryResult.getInt("CustomerCount");
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return customer == 0;
    }

    public Integer getUserID(String name, String surname) {
        int id = 0;

        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT ID FROM Customers WHERE"
                    + " NAME LIKE '"
                    + name + "' AND SURNAME LIKE '"
                    + surname + "';");
            // save to db

            while (queryResult.next()) {
                id = queryResult.getInt("ID");
            }

            setQuerry.closeDB();

        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    // save user :)
    public void saveUser(String name, String surename, String addres, int Trust,
            String pesel, String discount) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            if (!pesel.isEmpty()) {
                queryResult = setQuerry.dbSetQuery("INSERT INTO Customers (NAME, "
                        + "SURNAME, ADDRESS, PESEL, TRUST, DISCOUNT) VALUES"
                        + "('" + name + "','"
                        + surename + "','"
                        + addres + "','"
                        + pesel + "',"
                        + discount + ","
                        + Trust + ");");
            } else {
                queryResult = setQuerry.dbSetQuery("INSERT INTO Customers (NAME, "
                        + "SURNAME, ADDRESS, TRUST, DISCOUNT) VALUES"
                        + "('" + name + "','"
                        + surename + "','"
                        + addres + "',"
                        + discount + ","
                        + Trust + ");");
            }

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 
    //==========================================================================
    // Quueries for Agreements 
    //==========================================================================
    public Integer getMaxIDAgreements() {
        int maxID = 0;
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

            queryResult = setQuerry.dbSetQuery("SELECT MAX(ID) FROM Agreements;");

            while (queryResult.next()) {
                maxID = queryResult.getInt(1);
            }

        } catch (SQLException ex) {
            LombardiaLogger startLogging = new LombardiaLogger();
            String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
            startLogging.logToFile(text);
        }

        return maxID;
    }

    public void saveAgreements(String idAgreements, Date startDate, Date stopDate,
            Float value, String commision, Float itemValue, Float itemWeigth, float valueRest,
            float saveprice, int custoerID) {
        try {
            setQuerry = new QueryDB();
            conDB = setQuerry.getConnDB();
            stmt = conDB.createStatement();

//            queryResult = setQuerry.dbSetQuery("INSERT INTO Agreements (ID_AGREEMENTS,"
//                    + " START_DATE, STOP_DATE, VALUE, COMMISSION, ITEM_VALUE, ITEM_WEIGHT,"
//                    + " VALUE_REST, SAVEPRICE, ID_CUSTOMER)"
//                    + " VALUES ('"
//                    + createIndex() + "','" + ft.format(curretDate) + "','"
//                    + ft.format(selectedDate) + "','"
//                    + fields[4].getText().replaceAll(",", ".") + "',"
//                    + fields[19].getText() + ","
//                    + fields[16].getText().replaceAll(",", ".") + ","
//                    + fields[15].getText().replaceAll(",", ".") + ","
//                    + fields[22].getText().replaceAll(",", ".") + ","
//                    + fields[14].getText().replaceAll(",", ".") + ","
//                    + customer + ");");

            setQuerry.closeDB();
        } catch (SQLException ex) {
            Logger.getLogger(ListUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}