package utils.kernel;

import commands.*;
import commands.exceptions.WrongArgumentsAmountException;
import commands.exceptions.WrongCommandFoundException;
import db.service.*;
import managers.CommandManager;
import managers.ProductManager;
import models.*;
import server.*;
import transfer.Request;
import transfer.Response;
import utils.Hash;
import utils.console.ConsoleHandler;
import utils.files.FileReader;

import java.io.*;
import java.util.*;
import java.nio.channels.SelectionKey;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Kernel implements CommandLineRunner {
    private Thread consoleInputThread;
    private Scanner consoleScanner;
    private volatile boolean consoleThreadRunning = true;
    private boolean exitProgram = false;
    public ConsoleHandler consoleManager = new ConsoleHandler();
    private CommandManager commandManager = new CommandManager();
    private ProductManager<Product> productManager = new ProductManager<Product>();
    private FileReader fileReader = new FileReader();
    private Server server = new Server();
    private RequestHandler requestHandler = new RequestHandler(this.server);
    private ResponseHandler responseHandler = new ResponseHandler(this.server);
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private SelectionKey key;

    private Request request;
    private Response response;

    private ProductService productService;
    private PersonService personService;
    private CoordinatesService coordinatesService;

    private UserService userService;
    private LocationService locationService;

    private long userId = 0;

    private UserProductService userProductService;
    public void exitProgram() {
        this.consoleThreadRunning = false;
        if (this.consoleInputThread != null) {
            this.consoleInputThread.interrupt();
        }
        if (this.consoleScanner != null) {
            this.consoleScanner.close();
        }
        this.server.stop();
        Executable currentCommand = this.commandManager.getCommandsList().get("save");
        try {
            currentCommand.execute();
        } catch (WrongArgumentsAmountException e) {
            System.out.println(e.getMessage());
        }
        this.exitProgram = true;
    }

    @Autowired
    public Kernel(ProductService productService,
                  CoordinatesService coordinatesService,
                  LocationService locationService,
                  PersonService personService,
                  CommandManager commandManager,
                  ProductManager<Product> productManager,
                  ConsoleHandler consoleManager,
                  Server server,
                  RequestHandler requestHandler,
                  ResponseHandler responseHandler,
                  Response response,
                  Request request,
                  UserService userService,
                  UserProductService userProductService) {
        this.productService = productService;
        this.coordinatesService = coordinatesService;
        this.locationService = locationService;
        this.personService = personService;
        this.commandManager = commandManager;
        this.productManager = productManager;
        this.consoleManager = consoleManager;
        this.server = server;
        this.requestHandler = requestHandler;
        this.response = response;
        this.request = request;
        this.responseHandler = responseHandler;
        this.userService = userService;
        this.userProductService = userProductService;
    }

    public void setCommands() {
        this.commandManager.addCommand(new Help(this.commandManager.getCommandsList()));
        this.commandManager.addCommand(new Add(this.productManager, this.responseHandler, this.requestHandler, this.productService, this.request, this.userService));
        this.commandManager.addCommand(new Info(this.productManager));
        this.commandManager.addCommand(new Show(this.productManager));
        this.commandManager.addCommand(new Clear(this.userProductService));
        this.commandManager.addCommand(new UpdateID(this.productManager, this.responseHandler, this.requestHandler, this.productService, this.request, this.userService));
        this.commandManager.addCommand(new RemoveByID(this.productManager));
        this.commandManager.addCommand(new AddIfMax(this.productManager, this.responseHandler, this.requestHandler, this.productService, this.request, this.userService));
        this.commandManager.addCommand(new AddIfMin(this.productManager, this.responseHandler, this.requestHandler, this.productService, this.request, this.userService));
        this.commandManager.addCommand(new RemoveGreater(this.productManager, this.responseHandler, this.requestHandler, this.productService, this.request, this.userService));
        this.commandManager.addCommand(new CountLessThanPrice(this.productManager));
        this.commandManager.addCommand(new FilterContainsName(this.productManager));
        this.commandManager.addCommand(new RemoveAnyByPrice(this.productManager));
        this.commandManager.addCommand(new Save(this.productManager));
        this.commandManager.addCommand(new Exit(this));
        this.commandManager.addCommand(new ExecuteScript(this));
    }

    public void setOutputToByte() {
        this.originalOut = System.out;
        this.outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }
    public void setOutputOriginal() {
        System.setOut(this.originalOut);
    }

    public String getOutput() {
        return outputStream.toString();
    }

    @Override
    public void run(String... args) {
        this.setCommands();
        try{
            this.server.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.productManager.setCollection(new TreeSet<Product>(this.productService.getAllProducts()));
        this.consoleManager.printStringln("В ожидании запроса от клиента...");
        while (false == this.exitProgram) {
            if (this.exitProgram)
                break;
            try {
                if(System.in.available() > 0) {
                    BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                    String currentInput = consoleReader.readLine();
                    if(currentInput.equals("save")) {
                        Executable currentCommand = this.commandManager.getCommandsList().get("save");
                        currentCommand.execute();
                    }
                }
                if(this.server.getSelector().select(500) == 0) {
                    continue;
                }
                if (!this.server.getSelector().isOpen()) {
                    this.server.start();
                }
                Set<SelectionKey> selectionKeySet = this.server.getSelector().selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
                while(selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if(key.isReadable()) {
                        this.request = this.requestHandler.readRequest(key);
                        this.server.setClientAddress(this.requestHandler.getClientAdress());
                        this.responseHandler.setClientAddress(this.server.getClientAddress());
                        this.responseHandler.setKey(this.requestHandler.getKey());
                        if(request != null) {
                            if(this.request.getUser() != null && this.request.getCommand() == null && this.request.getProduct() == null) {
                                if(this.request.getUser().getStatus().equals("login")) {
                                    if (this.request.getUser().getUsername() != null && !this.request.getUser().getUsername().isEmpty()) {
                                        boolean isUserExists = this.userService.getUserExists(this.request.getUser().getUsername());
                                        Response response1 = new Response();
                                        if (this.request.getUser().getPassword() != null && !this.request.getUser().getPassword().isEmpty()) {
                                            User user = this.userService.getUserByUsername(this.request.getUser().getUsername());
                                            String password = this.request.getUser().getPassword();
                                            String salt = user.getSalt();
                                            String hashedPassword = Hash.hash(password, salt);
                                            if(user.getPassword().equals(hashedPassword)) {
                                                response1.setResponse("ACCEPT");
                                                this.responseHandler.sendResponse(key, response1);
                                            } else {
                                                response1.setResponse("WRONG");
                                                this.responseHandler.sendResponse(key, response1);
                                            }
                                        }
                                        if (isUserExists) {
                                            response1.setResponse("OK");
                                        } else {
                                            response1.setResponse("WRONG");
                                        }
                                        this.responseHandler.sendResponse(key, response1);
                                    }
                                } else if(this.request.getUser().getStatus().equals("signup")){
                                    User user1 = new User();
                                    user1 = this.request.getUser();
                                    String salt = Hash.generateSalt();
                                    String password = Hash.hash(user1.getPassword(), salt);
                                    user1.setPassword(password);
                                    user1.setSalt(salt);
                                    this.userService.saveUser(user1);
                                    System.out.println("Пользователь добавлен в базу данных");
                                    Response response1 = new Response();
                                    response1.setResponse("ACCEPT");
                                    this.responseHandler.sendResponse(key, response1);
                                }
                                this.commandManager.getCommandsList().get("update_id").setRequest(this.request);
                            }
                            this.commandManager.getCommandsList().get("update_id").setRequest(this.request);
                            if(this.request.getProduct() != null) {
                                Product product = this.request.getProduct();
                                User user = this.request.getUser();
                                long userId = this.userService.getId(user.getUsername());
                                this.productService.createProduct(product);
                                long productId = this.productService.getLastId();
                                UserProducts userProducts = new UserProducts();
                                userProducts.setUserId(userId);
                                userProducts.setProductId(productId);
                                UserProducts savedData = this.userProductService.saveUserProducts(userProducts);
                                this.productManager.setCollection(new TreeSet<Product>(this.productService.getAllProducts()));
                                System.out.println("Объект успешно записан в базу данных");
                            } else if (this.request.getCommand() != null) {
                                this.executeCommand(this.request, key);
                            }

                        }
                    }
                    selectionKeyIterator.remove();
                }
            } catch (IOException | WrongArgumentsAmountException e) {
                this.response.setResponse(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e);
            }
        }
    }
    public void executeCommand(Request request, SelectionKey key) {
        String currentInput = request.getCommand();
        String[] currentArguments = Arrays.stream(currentInput.replaceAll("\\s+", " ").trim().split(" "))
                .skip(1).toArray(String[]::new);
        Executable currentCommand = this.commandManager.getCommandsList().get(currentInput.split(" ")[0]);
        Response commandResponse = new Response();
        try {

            if (null == currentCommand) {
                throw new WrongCommandFoundException();
            } else {
                currentCommand.setResponse(commandResponse);
                currentCommand.setRequest(request);
                currentCommand.setProductService(this.productService);
                currentCommand.setUserService(this.userService);
                if (currentCommand.getNeededArguments() || currentArguments.length > 0) {
                    currentCommand.execute(currentArguments);
                } else {
                    currentCommand.execute();
                }

            }
        } catch (Exception exception) {
            commandResponse.setResponse(exception.getMessage());
        }
        try {
            key.interestOps(SelectionKey.OP_READ);
            this.responseHandler.sendResponse(key, commandResponse);
            this.productManager.setCollection(new TreeSet<Product>(this.productService.getAllProducts()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}