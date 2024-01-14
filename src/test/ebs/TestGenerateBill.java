package test.ebs;
import main.ebs.GenerateBill;
import main.ebs.ReadData;
import main.ebs.ReadDataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.junit.jupiter.api.Assertions.*;

public class TestGenerateBill {
    private GenerateBill generateBill;

    @BeforeEach
    void setUp() {
        generateBill = execute(GenerateBill::new);
    }

    @Test
    public void testGUIInitialization() {
        assertNotNull(generateBill.getB1());
        assertNotNull(generateBill.getC1());
        assertNotNull(generateBill.getC2());
    }


    @Test
    public void testButtonClick() {
        ActionEvent actionEvent = new ActionEvent(generateBill.getB1(), ActionEvent.ACTION_PERFORMED, null);
        assertDoesNotThrow(() -> generateBill.actionPerformed(actionEvent));
    }

    @Test
    public void testFileReadingDataPopulation() {

        generateBill.getC1().select("1001");
        generateBill.getC2().select("January");

        ActionEvent actionEvent = new ActionEvent(generateBill.getB1(), ActionEvent.ACTION_PERFORMED, null);
        assertDoesNotThrow(() -> generateBill.actionPerformed(actionEvent));
        assertNotNull(generateBill.t1.getText());

    }

    @Test
    public void testFileReadingDataPopulationMock(){
        // Create an instance of the mock and set it as the data provider for GenerateBill
        ReadDataMock readBillDataMock = new ReadDataMock();
        generateBill.getC1().select("1001");
        generateBill.getC2().select("January");
        readBillDataMock.writeIntoFileInfo("1001","January", "50","250");
        generateBill.setReadBillData(readBillDataMock);

        ActionEvent actionEvent = new ActionEvent(generateBill.getB1(), ActionEvent.ACTION_PERFORMED, null);
        assertDoesNotThrow(() -> generateBill.actionPerformed(actionEvent));
        assertNotNull(generateBill.getT1().getText());

    }


    @Test
    public void testSuccessfulBillRetrieval() throws IOException {
        ReadData readBillData = new ReadData();
        generateBill.getC1().select("1001");
        generateBill.getC2().select("January");

        ActionEvent actionEvent = new ActionEvent(generateBill.getB1(), ActionEvent.ACTION_PERFORMED, null);
        assertDoesNotThrow(() -> generateBill.actionPerformed(actionEvent));

        String billData = readBillData.readAndFindBillData("January", "1001");
        String[] billDataParts = billData.split(", ");

        if (!"Not found".equals(billData)) {
            String meterNo = billDataParts[0].split(":")[1].trim();
            String month = billDataParts[1].split(":")[1].trim();
            String unitsConsumed = billDataParts[2].split(":")[1].trim();
            String total = billDataParts[3].split(":")[1].trim();

            String expectedText = "\tReliance Power Limited\nELECTRICITY BILL FOR THE MONTH OF " + month + " ,2018\n\n\n" +
                    "Meter Number: " + meterNo + "\nMonth: " + month + "\nUnits Consumed:  " + unitsConsumed + "\nTotal Charges: " + total +
                    "\n---------------------------------------------------------------\n";

            assertEquals(expectedText, generateBill.getT1().getText());
        } else {
            fail("Bill not found for the selected criteria.");
        }
    }


    @Test
    public void testSuccessfulBillRevivalMock() throws IOException {
        ReadDataMock readBillDataMock = new ReadDataMock();
        generateBill.getC1().select("1002");
        generateBill.getC2().select("March");
        readBillDataMock.writeIntoFileInfo("1002", "March", "40","444");
        generateBill.setReadBillData(readBillDataMock);


        ActionEvent actionEvent = new ActionEvent(generateBill.getB1(), ActionEvent.ACTION_PERFORMED, null);
        assertDoesNotThrow(() -> generateBill.actionPerformed(actionEvent));

        String actualText = generateBill.getT1().getText();
        String expectedText = "\tReliance Power Limited\nELECTRICITY BILL FOR THE MONTH OF March ,2018\n\n\n" +
                "Meter Number: 1002\nMonth: March\nUnits Consumed: 40\nTotal Charges: 444\n---------------------------------------------------------------\n";

        assertEquals(expectedText.trim(), actualText.trim(), "Expected: " + expectedText + "\nActual: " + actualText);

    }




}
