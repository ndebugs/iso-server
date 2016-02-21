/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageType {
    
    @XmlAttribute
    private String alias;
    
    @XmlValue
    private String value;
    
    public String getAlias() {
        return alias != null ? alias : value;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
