package commands;


import commands.exceptions.WrongArgumentsAmountException;

import java.util.HashMap;


public class Help extends Command {
    private HashMap<String, Executable> commandHashMap;

    public Help(HashMap<String, Executable> commandMap) {
        super();
        this.commandHashMap = commandMap;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException {
        if (arguments.length > this.commandArguments) {
            throw new WrongArgumentsAmountException();
        }
    }

    @Override
    public String toString() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help : вывести справку по доступным командам";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        String output = "";
        for (Executable command: this.commandHashMap.values()) {
            if(!command.toString().equals("save"))
                output = output.concat(command.getDescription() + "\n");
        }
        this.response.setResponse(output);
    }
}
