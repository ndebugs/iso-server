/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.connection;

import com.debugs.io.socket.SocketConnection;
import com.debugs.server.iso.Application;
import com.debugs.server.iso.config.MessageType;
import com.debugs.server.iso.config.PackagerManager;
import com.debugs.server.iso.config.Route;
import com.debugs.server.iso.config.Settings;
import com.debugs.server.iso.message.ISOMessage;
import com.debugs.server.iso.message.ISOSubPackager;
import com.debugs.server.iso.message.parser.ISOPacker;
import com.debugs.server.iso.message.parser.ISOUnpacker;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ReceiverService implements Runnable {

    private final SocketConnection connection;
    private final byte[] data;
    private final int length;

    public ReceiverService(SocketConnection connection, byte[] data, int length) {
        this.connection = connection;
        this.data = data;
        this.length = length;
    }

    @Override
    public void run() {
        Logger log = LogManager.getLogger();
        
        Application app = Application.getInstance();
        Settings settings = app.getSettings();
    
        SocketListenerImpl listener = (SocketListenerImpl) connection.getListener();
        PackagerManager manager = listener.getManager();
        
        String mti = new String(data, 0, 4);
        MessageType type = settings.getMessageTypes().get(mti);
        Map<String, Route> routes = settings.getRoutes();
        Route route = routes.get(type.getAlias());
        
        ISOUnpacker unpacker = new ISOUnpacker(data, length);
        try {
            ISOSubPackager packagerIn = manager.loadRequest(type);
            ISOMessage request = unpacker.unpack(packagerIn);
            
            ISOPacker packer = new ISOPacker();
            ISOSubPackager packagerOut = manager.loadResponse(route, request);
            String response = packer.pack(packagerOut, request);
            
            listener.sendData(connection, response);
        } catch (Exception e) {
            log.catching(e);
        }
    }
}
