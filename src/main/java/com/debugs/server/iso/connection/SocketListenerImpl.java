/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.connection;

import com.debugs.io.socket.SocketConnection;
import com.debugs.io.socket.SocketListener;
import com.debugs.server.iso.Application;
import com.debugs.server.iso.config.PackagerManager;
import com.debugs.server.iso.config.Settings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SocketListenerImpl implements SocketListener {

    private final PackagerManager manager;
    private final ExecutorService executor;
    private ByteArrayOutputStream buffer;
    private int headerCount;
    private int messageLength;
    private final Logger log;

    public SocketListenerImpl(PackagerManager manager, ExecutorService executor) {
        this.manager = manager;
        this.executor = executor;
        log = LogManager.getLogger();
    }

    public PackagerManager getManager() {
        return manager;
    }

    @Override
    public void onOpen(SocketConnection conn) {
        Socket socket = conn.getSocket();
        log.info("Connection bound to {}:{}.", socket.getInetAddress(), socket.getPort());
    }

    @Override
    public void onClose(SocketConnection conn) {
        Socket socket = conn.getSocket();
        log.info("Connection unbound from {}:{}.", socket.getInetAddress(), socket.getPort());
    }

    @Override
    public void onSend(SocketConnection conn, byte[] data) {
        log.info("Socket Out:\n{}", new String(data));
    }

    @Override
    public void onReceive(SocketConnection conn, ByteArrayInputStream in) {
        Application app = Application.getInstance();
        Settings settings = app.getSettings();
        
        boolean isLowHigh = settings.isLowHigh();
        if (headerCount == 0) {
            messageLength = isLowHigh ? in.read() & 0xff : (in.read() & 0xff) << 8;
            headerCount++;
        }
        if (headerCount == 1 && in.available() > 0) {
            messageLength |= isLowHigh ? (in.read() & 0xff) << 8 : in.read() & 0xff;
            buffer = new ByteArrayOutputStream(messageLength);
            headerCount++;
        }
        int availableBytes = in.available();
        if (headerCount < 2 && availableBytes > 0) {
            return;
        }
        int nextLength = (buffer.size() + availableBytes) - messageLength;
        if (nextLength > 0) {
            availableBytes -= nextLength;
        }
        try {
            byte[] data = new byte[availableBytes];
            in.read(data);
            buffer.write(data);
        } catch (IOException ex) {}
        if (nextLength >= 0) {
            byte[] header = new byte[2];
            writeHeader(header, messageLength);
            
            byte[] dataIn = buffer.toByteArray();
            log.info("Socket In:\n{}{}", new String(header), new String(dataIn));
            
            ReceiverService service = new ReceiverService(conn, dataIn, messageLength);
            executor.execute(service);
            buffer = null;
            headerCount = 0;
            if (in.available() > 0) {
                onReceive(conn, in);
            }
        }
    }

    @Override
    public void onComplete(SocketConnection conn) {
        try {
            conn.close();
        } catch (IOException ex) {}
    }

    @Override
    public void onError(SocketConnection conn, Throwable t) {
        log.catching(t);
        try {
            conn.close();
        } catch (IOException ex) {}
    }
    
    private void writeHeader(byte[] data, int length) {
        Application app = Application.getInstance();
        Settings settings = app.getSettings();
        
        boolean isLowHigh = settings.isLowHigh();
        data[0] = (byte) (isLowHigh ? length & 0xff : (length >> 8) & 0xff);
        data[1] = (byte) (isLowHigh ? (length >> 8) & 0xff : length & 0xff);
    }
    
    public void sendData(SocketConnection conn, String data) {
        int len = data.length();
        byte[] dataOut = new byte[len + 2];
        writeHeader(dataOut, len);
        System.arraycopy(data.getBytes(), 0, dataOut, 2, len);
        
        conn.getSender().send(dataOut);
    }
}
