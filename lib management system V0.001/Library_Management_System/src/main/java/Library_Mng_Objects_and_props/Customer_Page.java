package Library_Mng_Objects_and_props;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Customer_Page extends Objects_and_Properties {
    public static void customerMenu() {
        viewAllBooksFunction();
    }


}

//maybe make a table that stores the date,name of the book that was borrowed

//seperate function where everytime the borrow book function is clicked, it'll create a recording of the book name and date
//can do this by creating a new table jsut for borrowed and held books