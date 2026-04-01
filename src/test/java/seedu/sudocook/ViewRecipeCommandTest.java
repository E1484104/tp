package seedu.sudocook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewRecipeCommandTest {
    private RecipeBook recipeBook;
    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        recipeBook = new RecipeBook();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Water", 1, "cup"));
        ArrayList<String> steps = new ArrayList<>();
        steps.add("Boil");
        recipeBook.addRecipe(new Recipe("TestRecipe", ingredients, steps, 10));

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

    // --- ViewRecipeCommand ---
    @Test
    public void viewRecipeCommand_noIndex_viewsAll() {
        ViewRecipeCommand cmd = new ViewRecipeCommand();
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("TestRecipe"));
    }

    @Test
    public void viewRecipeCommand_withValidIndex_viewsOne() {
        ViewRecipeCommand cmd = new ViewRecipeCommand(1);
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("TestRecipe"));
    }

    @Test
    public void viewRecipeCommand_withInvalidIndex_printsError() {
        ViewRecipeCommand cmd = new ViewRecipeCommand(99);
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("out of range"));
    }

    // --- FilterRecipeCommand ---
    @Test
    public void filterRecipeCommand_matchingTime_showsRecipe() {
        FilterRecipeCommand cmd = new FilterRecipeCommand(30);
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("TestRecipe"));
    }

    @Test
    public void filterRecipeCommand_tooLowTime_showsNoResults() {
        FilterRecipeCommand cmd = new FilterRecipeCommand(1);
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("No recipes found matching"));
    }

    // --- ListRecipeCommand ---
    @Test
    public void listRecipeCommand_withRecipes_listsNames() {
        ListRecipeCommand cmd = new ListRecipeCommand();
        cmd.execute(recipeBook);
        assertTrue(getOutput().contains("TestRecipe"));
    }

    @Test
    public void listRecipeCommand_emptyBook_printsNoRecipes() {
        RecipeBook emptyBook = new RecipeBook();
        ListRecipeCommand cmd = new ListRecipeCommand();
        cmd.execute(emptyBook);
        assertTrue(getOutput().contains("No recipes found"));
    }

    // --- HelpCommand ---
    @Test
    public void helpCommand_printsHelpGuide() {
        HelpCommand cmd = new HelpCommand();
        Inventory inventory = new Inventory();
        cmd.execute(inventory);
        assertTrue(getOutput().contains("Help Guide"));
    }

    @Test
    public void helpCommand_executeRecipeBook_doesNothing() {
        HelpCommand cmd = new HelpCommand();
        cmd.execute(recipeBook);
        // execute(RecipeBook) is a no-op, just ensure it doesn't crash
    }
}
