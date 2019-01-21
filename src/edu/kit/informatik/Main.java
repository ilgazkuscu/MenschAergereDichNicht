package edu.kit.informatik;

public class Main {

    /**
     * Main method. Handles the user interactions. Gets the user input from the Terminal class, extracts the command
     * from it, if the user has input a valid one. The only program logic that happens here is checking whether the
     * command is valid, and whether the user has actually given *any* argument at all. Literally anything passes as an
     * argument as long as it exists. The actual verification of the argument happens in its relevant class method.
     * @param args default main args, not meant to do anything.
     */
    public static void main(String[] args) {

        Controller controller = new Controller();
        String input;
        String command;
        String argument = "";

        do {

            input = Terminal.readLine(); // Takes the terminal input from the Terminal class.

            if (input.contains(" ")) { //If there's a space character in the input
                String[] splitInput = input.split(" ", 2); // Split the input into two, from the space char
                command = splitInput[0]; // Put the part that comes before the space into the command string
                argument = splitInput[1]; // Cast the second part as an int, and make it the argument
            } else {
                command = input; // If there's no space character, use the input as-is.
            }

            switch (command) {
                case "start":
                    if (argument == null) {
                        Terminal.printLine(controller.startGame());
                    } else {
                        Terminal.printLine(controller.startGame(argument));
                    }
                    break;
                case "roll":
                    if (argument == null) {
                        Terminal.printError("the result of the dice roll must be given!");
                    } else {
                        Terminal.printLine(controller.rollTheDice(argument));
                    }
                    break;
                case "move":
                    break;
                case "print":
                    break;
                case "abort":
                    controller.resetGame();
                    break;
                case "quit":
                    break;
                default:
                    Terminal.printError("command not recognized!");
                    break;
            }
            argument = null;

        } while (!input.equals("quit"));

    }



}