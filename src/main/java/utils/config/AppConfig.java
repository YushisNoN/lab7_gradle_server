package utils.config;

import managers.CommandManager;
import managers.ProductManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.*;
import transfer.Request;
import transfer.Response;
import utils.console.ConsoleHandler;

@Configuration
public class AppConfig {

    @Bean
    public CommandManager commandManager() {
        return new CommandManager();
    }

    @Bean
    public ProductManager<?> productManager() {
        return new ProductManager<>();
    }

    @Bean
    public ConsoleHandler consoleHandler() {
        return new ConsoleHandler();
    }

    @Bean
    public Server server() {
        return new Server();
    }

    @Bean
    public RequestHandler requestHandler(Server server) {
        return new RequestHandler(server);
    }

    @Bean
    public ResponseHandler responseHandler(Server server) {
        return new ResponseHandler(server);
    }

    @Bean
    public Response response() {return new Response();}

    @Bean
    public Request request() {return new Request();}
}