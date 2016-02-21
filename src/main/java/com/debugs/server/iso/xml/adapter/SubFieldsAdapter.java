/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.xml.adapter;

import com.debugs.server.iso.message.ISOSubField;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SubFieldsAdapter extends XmlAdapter<SubFieldsWrapper, Map<Integer, ISOSubField>> {

    @Override
    public Map<Integer, ISOSubField> unmarshal(SubFieldsWrapper v) throws Exception {
        Map<Integer, ISOSubField> map = new HashMap(v.getFields().length);
        for (ISOSubField field : v.getFields()) {
            map.put(field.getId(), field);
        }
        return map;
    }

    @Override
    public SubFieldsWrapper marshal(Map<Integer, ISOSubField> v) throws Exception {
        SubFieldsWrapper list = new SubFieldsWrapper();
        ISOSubField[] fields = new ISOSubField[v.size()];
        int i = 0;
        for (Entry<Integer, ISOSubField> e : v.entrySet()) {
            fields[i++] = e.getValue();
        }
        list.setFields(fields);
        return list;
    }
}
