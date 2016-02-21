/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.message.ISOSubField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubFieldsWrapper {
    
    @XmlElement(name="field")
    private ISOSubField[] fields;

    public ISOSubField[] getFields() {
        return fields;
    }

    public void setFields(ISOSubField[] fields) {
        this.fields = fields;
    }
}
