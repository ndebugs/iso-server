/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Packager;
import com.debugs.server.iso.xml.adapter.SubFieldsAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlRootElement(name="packager")
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOSubPackager implements Packager<Integer, ISOSubField> {
    
    @XmlTransient
    private String mti;

    @XmlElement
    @XmlJavaTypeAdapter(SubFieldsAdapter.class)
    private Map<Integer, ISOSubField> fields;

    public String getMTI() {
        return mti;
    }

    public void setMTI(String mti) {
        this.mti = mti;
    }
    
    @Override
    public ISOSubField get(Integer key) {
        return fields.get(key);
    }

    @Override
    public void set(Integer key, ISOSubField field) {
        fields.put(key, field);
    }
    
    @Override
    public List<Integer> keys() {
        List<Integer> keys = new ArrayList(fields.keySet());
        Collections.sort(keys);
        return keys;
    }

    public Map<Integer, ISOSubField> getFields() {
        return fields;
    }

    public void setFields(Map<Integer, ISOSubField> fields) {
        this.fields = fields;
    }
}
