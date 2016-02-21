/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.config;

import com.debugs.server.iso.xml.adapter.MessageTypesAdapter;
import com.debugs.server.iso.xml.adapter.RoutesAdapter;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {
    
    @XmlElement
    private int port;
    
    @XmlElement(name="low-high")
    private boolean lowHigh;
    
    @XmlElement(name="message-types")
    @XmlJavaTypeAdapter(MessageTypesAdapter.class)
    private Map<String, MessageType> messageTypes;
    
    @XmlElement
    @XmlJavaTypeAdapter(RoutesAdapter.class)
    private Map<String, Route> routes;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isLowHigh() {
        return lowHigh;
    }

    public void setLowHigh(boolean lowHigh) {
        this.lowHigh = lowHigh;
    }

    public Map<String, MessageType> getMessageTypes() {
        return messageTypes;
    }

    public void setMessageTypes(Map<String, MessageType> messageTypes) {
        this.messageTypes = messageTypes;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }
}
