package commands;

import db.service.ProductService;
import db.service.UserService;
import transfer.Request;
import transfer.Response;

public abstract class Command implements Executable {
    protected String description;
    protected boolean isNeedAllCommands = false;
    protected boolean isNeedArguments = false;
    protected boolean isNeedCollection = false;
    protected int commandArguments = 0;

    protected Request request;

    protected Response response;

    protected ProductService productService;
    protected UserService userService;

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void setResponse(Response response) {this.response = response;}

    @Override
    public String toString() {
        return "Command";
    }

    public boolean getNeededCommands() {
        return this.isNeedAllCommands;
    }

    public boolean getNeededArguments() {
        return this.isNeedArguments;
    }

    public boolean getNeededCollections() {
        return this.isNeedCollection;
    }
    public String getDescription() {
        return "";
    }

    @Override
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
