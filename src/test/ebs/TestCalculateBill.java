package test.ebs;
import main.ebs.CalculateBill;
import main.ebs.WriteFileMockB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.io.TempDir;
import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.junit.jupiter.api.Assertions.*;


public class TestCalculateBill {

    private CalculateBill bill;

    @BeforeEach
    public void setUp() {
        bill = execute(CalculateBill::new);
    }

    @TempDir
    Path tempDir;

    Path tempFile;


    @Test
    void testInvalidMeterNoNoLessThan1001(){
        assertFalse(bill.getMeterNumber()<1001);
    }

    @Test
    void testInvalidMeterNoNoMoreThan1010(){

        assertFalse(bill.getMeterNumber()>1010);
    }

    @Test
    void testMeterNumberRange() {
        Choice meterChoice = bill.c1;
        // Define the expected range
        int lowerBound = 1001;
        int upperBound = 1010;

        // Use IntStream to check if all meter numbers are within the range
        boolean allWithinRange = IntStream.range(0, meterChoice.getItemCount())
                .allMatch(i -> {
                    String meterNumber = meterChoice.getItem(i);
                    int meterValue = Integer.parseInt(meterNumber);
                    return meterValue >= lowerBound && meterValue <= upperBound;
                });

        assertTrue(allWithinRange, "All meter numbers should be within the range " + lowerBound + " - " + upperBound);
    }


    @Test
    void testValidInputMockVer(){
        WriteFileMockB writeFileMockB = new WriteFileMockB();
        bill.setWriteFile(writeFileMockB);

        bill.c1.select("1001");
        bill.t1.setText("50");
        bill.c2.select("January");

        // Perform action
        bill.getB1().doClick();

        // Assertions
        String fileContent = writeFileMockB.getFileInfo();
        assertTrue(fileContent.contains("Meter No: 1001, Month: January, Units Consumed: 50, Total Charges:"));
    }


    @Test
    void testCalculateBill_CancelOperation() {
        bill.setVisible(true);

        JButton cancelButton = bill.getB2();
        cancelButton.doClick();

        assertFalse(bill.isVisible());
    }


    @Test
    void testInvalidInputForUnitConsumed() {
        // Set necessary values (e.g., Meter No and Units Consumed)
        bill.setMeterNumber("1005");
        bill.setUnitsConsumed("abc");
        bill.setMonth("March");

        assertThrows(NumberFormatException.class,() -> bill.getB1().doClick());
    }



    @Test
    void testGetFileContentWithError() throws IOException {
        tempFile = tempDir.resolve("bill_info.txt");
        Files.deleteIfExists(tempFile);

        String actualContent = bill.getFileContent(tempFile.toString());
        assertNotEquals("Error reading file or file is empty:", actualContent,
                "Error message should be present for a non-existent file or file with no read permissions");
    }


    @Test
    void testGetFileContentEmptyFile() throws IOException {
        tempFile = tempDir.resolve("bill_info.txt");
        Files.createFile(tempFile);
        String actualContent = bill.getFileContent(tempFile.toString());
        assertEquals("", actualContent, "File content should be empty for an empty file");
    }
}










