package Library_Mng_Objects_and_props;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


//starting with the login page, we're going to list out the steps and explanation
public class Login_Page extends Objects_and_Properties{
    //For the GUI, we want to open up a window and to do that we need to use jframe
    public static void login() {
        databaseInitialization();
        //name this window login since it's going to be our login page
        JFrame jf = new JFrame("Login");
        //create labels to start building out our skeleton
        //we obviously need a username and password if it's going to be a log in page
        JLabel l1, l2;
        l1 = new JLabel("username");
        l1.setBounds(30,15,100,30);

        l2 = new JLabel("password");
        l2.setBounds(30,50,100,30);

        //now we need to input fields for the user to input string
        JTextField usernameField;
        usernameField= new JTextField("admin");
        usernameField.setBounds(110,15,200,30);

        //we updated this to jpasswordfield rather than jtextfield since it handles passwords better
        JPasswordField passwordField ;
        passwordField= new JPasswordField("admin");
        passwordField.setBounds(110,50,200,30);
        passwordField.setVisible(true);

        //The next logical step is to allow the user to login and we want to have a button for this
        JButton loginButton = new JButton("login");
        loginButton.setBounds(130,90,80,25);
        //now we want to add some functionality to our log in button. Once clicked, it will shoot a connection to the MySql database and execute the query to compare username and password
        //entered to the passwords and usernames stored in the database
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //we want to grab the username and password that was entered
                String attemptUserName = usernameField.getText().trim();
                String attemptPassword = new String(passwordField.getPassword()).trim();

                //now that we have the usernames and passwords, we want to be able to compare this to the data in our db

                //if the username or password are wrong it spits out this message
                if (usernameField.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter a valid username");
                } else if (passwordField.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter a valid password");
                } else {

                    //if they enter a username nad password, we connect and compare to the database
                    Connection connection = connect();
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.executeUpdate("use library");
                        //we want to use the string variabel we just created and pass it into execute query
                        PreparedStatement pstmt = connection.prepareStatement("Select * From users where username = ? and password = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setString(1, attemptUserName);
                        pstmt.setString(2, attemptPassword);
                        ResultSet sqlUsername = pstmt.executeQuery();


                        if (!sqlUsername.next()) {
                            System.out.println("no such user or password");
                            JOptionPane.showMessageDialog(null, "no such username and password exists");
                        } else {
                            //this will exit from the previous login page
                             String admin = sqlUsername.getString("admin");
                                String UID = sqlUsername.getString("UID");
                                if(admin.equals("1")){
                                    create();
                                    jf.dispose();
                                    Admin_Page.admin_menu();
                                } else {
                                    jf.dispose();
                                    Customer_Page.customerMenu();
                                }

                        }
                        //we need to close these in order to free up the pointer resource and not add unnecessary load to the app
                        sqlUsername.close();
                        stmt.close();
                        pstmt.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        jf.add(l1);
        jf.add(l2);
        jf.add(loginButton);
        jf.add(usernameField);
        jf.add(passwordField);
        jf.setSize(1000,1000);
        jf.setLayout(null);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


}
