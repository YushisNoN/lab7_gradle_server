package transfer;

import java.io.Serializable;

public class Response  implements Serializable {
    private String response = null;
    private Status status = null;

    private String command = null;

    public Status getStatus() {
        return this.status;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCommand() {
        return this.command;
    }
    public String getResponse() {
        return this.response;
    }
}
