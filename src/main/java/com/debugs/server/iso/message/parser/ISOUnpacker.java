/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message.parser;

import com.debugs.messaging.MessageBundle;
import com.debugs.messaging.evaluator.EvaluatorHandler;
import com.debugs.messaging.evaluator.FieldEvaluationException;
import com.debugs.messaging.evaluator.FieldEvaluator;
import com.debugs.messaging.evaluator.MessageEvaluator;
import com.debugs.messaging.utils.MathUtils;
import com.debugs.server.iso.message.ISOMessage;
import com.debugs.server.iso.message.ISOSubField;
import com.debugs.server.iso.message.ISOSubPackager;
import com.debugs.server.iso.message.attribute.ISOLength;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ISOUnpacker implements EvaluatorHandler<ISOSubField, Integer, String> {
    
    private final byte[] data;
    private final int length;
    private int offset;
    private int bitmapIndex;
    private int bitmapSubIndex;
    private int bitmapOffset;
    private int bitmapLength;
    private byte bitmapHex;

    public ISOUnpacker(byte[] data, int length) {
        this.data = data;
        this.length = length;
    }
    
    @Override
    public Integer nextKey(MessageEvaluator evaluator) {
        while (bitmapIndex < bitmapLength) {
            if (++bitmapSubIndex % 4 == 0) {
                bitmapSubIndex = 0;
                bitmapHex = (byte) Character.digit(data[++bitmapIndex + bitmapOffset], 16);
            }

            if (((bitmapHex >> 3 - bitmapSubIndex) & 1) == 1) {
                int key = (4 * bitmapIndex) + bitmapSubIndex + 1;
                if (key == 1) {
                    bitmapLength *= 2;
                }
                return key;
            }
        }
        return null;
    }

    @Override
    public String nextValue(MessageEvaluator evaluator, ISOSubField field) throws FieldEvaluationException {
        if (field.getParent() == null) {
            Exception clause = new Exception("Field has no parent.");
            throw new FieldEvaluationException(field, clause);
        }
        ISOLength len = field.getLength();
        int lenDigit = len != null && !len.isFixed() ?
                MathUtils.digit(len.getValue()) : 0;
        int lenValue = lenDigit > 0 ?
                Integer.parseInt(new String(data, offset, lenDigit)) : len.getValue();
        offset += lenDigit;
        String value = new String(data, offset, lenValue);
        offset += lenValue;
        return value;
    }

    @Override
    public void onEvaluateObject(FieldEvaluator evaluator, ISOSubField field, String value, int depth) throws Exception {}

    @Override
    public void onPreEvaluateArray(FieldEvaluator evaluator, ISOSubField field, Object[] value, int depth) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPostEvaluateArray(FieldEvaluator evaluator, ISOSubField field, Object[] value, int depth) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPreEvaluateMap(FieldEvaluator evaluator, ISOSubField field, Map value, int depth) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void onPostEvaluateMap(FieldEvaluator evaluator, ISOSubField field, Map value, int depth) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ISOMessage unpack(ISOSubPackager packager) throws Exception {
        ISOMessage message = new ISOMessage();
        message.setLength(length);
        
        String mti = new String(data, 0, 4);
        message.setMTI(mti);
        
        offset = mti.length() + 16;
        bitmapIndex = -1;
        bitmapSubIndex = -1;
        bitmapOffset = mti.length();
        bitmapLength = 16;
        
        MessageEvaluator evaluator = new MessageEvaluator(packager, this);
        
        MessageBundle bundle = new MessageBundle(message);
        evaluator.evaluate(bundle);
        
        Logger log = LogManager.getLogger();
        log.info("-- Unpack Message --\n{}", message);
        return message;
    }
}
