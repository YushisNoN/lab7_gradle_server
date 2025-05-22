package managers;


import commands.Executable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class CommandManager {
    protected HashMap<String, Executable> commandsMap = new HashMap<>();

    public HashMap<String, Executable> getCommandsList() {
        return commandsMap;
    }

    public void addCommand(Executable command) {
        commandsMap.put(command.toString(), command);
    }

}
