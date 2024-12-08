package Library_Mng_Objects_and_props;

import org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary;

import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Admin_Page extends Objects_and_Properties {

    public static void admin_menu() {
        JFrame adminFrame = new JFrame("Admin Page");
        //jf.setSize();

        //ADD TO JFRAME
        JButton addRemoveBookButton = new JButton("Add/Delete Book");
        addRemoveBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] addOrRemoveOption = {"Add", "Delete"};
                int response = JOptionPane.showOptionDialog(null, "Add or Remove Book", "Add/Remove", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, addOrRemoveOption, addOrRemoveOption[0]);
                if (response == 0) {
                    //adminFrame.setVisible(false);
                    addBookFunction();
                    //adminFrame.setVisible(true);
                } else if (response == 1) {
                    //adminFrame.setVisible(false);
                    deleteFunction();
                    //adminFrame.setVisible(true);
                }
            }
        });

        //view books button
        JButton viewBooksBTN = new JButton("View all books");
        viewBooksBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllBooksFunction();
            }
        });

        //view user button
        JButton viewAndUpdateUsersBTN = new JButton("View or update users");
        viewAndUpdateUsersBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndUpdateUsers();
            }
        });
        adminFrame.setLayout(new FlowLayout());
        adminFrame.add(addRemoveBookButton);
        adminFrame.add(viewBooksBTN);
        adminFrame.add(viewAndUpdateUsersBTN);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setVisible(true);
        adminFrame.setSize(1000,1000);
    }


}

//if(rs.next() == false){
//                        System.out.println("No Books on record or database is not properly connnected");
//                        JOptionPane.showMessageDialog(null,"No Books on record, try adding a book");
//                    } else {
//                        jf.dispose();
//                    }

/* a way to enforce date format check through a class
   private boolean dateInputChecker(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String newDate = LocalDate.parse(date, formatter).toString();
            return true;
        } catch (DateTimeParseException e) {
        }
        return false;
    }

 */