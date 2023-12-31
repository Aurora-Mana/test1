package main.ebs;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class CalculateBill extends JFrame implements ActionListener {
    JLabel l1,l2,l3,l4,l5;
    public JTextField t1;
    public Choice c1, c2;
    private JButton b1,b2;
    JPanel p;
    private WriteFileB writeFile = new WriteFileB(); // Initialize the WriteFile object directly

    public void setWriteFile(WriteFileB writeFile) {
        this.writeFile = writeFile;
    }

    public WriteFileB getWriteFile() {
        return writeFile;
    }

    public JButton getB1(){
        return b1;
    }

    public JButton getB2(){
        return b2;
    }

    public CalculateBill(){

        p = new JPanel();
        p.setLayout(new GridLayout(4,2,30,30));
        p.setBackground(Color.WHITE);

        //labels
        l1 = new JLabel("Calculate Electricity Bill");
        l2 = new JLabel("Meter No");
        l3 = new JLabel("Units Consumed");
        l5 = new JLabel("Month");

        t1 = new JTextField();

        //choices for the meter number
        c1 = new Choice();
        c1.add("1001");
        c1.add("1002");
        c1.add("1003");
        c1.add("1004");
        c1.add("1005");
        c1.add("1006");
        c1.add("1007");
        c1.add("1008");
        c1.add("1009");
        c1.add("1010");

        //choices for the month
        c2 = new Choice();
        c2.add("January");
        c2.add("February");
        c2.add("March");
        c2.add("April");
        c2.add("May");
        c2.add("June");
        c2.add("July");
        c2.add("August");
        c2.add("September");
        c2.add("October");
        c2.add("November");
        c2.add("December");

        //buttons
        b1 = new JButton("Submit");
        b2 = new JButton("Cancel");


        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);

        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/hicon2.jpg"));
        Image i2 = i1.getImage().getScaledInstance(180, 270,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        l4 = new JLabel(i3);



        l1.setFont(new Font("Senserif",Font.PLAIN,26));
        //Move the label to center
        l1.setHorizontalAlignment(JLabel.CENTER);



        p.add(l2);
        p.add(c1);
        p.add(l5);
        p.add(c2);
        p.add(l3);
        p.add(t1);
        p.add(b1);
        p.add(b2);

        setLayout(new BorderLayout(30,30));

        add(l1,"North");
        add(p,"Center");
        add(l4,"West");


        b1.addActionListener(this);
        b2.addActionListener(this);

        getContentPane().setBackground(Color.WHITE);
        setSize(650,500);
        setLocation(350,220);
    }

    //get value of meter number selected
    public int getMeterNumber() {

        return Integer.parseInt(c1.getSelectedItem());
    }

    //get value of month selected
    public String getMonth(){

        return c2.getSelectedItem();
    }

    public void setMeterNumber(String meterNumber) {

        c1.select(meterNumber);
    }

    // Set Units Consumed
    public void setUnitsConsumed(String unitsConsumed) {

        t1.setText(unitsConsumed);
    }

    // Set Month
    public void setMonth(String month) {

        c2.select(month);
    }




    public void actionPerformed(ActionEvent ae) {
        ///string for choice of meter number
        String a = c1.getSelectedItem();
        ///string for the unit consumed
        String b = t1.getText();
        ///string for storing the choice for month
        String c = c2.getSelectedItem();

        if (ae.getSource() == b1) {

            try{
                if (!b.matches("\\d+"))
                    JOptionPane.showMessageDialog(null, "Units Consumed should contain only numbers.");
            }catch (NumberFormatException e){
                System.out.println(e.getMessage());
            }

            ///we convert the string containing unit consumed into int
            int p1 = Integer.parseInt(b);
            ///calculations to find how much is the bill
            int p2 = p1 * 7;
            int p3 = p2 + 50 + 12 + 102 + 20 + 50;

            try {
                writeFile.writeBillData(Integer.parseInt(a), p1, c, p3);
                /// When finished message bill updated will be shown
                JOptionPane.showMessageDialog(null, "Bill Updated");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == b2) {
            // Cancel button
            setVisible(false);
        }


    }

    // Method to get the content of a file
    public String getFileContent(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file or file is empty: " + e.getMessage());
        }
        return content.toString();
    }

    public static void main(String[] args){

        new CalculateBill().setVisible(true);
    }
}
