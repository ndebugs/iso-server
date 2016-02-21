/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.message.ISOField;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class FieldsAdapter extends XmlAdapter<FieldsWrapper, Map<Integer, ISOField>> {

    @Override
    public Map<Integer, ISOField> unmarshal(FieldsWrapper v) throws Exception {
        Map<Integer, ISOField> map = new HashMap(v.getFields().length);
        for (ISOField field : v.getFields()) {
            map.put(field.getId(), field);
        }
        return map;
    }

    @Override
    public FieldsWrapper marshal(Map<Integer, ISOField> v) throws Exception {
        FieldsWrapper list = new FieldsWrapper();
        ISOField[] fields = new ISOField[v.size()];
        int i = 0;
        for (Entry<Integer, ISOField> e : v.entrySet()) {
            fields[i++] = e.getValue();
        }
        list.setFields(fields);
        return list;
    }
}
