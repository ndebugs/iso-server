/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.type.TObject;
import com.debugs.messaging.type.TypeMismatchException;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ISOSubFieldType extends TObject {

    public ISOSubFieldType(Object value) {
        setValue(value);
    }
    
    @Override
    public Object parse(Object value) throws TypeMismatchException {
        return null;
    }

    @Override
    public TObject clone(Object value) throws TypeMismatchException {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    protected Object[] params() {
        return null;
    }

}
