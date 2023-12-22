package main.ebs;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class GenerateBill extends JFrame implements ActionListener {
    private JLabel l1;
    public JTextArea t1;
    private JButton b1;
    private Choice c1, c2;
    private JPanel p1;
    private ReadBillData readBillData = new ReadBillData();

    public void setReadBillData(ReadBillData readBillData) {
        this.readBillData = readBillData;
    }

    public GenerateBill() {
        setSize(500, 900);
        setLayout(new BorderLayout());

        p1 = new JPanel();
        l1 = new JLabel("Generate Bill");
        c1 = new Choice();
        c2 = new Choice();

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


        t1 = new JTextArea(50, 15);
        JScrollPane jsp = new JScrollPane(t1);
        t1.setFont(new Font("Senserif", Font.ITALIC, 18));

        b1 = new JButton("Generate Bill");

        p1.add(l1);
        p1.add(c1);
        p1.add(c2);
        add(p1, "North");

        add(jsp, "Center");
        add(b1, "South");

        b1.addActionListener(this);

        setLocation(350, 40);
    }

    public JButton getB1() {
        return b1;
    }

    public Choice getC1(){
        return c1;
    }

    public Choice getC2(){
        return c2;
    }

    public JTextArea getT1(){
        return t1;
    }


    public void actionPerformed(ActionEvent ae) {
        boolean billFound;
        try {

            String month = c2.getSelectedItem();
            String meterNumber = c1.getSelectedItem();
            billFound = false;

            t1.setText("\tReliance Power Limited\nELECTRICITY BILL FOR THE MONTH OF " + month + " ,2018\n\n\n");

            String[] billData = readBillData.readAndFindBillData(month, meterNumber).split(",");
            System.out.println(billData[0]);
            if (billData[0].equals("Not found"))
                t1.setText("No bill found for the selected criteria.");
            else {
                System.out.println(billData[0]);
                System.out.println(billData[1]);
                System.out.println(billData[2]);
                System.out.println(billData[3]);

                t1.append("Meter Number: " + billData[0].substring(10) + "\n"); // Extracting the value after "Meter No: "
                t1.append("Month: " + billData[1].substring(8) + "\n"); // Extracting the value after "Month: "
                t1.append("Units Consumed: " + billData[2].substring(16) + "\n"); // Extracting the value after "Units Consumed: "
                t1.append("Total Charges: " + billData[3].substring(16) + "\n"); // Extracting the value after "Total Charges: "
                t1.append("---------------------------------------------------------------\n");
                billFound = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public static void main(String[] args) {

        new GenerateBill().setVisible(true);
    }


}
