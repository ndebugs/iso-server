/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.config.MessageType;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class MessageTypesAdapter extends XmlAdapter<MessageTypesWrapper, Map<String, MessageType>> {

    @Override
    public Map<String, MessageType> unmarshal(MessageTypesWrapper v) throws Exception {
        Map<String, MessageType> map = new HashMap(v.getMessageTypes().length);
        for (MessageType type : v.getMessageTypes()) {
            map.put(type.getValue(), type);
        }
        return map;
    }

    @Override
    public MessageTypesWrapper marshal(Map<String, MessageType> v) throws Exception {
        MessageTypesWrapper list = new MessageTypesWrapper();
        MessageType[] messageTypes = new MessageType[v.size()];
        int i = 0;
        for (Map.Entry<String, MessageType> e : v.entrySet()) {
            messageTypes[i++] = e.getValue();
        }
        list.setMessageTypes(messageTypes);
        return list;
    }
}
