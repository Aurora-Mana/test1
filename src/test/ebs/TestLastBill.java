package test.ebs;
import main.ebs.LastBill;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.junit.jupiter.api.Assertions.*;


public class TestLastBill {

    private LastBill lastBill;
    @TempDir
    Path tempDir;

    Path tempFile;


    @BeforeEach
    void setUp(){
        lastBill = execute(LastBill::new);
        tempFile = tempDir.resolve("tempFile.txt");
    }


    @AfterEach
    void cleanup() throws IOException {
        // Delete temporary files or directories after each test
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testGUIInitialization() {
        assertNotNull(lastBill.getB1());
        assertNotNull(lastBill.getC1());
        assertNotNull(lastBill.getP1());
        assertNotNull(lastBill.getT1());
    }


    @Test
    void testFindLastBill_WithMatchingMeterNumber() throws IOException {
        // Use temporary file instead of the main file
        Files.write(tempFile, "Meter No: 1001, Month: January, Units Consumed: 20, Total Charges: 300".getBytes());
        BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()));

        String result = lastBill.findLastBill(reader, "1001");

        assertEquals("Meter No: 1001, Month: January, Units Consumed: 20, Total Charges: 300", result);
    }


    @Test
    void testFindLastBill_WithNonMatchingMeterNumber() throws IOException {
        Files.write(tempFile, "Meter No: 1002, Month: January, Units Consumed: 20, Total Charges: 300".getBytes());
        BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()));

        String result = lastBill.findLastBill(reader, "1001");

        assertNull(result);
    }

    @Test
    void testIsMatchingMeterNumber_WithMatchingMeterNumber() {
        boolean result = lastBill.isMatchingMeterNumber("Meter No: 1001", "1001");
        assertTrue(result);
    }

    @Test
    void testIsMatchingMeterNumber_WithNonMatchingMeterNumber() {
        boolean result = lastBill.isMatchingMeterNumber("Meter No: 1002", "1001");

        assertFalse(result);
    }

    @Test
    void testUpdateTextArea_WithLastBillDetails() {
        lastBill.updateTextArea("Meter No: 1001, Month: January, Units Consumed: 20, Total Charges: 300");

        assertEquals("Details of the Last Bill\n\n\n" +
                "Meter No: 1001\nMonth: January\nUnits Consumed: 20\nTotal Charges: 300\n" +
                "---------------------------------------------------------------\n", lastBill.getT1().getText());
    }

    @Test
    void testUpdateTextArea_WithNoBillFound() {
        lastBill.updateTextArea(null);

        assertEquals("No bill found for the selected criteria.", lastBill.getT1().getText());
    }


    @Test
    void testFindLastBill_WithEmptyFile() throws IOException {
        StringReader stringReader = new StringReader("");
        BufferedReader reader = new BufferedReader(stringReader);
        String result = lastBill.findLastBill(reader, "1001");

        assertNull(result);
    }

    @Test
    void testFindLastBill_WithInvalidFileFormat() throws IOException {
        StringReader stringReader = new StringReader("Invalid Format");
        BufferedReader reader = new BufferedReader(stringReader);
        String result = lastBill.findLastBill(reader, "1001");

        assertNull(result);
    }



}



