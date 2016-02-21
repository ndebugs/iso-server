/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Packager;
import com.debugs.server.iso.xml.adapter.FieldsAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
@XmlRootElement(name="packager")
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOPackager implements Packager<Integer, ISOField> {
    
    @XmlElement
    @XmlJavaTypeAdapter(FieldsAdapter.class)
    private Map<Integer, ISOField> fields;

    @Override
    public ISOField get(Integer key) {
        return fields.get(key);
    }

    @Override
    public void set(Integer key, ISOField field) {
        fields.put(key, field);
    }

    @Override
    public List<Integer> keys() {
        List<Integer> keys = new ArrayList(fields.keySet());
        Collections.sort(keys);
        return keys;
    }

    public Map<Integer, ISOField> getFields() {
        return fields;
    }

    public void setFields(Map<Integer, ISOField> fields) {
        this.fields = fields;
    }
}
