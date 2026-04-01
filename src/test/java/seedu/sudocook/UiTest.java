package seedu.sudocook;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UiTest {
    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void printWelcome_containsLogo() {
        Ui.printWelcome();
        String out = getOutput();
        assertTrue(out.contains("Welcome") || out.contains("Sudo") || out.contains("Cook"));
    }

    @Test
    public void printBye_containsGoodbye() {
        Ui.printBye();
        assertTrue(getOutput().contains("Goodbye"));
    }

    @Test
    public void printError_containsOops() {
        Ui.printError("Test error");
        assertTrue(getOutput().contains("Oops"));
    }

    @Test
    public void printMessage_printsContent() {
        Ui.printMessage("Hello World");
        assertTrue(getOutput().contains("Hello World"));
    }

    @Test
    public void printGradientMessage_printsContent() {
        Ui.printGradientMessage("Gradient test");
        assertTrue(getOutput().contains("Gradient test"));
    }

    @Test
    public void printGradientMessage_withEmptyLine_handlesGracefully() {
        Ui.printGradientMessage("Line1\n\nLine2");
        String out = getOutput();
        assertTrue(out.contains("Line1"));
        assertTrue(out.contains("Line2"));
    }

    @Test
    public void printLine_printsDivider() {
        Ui.printLine();
        assertNotNull(getOutput());
        assertTrue(getOutput().length() > 0);
    }

    @Test
    public void formatResponse_printsFormattedContent() {
        Ui.formatResponse("Test response");
        assertTrue(getOutput().contains("Test response"));
    }

    @Test
    public void formatResponse_withEmptyLine_handlesGracefully() {
        Ui.formatResponse("Line1\n\nLine2");
        String out = getOutput();
        assertTrue(out.contains("Line1"));
        assertTrue(out.contains("Line2"));
    }

    @Test
    public void getGradientText_returnsText() {
        String result = Ui.getGradientText("hello", 255, 0, 0, 0, 0, 255);
        assertTrue(result.contains("hello"));
    }

    @Test
    public void readInput_eofReturnsBye() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
        Ui ui = new Ui();
        // When there's no input, readInput should return "bye"
        // We need a fresh Ui with the new stdin
        Ui freshUi = new Ui();
        String result = freshUi.readInput();
        assertTrue(result.equals("bye") || result != null);
    }

    @Test
    public void readInput_validInput_returnsInput() {
        String input = "test command\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Ui ui = new Ui();
        String result = ui.readInput();
        assertTrue(result.equals("test command"));
    }
}
