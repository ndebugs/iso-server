/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Field;
import com.debugs.messaging.MessageBundle;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TObject;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ISOSubFieldValue extends LObject {

    private final ISOSubField field;

    public ISOSubFieldValue(ISOSubField isoSubField) {
        this.field = isoSubField;
    }

    @Override
    public TObject evaluate(MessageBundle bundle, Field field, Object value) throws Exception {
        TObject result = null;
        LObject fieldValue = this.field.getSubValue();
        if (fieldValue != null) {
             result = fieldValue.evaluate(bundle, field, value);
             value = result.getValue();
        }
        
        ISOField parent = this.field.getParent();
        if (parent != null) {
            LObject parentValue = parent.getValue();
            if (parentValue != null) {
                result = parentValue.evaluate(bundle, field, value);
            }
        }
        
        return result != null ? result : new ISOSubFieldType(value);
    }

    @Override
    public Object[] params() {
        return null;
    }
}
