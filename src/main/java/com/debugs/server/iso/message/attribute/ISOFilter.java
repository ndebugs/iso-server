/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message.attribute;

import com.debugs.messaging.attribute.Filter;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TBoolean;
import com.debugs.server.iso.xml.adapter.ValueAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOFilter extends Filter {

    @XmlAttribute
    @Override
    public boolean getError() {
        return super.getError();
    }
    
    @XmlValue
    @XmlJavaTypeAdapter(ValueAdapter.class)
    @Override
    public LObject<TBoolean> getValue() {
        return super.getValue();
    }
}
