package com.debugs.server.iso;

import com.debugs.io.socket.SocketConnection;
import com.debugs.io.socket.SocketListener;
import com.debugs.server.iso.config.PackagerManager;
import com.debugs.server.iso.config.Settings;
import com.debugs.server.iso.connection.SocketListenerImpl;
import com.debugs.server.iso.xml.XMLParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class Application implements Runnable {

    private static Application application;
    
    private Settings settings;
    private ServerSocket server;
    private ExecutorService executor;

    private Application() throws IOException, JAXBException {
        init();
    }

    public Settings getSettings() {
        return settings;
    }
    
    private void init() throws IOException, JAXBException {
        System.setProperty("log4j.configurationFile", "config/log4j2.xml");
        
        InputStream is = new FileInputStream("config/settings.xml");
        XMLParser<Settings> parser = new XMLParser(Settings.class);
        settings = parser.unmarshall(is);
    }
    
    public void start() throws Exception {
        server = new ServerSocket(settings.getPort());
        executor = Executors.newCachedThreadPool();
        executor.execute(this);
    }

    @Override
    public void run() {
        Logger log = LogManager.getLogger();
        PackagerManager manager = new PackagerManager();
        try {
            while (!server.isClosed()) {
                log.info("Waiting for client..");
                SocketListener listener = new SocketListenerImpl(manager, executor);
                SocketConnection conn = new SocketConnection(server.accept(), listener, executor);
                conn.open();
            }
        } catch (IOException e) {
            log.catching(e);
        }
    }
    
    public void stop() throws IOException {
        server.close();
        executor.shutdownNow();
    }
    
    public static Application getInstance() {
        return application;
    }
    
    public static void main(String[] args) throws Exception {
        application = new Application();
        application.start();
        
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    application.stop();
                } catch (Exception e) {}
            }
        });
        Runtime.getRuntime().addShutdownHook(thread);
    }
}
