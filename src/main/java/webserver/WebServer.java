package webserver;

import http.controller.*;
import http.session.HttpSessionManager;
import http.session.RandomGenerateStrategy;
import http.session.SessionManager;
import http.supoort.RequestMapping;
import http.supoort.converter.HttpMessageConverter;
import http.supoort.converter.request.RequestMessageConverter;
import http.supoort.converter.response.HandleBarViewResolver;
import http.supoort.converter.response.ResponseMessageConverter;
import http.supoort.converter.response.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = getPort(args);

        HttpRequestControllers requestHandlers = initRequestHandlers();
        SessionManager sessionManager = initSessionManager();
        HttpMessageConverter converter = initConverter();

        ExecutorService es = Executors.newFixedThreadPool(100);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                es.execute(new RequestHandler(connection, requestHandlers, sessionManager, converter));
            }
        }
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
    }

    private static int getPort(String[] args) {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        return port;
    }

    private static HttpRequestControllers initRequestHandlers() {
        HttpRequestControllers httpRequestControllers = new HttpRequestControllers(new FileResourceController(RequestMapping.GET("/*")));
        Controller userCreateController = new UserCreateController(RequestMapping.GET("/user/create"), RequestMapping.POST("/user/create"));
        Controller loginController = new LoginController(RequestMapping.POST("/user/login"));

        httpRequestControllers.addHandler(userCreateController);
        httpRequestControllers.addHandler(loginController);
        return httpRequestControllers;
    }

    private static HttpMessageConverter initConverter() {
        return new HttpMessageConverter(new RequestMessageConverter(), new ResponseMessageConverter(initViewResolver()));
    }

    private static ViewResolver initViewResolver() {
        return new HandleBarViewResolver();
    }

    private static SessionManager initSessionManager() {
        return new HttpSessionManager(new RandomGenerateStrategy());
    }
}
