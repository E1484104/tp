package seedu.sudocook;

/**
 * Main class for the SudoCook application.
 * Initializes the application and runs the main loop.
 */
public class SudoCook {
    public static final int DELETE_R_PREFIX = 8;

    private static RecipeBook recipes;
    private static Ui ui;

    private void run() {
        ui = new Ui();
        Parser parser = new Parser(ui);
        recipes = new RecipeBook();
        ui.printWelcome();
        String input = ui.readInput();
        while (!input.equals("bye")) {
            if(input.startsWith("delete-r")){
                int index = Integer.parseInt(input.substring(DELETE_R_PREFIX).trim());
                recipes.removeRecipe(index);
            } else if (input.startsWith("list-r")){
                recipes.listRecipe();
            } else{
                ui.printError("I don't recognise that command!");
            }
            input = ui.readInput();
            ui.printLine();
        }
        ui.printBye();
    }

    public static void main(String[] args) {
        new SudoCook().run();
    }
}
