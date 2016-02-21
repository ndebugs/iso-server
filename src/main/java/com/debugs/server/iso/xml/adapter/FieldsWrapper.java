/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.message.ISOField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldsWrapper {
    
    @XmlElement(name="field")
    private ISOField[] fields;

    public ISOField[] getFields() {
        return fields;
    }

    public void setFields(ISOField[] fields) {
        this.fields = fields;
    }
}
