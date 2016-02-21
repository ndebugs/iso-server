/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Message;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ISOMessage implements Message<Object, String> {
    
    private int length;
    private String mti;
    private final Map<Integer, String> values;

    public ISOMessage() {
        values = new HashMap();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
    public String getMTI() {
        return mti;
    }

    public void setMTI(String mti) {
        this.mti = mti;
    }

    @Override
    public String get(Object key) {
        return values.get(key instanceof Integer ?
                (Integer) key : Integer.parseInt(key.toString()));
    }

    @Override
    public void set(Object key, String value) {
        values.put(key instanceof Integer ?
                (Integer) key : Integer.parseInt(key.toString()),
                value);
    }

    @Override
    public List keys() {
        List<Integer> keys = new ArrayList(values.keySet());
        Collections.sort(keys);
        return keys;
    }

    public Map<Integer, String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString())
                .append(": {\n  Length: ").append(length)
                .append(",\n  MTI: ").append(mti);
        for (Integer key : (List<Integer>) keys()) {
            if (key > 1) {
                sb.append(",\n  Bit ").append(key)
                        .append(": ").append(values.get(key));
            }
        }
        sb.append("\n}");
        return sb.toString();
    }
}
