package commands;

import commands.exceptions.WrongArgumentsAmountException;
import db.service.ProductService;
import db.service.UserService;
import models.IncorrectIntegerValueException;
import models.IncorrectStringValueException;
import transfer.Request;
import transfer.Response;

public interface Executable {

    public String getDescription();

    public void execute() throws WrongArgumentsAmountException;

    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException, IncorrectIntegerValueException, WrongArgumentsAmountException, IncorrectStringValueException;

    boolean getNeededArguments();

    public void setRequest(Request request);

    public void setResponse(Response response);

    public void setProductService(ProductService productService);

    public void setUserService(UserService userService);
}
