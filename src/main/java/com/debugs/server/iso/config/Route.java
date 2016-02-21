/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {
    
    @XmlElement(name="request-type")
    private String requestType;
    
    @XmlElement(name="response-type")
    private String responseType;
    
    @XmlElement(name="field-key")
    @XmlElementWrapper(name="field-keys")
    private int[] fieldKeys;
    
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public int[] getFieldKeys() {
        return fieldKeys;
    }

    public void setFieldKeys(int[] fieldKeys) {
        this.fieldKeys = fieldKeys;
    }
}
