package Library_Mng_Objects_and_props;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import net.bytebuddy.asm.Advice;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.Statement;
import static Library_Mng_Objects_and_props.Login_Page.login;
import static Library_Mng_Objects_and_props.Admin_Page.admin_menu;

public class Objects_and_Properties {

    //INITIALIZING DATABASE
    public static void databaseInitialization(){
        System.out.println("initializing the database");


        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet rS = connection.getMetaData().getCatalogs();


            while (rS.next()){
                String databaseName = rS.getString(1);
                if(databaseName.equals("library")){
                    System.out.println("Database exists so we do not need to initialize");
                    return;
                }
            }
            String createTableSQL = ("Create database library");
            //execute the query
            stmt.executeUpdate(createTableSQL);
            System.out.println("successfully created table library");
            //tell sql to navigate to the table we want to use

            stmt.executeUpdate("use library");
            System.out.println("successfully executed query to use library table");
            //we want to create the values and intialize the table's columns

            String createUsersTable = ("create table users(UID int auto_increment primary key, Username varchar(30), password varchar(30), admin int default 0, Birthday date null)");
            stmt.executeUpdate(createUsersTable);
            System.out.println("successfully executed query to create users table");
            //now we want to create the credentials for the admin user

            stmt.executeUpdate("insert into users(username, password, admin) values('admin', 'admin', 1)");
            System.out.println("successfully executed query to initialize admin into users table");
            //now we want to create the table for books
            //0 universally throughout the code indicates that the object is available.

            stmt.executeUpdate("create table books(BID int auto_increment primary key, BookName  varchar(30), author varchar(30), PublicationDate date, Borrowed int default '0', onHold int default '0', Available int default '0') ");
            System.out.println("successfully executed query to create books table");

            stmt.executeUpdate("create table records(RID int auto_increment primary key, Date date, Time varchar(50),action varchar(30), bookID int, bookName varchar(30))");
            System.out.println("successfully executed query to create records table");

            System.out.println("library database was deleted");
            rS.close();
            stmt.close();
            connection.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    //create
    public static void create() {
        try {

            Connection connection = connect();
            Statement stmt = connection.createStatement();
            /* breaking this line down into segments
            1.We want to call the resultset data type from the sql library that integrates into java
            2.we want to use the getMetaData().getCatalogs() function to pull ALL databases available on the server.
            3.we want to then store this as a sql result set that can query into java
            */
            ResultSet rS = connection.getMetaData().getCatalogs();
            //we want to iterate through all the databases
            while (rS.next()) {
                //we are choosing to get the string from index 1 because that is the column where the name of the databases are
                String databaseName = rS.getString(1);
                //we want to create if statements to navigate sql to the proper database and display on java GUI
                if (databaseName.equals("library")) {
                    //show us that the library already exists
                    System.out.println("library database already exists");
                    //we want to give the option to delete the existing database
                    /* breaking down this code
                    1.We declare it as an int because when it's returned, yes is 0, no is 1, and closing the panel is -1
                    2.we use joptionpane.showconfirmdialog since this gives the user a pop up window with yes or no
                    3.joptionpane.showconfirmdialogue has teh following parameters: position, message displayed to user,
                        title of dialogue box, and the yes or no options
                    4. the parent component represents where the window will be positioned and we use null to center it

                    */
                    int response = JOptionPane.showConfirmDialog(null, "library database already exists, do you want to delete and create a new one?", "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        String dropTableSQL = ("Drop database Library");
                        stmt.executeUpdate(dropTableSQL);

                        //now we want to create the database for the gui
                        // create string variable for sql query to initialize database
                        String createTableSQL = ("Create database library");
                        //execute the query
                        stmt.executeUpdate(createTableSQL);
                        //tell sql to navigate to the table we want to use
                        stmt.executeUpdate("use library");
                        //we want to create the values and intialize the table's columns
                        String createUsersTable = ("create table users(UID int auto_increment primary key, Username varchar(30), password varchar(30), admin int default 0, Birthday date null)");
                        stmt.executeUpdate(createUsersTable);
                        //now we want to create the credentials for the admin user
                        stmt.executeUpdate("insert into users(username, password, admin) values('admin', 'admin', 1)");
                        System.out.println("Created user table");
                        //now we want to create the table for books
                        //0 universally throughout the code indicates that the object is available.
                        stmt.executeUpdate("create table books(BID int auto_increment primary key, BookName  varchar(30), author varchar(30), PublicationDate date, Borrowed int default '0', onHold int default '0', Available int default '0') ");
                        System.out.println("Created books table");
                        stmt.executeUpdate("create table records(RID int auto_increment primary key, Date date, Time varchar(50),action varchar(30), bookID int, bookName varchar(30))");
                        System.out.println("library database was deleted");

                    } else if (response == JOptionPane.NO_OPTION) {
                        System.out.println("library database was not deleted");
                        return;
                    } else {
                        System.out.println("no action was taken");
                    }
                    System.out.println("created database");
                }
            }
            rS.close();
            stmt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println("Loaded driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?user=[PLACEHOLDER]&password=[PLACEHOLDER]");
            //System.out.println("Connected to MySQL");
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void addBookFunction() {
        Connection connection = connect();

        JFrame addBookFrame = new JFrame();

        JLabel name, author, publicationDate;

        name = new JLabel("Name of Book");
        name.setBounds(30, 50, 100, 30);
        JTextField nameField = new JTextField();
        nameField.setBounds(30, 75, 100, 30);


        author = new JLabel("Name of Author");
        author.setBounds(30, 100, 100, 30);
        JTextField authorField = new JTextField();
        authorField.setBounds(30, 125, 100, 30);

        publicationDate = new JLabel("Date of Publication");
        publicationDate.setBounds(30, 150, 100, 30);
        JTextField publicationDateField = new JTextField();
        publicationDateField.setBounds(30, 175, 100, 30);

        JButton addBookBtn = new JButton("Add Book");
        addBookBtn.setBounds(30, 250, 100, 30);
        addBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User clicked addbook button");
                String submittedBookName = nameField.getText().trim();
                System.out.println("obtained: " + submittedBookName);
                String submittedAuthorName = authorField.getText().trim();
                System.out.println("obtained: " + submittedAuthorName);
                String submittedDateOfPublication = publicationDateField.getText();
                System.out.println("obtained: " + submittedDateOfPublication);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                if (submittedBookName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a name");
                    System.out.println("no book name was entered");
                } else if (submittedAuthorName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a author");
                    System.out.println("no author name was entered");
                } else if (submittedDateOfPublication.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a publication date");
                    System.out.println("no publication date was entered");
                }

                if (!submittedDateOfPublication.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "enter date in valid format: " + "MM/dd/yyyy");
                    System.out.println("format was not followed");
                    return;
                }
                LocalDate enteredDate = LocalDate.parse(submittedDateOfPublication, formatter);
                System.out.println("obtained: " + Date.valueOf(enteredDate));
                try {
                    PreparedStatement pstmt = connection.prepareStatement("Insert into books(BookName, author, PublicationDate) values(?,?,?)");
                    pstmt.setString(1, submittedBookName);
                    pstmt.setString(2, submittedAuthorName);
                    pstmt.setDate(3, Date.valueOf(enteredDate));
                    //w'ere gonna use the last index to check the data base to see if the book was truly submitted
                    pstmt.executeUpdate();

                    try {
                        pstmt = connection.prepareStatement("Select * from books where BookName = ?");
                        pstmt.setString(1, submittedBookName);
                        ResultSet rs = pstmt.executeQuery();

                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "Book was not successfully added, try again.");
                            System.out.println(submittedBookName + " does not exist and was not successfully added");
                            addBookFrame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, submittedBookName + " was added");
                            System.out.println(submittedBookName + "now exists in database");
                            addBookFrame.setVisible(false);
                        }
                        rs.close();
                        pstmt.close();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1);
                        e1.getCause();
                        System.out.println("Error with book creator checker");
                    }


                    pstmt.close();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                    System.out.println("Error with adding book");
                }
            }
        });
        addBookFrame.add(name);
        addBookFrame.add(author);
        addBookFrame.add(publicationDate);
        addBookFrame.add(nameField);
        addBookFrame.add(authorField);
        addBookFrame.add(publicationDateField);
        addBookFrame.add(addBookBtn);
        addBookFrame.setLayout(null);//using no layout managers
        addBookFrame.setVisible(true);//making the frame visible
        addBookFrame.setLocationRelativeTo(null);
        addBookFrame.setSize(1000, 1000);
    }

    public static void deleteFunction() {
        //DELETE FUNCTION!!!!!!!!!!


        //we want to delete a book but we want to do it in a way where the user can view all books and click
        // on the book they want to delete
        JFrame deleteBookFrame = new JFrame("Delete A Book");
        JTable viewOfAllBooks = new JTable();
        Connection connection = connect();

        //DISPLAY THE TABLE AND ALLOW THEM TO SCROLL
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from books");
            System.out.println("Successfully executed query to view all books");
            viewOfAllBooks.setModel(DbUtils.resultSetToTableModel(rs));
            System.out.println("Successfully set table model");

            //we want to add a scroll bar incase the list is long
            //let the user select more than one row

            viewOfAllBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            viewOfAllBooks.setCellSelectionEnabled(false);
            viewOfAllBooks.setRowSelectionAllowed(true);
            viewOfAllBooks.setColumnSelectionAllowed(false);
            viewOfAllBooks.setVisible(true);
            viewOfAllBooks.setLayout(null);
            deleteBookFrame.add(viewOfAllBooks);


            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error in bringing up table view of all books");
        }
        JScrollPane bookListScroller = new JScrollPane(viewOfAllBooks);
        bookListScroller.setVisible(true);
        bookListScroller.setLayout(null);
        bookListScroller.setBounds(30, 50, 800, 400);
        deleteBookFrame.add(bookListScroller);

        //CREATE BUTTON FOR IF THEY SELECT A ROW AND CLICK DELETE
        JButton deleteBookBTN = new JButton("Delete selected book");
        deleteBookBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User clicked delete book button");
                int[] selectedRows = viewOfAllBooks.getSelectedRows();
                System.out.println("Successfully acquired selected rows into a linear array");
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a book to delete");
                    System.out.println("User did not select a book");
                    return;
                }

                int responseForDelete = JOptionPane.showConfirmDialog(null, "Delete Permanently?", "Confirmation", JOptionPane.YES_NO_OPTION);
                System.out.println("User is asked if they want to delete");
                if (responseForDelete == 0) {
                    System.out.println("User responded yes");
                    try {
                        PreparedStatement pstmt = connection.prepareStatement("delete from books where bookname = ?");
                        //we need to use a for-each loop in order to iterate through the array of booknames

                        //create an arraylist to store the string and print it out in the joptionpane
                        ArrayList<String> deletedBooks = new ArrayList<>();
                        for (int row : selectedRows) {
                            String bookToDelete = viewOfAllBooks.getValueAt(row, 1).toString();
                            deletedBooks.add(bookToDelete);
                            pstmt.setString(1, bookToDelete);
                            pstmt.executeUpdate();
                            System.out.println("Successfully submitted delete book query");
                            System.out.println("Deleted the following from database: " + deletedBooks);
                        }

                        JOptionPane.showMessageDialog(null, "Deleted the following from database: " + deletedBooks);
                        reloadTable("books",viewOfAllBooks);
                        pstmt.close();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Unsuccessfully submitted delete book query");
                    }
                } else if (responseForDelete == 1) {
                    System.out.println("User responded yes");
                    System.out.println("User did not delete book(s)");
                }
            }
        });

    deleteBookFrame.add(viewOfAllBooks);
    deleteBookFrame.add(bookListScroller);
        deleteBookFrame.add(deleteBookBTN);
        deleteBookFrame.setVisible(true);
        deleteBookFrame.setLayout(new FlowLayout());
        deleteBookFrame.setLocationRelativeTo(null);
        deleteBookFrame.setSize(1000, 1000);
    }

    public static void viewAllBooksFunction() {
        JFrame viewBooksFrame = new JFrame();
        JTable viewOfAllBooks = new JTable();

        Connection connection = connect();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from books");
            System.out.println("Successfully executed query to view all books");
            viewOfAllBooks.setModel(DbUtils.resultSetToTableModel(rs));
            System.out.println("Successfully set table model");
            //we want to add a scroll bar incase the list is long
            //let the user select more than one row

            //left this in for scalability later
            viewOfAllBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            viewOfAllBooks.setCellSelectionEnabled(false);
            viewOfAllBooks.setRowSelectionAllowed(true);
            viewOfAllBooks.setColumnSelectionAllowed(false);
            viewOfAllBooks.setVisible(true);


            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JButton borrowBook = new JButton("borrow");
        //borrowBook.setBounds();
        borrowBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = viewOfAllBooks.getSelectedRow();
                System.out.println("Successfully acquired selected row: " + selectedRow);
                String avaliableCheck = viewOfAllBooks.getValueAt(selectedRow, 4).toString();
                System.out.println("Successfully acquired borrow row value: " + avaliableCheck);
                String bookID = viewOfAllBooks.getValueAt(selectedRow, 0).toString();
                System.out.println("Successfully acquired book id row value: " + bookID);
                String bookName = viewOfAllBooks.getValueAt(selectedRow, 1).toString();
                System.out.println("Successfully acquired book name row value: " + bookName);
                if (avaliableCheck == "1") {
                    JOptionPane.showMessageDialog(null, "book is unavailable");
                    System.out.println("Unsuccessful attempt to hold book due to being unavailable");
                    return;
                }
                try {
                    System.out.println("attempting to send SQL query to update book to borrowed");
                    PreparedStatement pstmt = connection.prepareStatement("update books set borrowed = 1 where BID =?");
                    pstmt.setString(1, bookID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "borrowed book for 2 weeks");
                    pstmt.close();
                    recordKeeper(bookName, bookID, "borrow");
                    System.out.println("Successfully sent SQL query to update book to borrowed");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Unsuccessful in sending SQL query to update book to borrowed");
                }
            }
        });

        JButton holdBook = new JButton("Hold");
        borrowBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = viewOfAllBooks.getSelectedRow();
                System.out.println("Successfully acquired selected row: " + selectedRow);
                String avaliableCheck = viewOfAllBooks.getValueAt(selectedRow, 5).toString();
                System.out.println("Successfully acquired hold row value: " + avaliableCheck);
                String bookID = viewOfAllBooks.getValueAt(selectedRow, 0).toString();
                System.out.println("Successfully acquired book id value: " + bookID);
                String bookName = viewOfAllBooks.getValueAt(selectedRow, 1).toString();
                System.out.println("Successfully acquired book name: " + bookName);

                if (avaliableCheck == "1") {
                    JOptionPane.showMessageDialog(null, "book is unavailable");
                    System.out.println("Unsuccessful due to book being unavailable");
                    return;
                }
                try {
                    System.out.println("Attempting to send SQL query to update hold row for: " + bookName);
                    PreparedStatement pstmt = connection.prepareStatement("update books set onhold = 1 where BID =?");
                    pstmt.setString(1, bookID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "book has been placed on hold for 1 week");
                    System.out.println("Successfully placed bookname:" + bookName + "book id: " + bookID + ", on hold " );
                    pstmt.close();
                    recordKeeper(bookName, bookID, "hold");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Unsuccessful in executing SQL query");
                }
            }
        });


        JScrollPane bookListScroller = new JScrollPane(viewOfAllBooks);
        bookListScroller.setVisible(true);
        bookListScroller.setLayout(null);
        //bookListScroller.setBounds(30, 50, 800, 400);
        viewBooksFrame.add(viewOfAllBooks);
        viewBooksFrame.add(bookListScroller);
        viewBooksFrame.setVisible(true);
        viewBooksFrame.setLayout(new FlowLayout());
        viewBooksFrame.setLocationRelativeTo(null);
        viewBooksFrame.setSize(1000, 1000);
        viewBooksFrame.add(borrowBook);
        viewBooksFrame.add(holdBook);

    }

    public static void returnToAdminmenu(JFrame currentFrame) {
        JButton returnToAdminMenu = new JButton("Return To Admin Menu");
        returnToAdminMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFrame != null) {
                    currentFrame.dispose();
                    admin_menu();
                }
            }
        });

    }

    public static void viewAndUpdateUsers() {
        JFrame viewAllUsersADMIN = new JFrame("User List");
        JTable userTable = new JTable();


        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from users");
            System.out.println("Successfully executed query for result set");
            userTable.setModel(DbUtils.resultSetToTableModel(rs));
            System.out.println("Successfully set table model");
            userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            userTable.setCellSelectionEnabled(false);
            userTable.setRowSelectionAllowed(true);
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unsuccessfully executed query for result set");
        }
        viewAllUsersADMIN.add(userTable);
        JScrollPane userViewScroller = new JScrollPane(userTable);
        viewAllUsersADMIN.setLayout(new FlowLayout());
        viewAllUsersADMIN.add(userViewScroller);


        viewAllUsersADMIN.setVisible(true);
        viewAllUsersADMIN.setSize(1000, 1000);

        JButton deleteUserBTN = new JButton("Delete User");
        deleteUserBTN.setBounds(110, 15, 200, 30);
        viewAllUsersADMIN.add(deleteUserBTN);
        deleteUserBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User clicked delete user button");
                int rowSelected = userTable.getSelectedRow();
                System.out.println("Successfully acquired selected row: " + rowSelected);
                String userToDelete = userTable.getValueAt(rowSelected, 2).toString();
                System.out.println("Successfully acquired user to delete: " + userToDelete);
                if (rowSelected == -1) {
                    JOptionPane.showMessageDialog(null, "No user is selected to delete");
                    System.out.println("User did not select a row from the user table");
                    return;
                }
                int responseToDeleteUser = JOptionPane.showConfirmDialog(null, "Permanently delete user: " + userToDelete + "?", "Confirmation", JOptionPane.YES_NO_OPTION);
                System.out.println("User is asked if they want to permanently delete user: " + userToDelete);

                    if(responseToDeleteUser == 0){
                        System.out.println("User confirms that they want to delete user: " + userToDelete);
                        try {
                            System.out.println("Attempting to submit SQL query to delete user");
                            Connection connection = connect();
                            PreparedStatement pstmt = connection.prepareStatement("delete from users where UID = ?");
                            String userID = userTable.getValueAt(rowSelected, 0).toString();
                            System.out.println("Successfully acquired user id: " + userID);
                            pstmt.setString(1, userID);
                            pstmt.executeUpdate();
                            System.out.println("Admin deleted user:" + userID + " from database");
                            pstmt.close();
                            JOptionPane.showMessageDialog(null, "Deleted user:" + userToDelete + ", userid: " + userID + " from database");
                            reloadTable("Users", userTable);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Unsuccessfully attempted to delete user: " + userToDelete);
                        }
                    } else if(responseToDeleteUser == 1) {
                        String userID = userTable.getValueAt(rowSelected, 0).toString();
                        System.out.println("User decided not to delete user:" + userToDelete + ", " + "UID: " + userID);
                    }
                }
        });

        JButton editUserBTN = new JButton("Edit User");
        editUserBTN.setBounds(10, 50, 200, 30);
        viewAllUsersADMIN.add(editUserBTN);
        editUserBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User clicked edit user button");
                viewAllUsersADMIN.setVisible(false);
                JFrame editingUserFrame = new JFrame("Edit User Info");
                editingUserFrame.setSize(1000, 1000);
                editingUserFrame.setVisible(true);
                editingUserFrame.setLayout(null);

                JLabel usernameLabel = new JLabel("Edit Username");
                usernameLabel.setBounds(30, 15, 200, 30);
                JTextField editUserNameField = new JTextField();
                editUserNameField.setBounds(200, 15, 200, 30);
                editingUserFrame.add(usernameLabel);
                editingUserFrame.add(editUserNameField);

                JLabel passwordLabel = new JLabel("Edit Password");
                passwordLabel.setBounds(30, 50, 200, 30);
                JTextField editPasswordField = new JTextField();
                editPasswordField.setBounds(200, 50, 200, 30);
                editingUserFrame.add(passwordLabel);
                editingUserFrame.add(editPasswordField);

                JLabel birthdayLabel = new JLabel("Edit Birthday");
                birthdayLabel.setBounds(30, 85, 200, 30);
                JTextField editBirthdayField = new JTextField();
                editBirthdayField.setBounds(200, 85, 200, 30);
                editingUserFrame.add(birthdayLabel);
                editingUserFrame.add(editBirthdayField);

                JButton submitUserEditBTN = new JButton("Submit Changes");
                submitUserEditBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("User clicked submit changes");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        int rowSelected = userTable.getSelectedRow();
                        System.out.println("Successfully acquired selected row: " + rowSelected);
                        String userToEdit = userTable.getValueAt(rowSelected, 0).toString();
                        System.out.println("Successfully acquired user to edit: " + userToEdit);
                        String editeduserNameField = editUserNameField.getText();
                        System.out.println("Successfully acquired submitted username: " + editeduserNameField);
                        String editedPasswordField = editPasswordField.getText();
                        System.out.println("Successfully acquired submitted password: " + editedPasswordField);
                        String newBirthDate = editBirthdayField.getText();
                        System.out.println("Successfully acquired submitted birthday: " + newBirthDate);
                        LocalDate currentBday = LocalDate.parse(newBirthDate, formatter);
                        System.out.println("Successfully parsed:" + newBirthDate + ", into date format");

                        if (!newBirthDate.matches("\\d{2}" + "/" + "\\d{2}" + "/" + "\\d{4}")) {
                            JOptionPane.showMessageDialog(null, "enter date in valid format: " + "MM/dd/yyy");
                            System.out.println("User submitted date in incorrect format");
                            return;
                        } else if (currentBday.isAfter(LocalDate.now())) {
                            JOptionPane.showMessageDialog(null, "Enter a date that has occurred");
                            System.out.println("User submitted date that has not occurred yet");
                            return;
                        }
                        int responseToSaveEdits = JOptionPane.showConfirmDialog(null, "Confirm you want to save changes?");
                        System.out.println("User is asked if they want to confirm changes");
                        if (responseToSaveEdits == JOptionPane.YES_OPTION) {
                            System.out.println("User confirmed changes");
                            try {
                                System.out.println("Attempting to update " + userToEdit);
                                Connection connection = connect();
                                PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET username = ?, password = ?, birthday = ? where UID = ?");
                                pstmt.setString(1, editeduserNameField);
                                pstmt.setString(2, editedPasswordField);
                                pstmt.setDate(3, Date.valueOf(currentBday));
                                pstmt.setString(4, userToEdit);
                                pstmt.executeUpdate();
                                pstmt.close();
                                JOptionPane.showMessageDialog(null, "Successfully saved changes");
                                System.out.println("User successfully updated");
                                reloadTable("users",userTable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("edits to users were not successfully saved");
                            }
                            editingUserFrame.dispose();
                            viewAllUsersADMIN.setVisible(true);
                        } else if (responseToSaveEdits == JOptionPane.NO_OPTION) {
                            System.out.println("User did not save changes");
                        }
                    }
                });
                submitUserEditBTN.setBounds(200, 200, 200, 30);
                submitUserEditBTN.setVisible(true);
                editingUserFrame.add(submitUserEditBTN);


            }
        });


        //add user button
        JButton addUserBTN = new JButton("Add User");
        addUserBTN.setBounds(10, 85, 200, 30);
        addUserBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User clicked add user button");
                viewAllUsersADMIN.setVisible(false);

                Connection connection = connect();

                int rowSelected = userTable.getSelectedRow();

                JFrame addingUserFrame = new JFrame();
                addingUserFrame.setSize(1000, 1000);
                addingUserFrame.setVisible(true);
                addingUserFrame.setLayout(null);

                JLabel newUserNameLabel = new JLabel("Username");
                newUserNameLabel.setBounds(30, 15, 100, 30);
                JTextField newUserNameField = new JTextField();
                newUserNameField.setBounds(110, 15, 200, 30);
                addingUserFrame.add(newUserNameField);
                addingUserFrame.add(newUserNameLabel);


                JLabel newPasswordLabel = new JLabel("Password");
                newPasswordLabel.setBounds(30, 50, 100, 30);
                JTextField newPasswordField = new JTextField();
                newPasswordField.setBounds(110, 50, 200, 30);
                addingUserFrame.add(newPasswordField);
                addingUserFrame.add(newPasswordLabel);

                JLabel newBirthdayLabel = new JLabel("Birthday");
                newBirthdayLabel.setBounds(30, 85, 100, 30);
                JTextField newBirthdayField = new JTextField("yyyy/MM/dd");
                newBirthdayField.setBounds(110, 85, 200, 30);
                addingUserFrame.add(newBirthdayLabel);
                addingUserFrame.add(newBirthdayField);

                JButton submitNewUserBTN = new JButton("Submit");
                submitNewUserBTN.setBounds(110, 120, 100, 25);
                addingUserFrame.add(submitNewUserBTN);
                submitNewUserBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("User clicked submit new user button");
                        String submittedNewUserNameField = newUserNameField.getText();
                        System.out.println("Successfully acquired username: " + submittedNewUserNameField);
                        String submittednewPasswordField = newPasswordField.getText();
                        System.out.println("Successfully acquired password: " + submittednewPasswordField);
                        String submittedBirthdayField = newBirthdayField.getText().trim();
                        System.out.println("Successfully acquired birthday: " + submittedBirthdayField);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate submittedBirthdayDate = LocalDate.parse(submittedBirthdayField, formatter);
                        System.out.println("Successfully parsed " + submittedBirthdayDate + "to date format");
                        if (!newBirthdayField.getText().matches("\\d{2}" + "/" + "\\d{2}" + "/" + "\\d{4}")) {
                            JOptionPane.showMessageDialog(null, "enter date in valid format: " + "MM/dd/yyyy");
                            System.out.println("User submitted date in incorrect format");
                            return;
                        }
                        try {
                            System.out.println("Attempting to add new user into database");
                            PreparedStatement pstmt = connection.prepareStatement("Insert into users(username, password, birthday) values(?,?,?)");
                            pstmt.setString(1, submittedNewUserNameField);
                            pstmt.setString(2, submittednewPasswordField);
                            pstmt.setDate(3, Date.valueOf(submittedBirthdayDate));
                            pstmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "New user added");
                            System.out.println("New user successfully added");
                            reloadTable("users",userTable);
                            addingUserFrame.dispose();
                            pstmt.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Unsuccessful attempt to add new user into database");
                        }
                        viewAllUsersADMIN.setVisible(true);

                    }
                });
            }
        });
        viewAllUsersADMIN.add(addUserBTN);
    }

    public static void recordKeeper(String bookName, String bookID, String action) {
        System.out.println("recordKeeper function invoked");
        Connection connection = connect();

        LocalDate currentDate = LocalDate.now();
        System.out.println("Acquired current local date");
        LocalTime currentTime = LocalTime.now();
        System.out.println("Acquired current local time");
        String timeRecord = currentTime.toString().trim();
        System.out.println("converted time to string");


        try {
            System.out.println("Attempting to submit record into records table");
            PreparedStatement pstmt = connection.prepareStatement("insert into records(Date, Time, action, bookID, bookName) values(?,?,?,?,?)");
            pstmt.setDate(1, Date.valueOf(currentDate));
            pstmt.setString(2, timeRecord);
            pstmt.setString(3, action);
            pstmt.setString(4, bookID);
            pstmt.setString(5, bookName);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            System.out.println("Successfully submitted record into records table");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unsuccessfully submitted record into records table");
        }
    }

    //to refresh the table on the gui
    public static void reloadTable(String tableName, JTable jTable) {
        System.out.println("reloadTable function has been invoked");
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            ResultSet tableView = stmt.executeQuery("Select * From " + tableName);
            System.out.println("Successfully acquired result set");
            jTable.setModel(DbUtils.resultSetToTableModel(tableView));
            System.out.println("Successfully set table model");

            tableView.close();
            stmt.close();
            connection.close();
            System.out.println("Successfully reloaded table viewer");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error reloading table viewer");
            JOptionPane.showMessageDialog(null, "Error reloading table viewer");

        }
    }
}

//could be useful for logging where problems occurred and on which page :
/*
String frameTitle;
        JFrame nameofFrame = new JFrame();
        frameTitle = nameofFrame.getTitle();

        timestamp date, action varchar(30), bookID int, bookName
* */
//provide the option to backspace