/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.config.MessageType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageTypesWrapper {
    
    @XmlElement(name="message-type")
    private MessageType[] messageTypes;

    public MessageType[] getMessageTypes() {
        return messageTypes;
    }

    public void setMessageTypes(MessageType[] messageTypes) {
        this.messageTypes = messageTypes;
    }
}
