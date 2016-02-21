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
import com.debugs.messaging.utils.HexString;
import com.debugs.messaging.utils.MathUtils;
import com.debugs.messaging.utils.Padder;
import com.debugs.server.iso.message.ISOMessage;
import com.debugs.server.iso.message.ISOSubField;
import com.debugs.server.iso.message.ISOSubPackager;
import com.debugs.server.iso.message.attribute.ISOLength;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ISOPacker implements EvaluatorHandler<ISOSubField, Integer, String> {
    
    private List<Integer> keys;
    private int keyIndex;
    private boolean bitmap[];
    private StringBuilder elements;

    @Override
    public Integer nextKey(MessageEvaluator compiler) {
        if (keyIndex < keys.size()) {
            return keys.get(keyIndex++);
        } else {
            return null;
        }
    }

    @Override
    public String nextValue(MessageEvaluator compiler, ISOSubField field) throws FieldEvaluationException {
        return null;
    }

    @Override
    public void onEvaluateObject(FieldEvaluator evaluator, ISOSubField field, String value, int depth) throws Exception {
        if (value != null) {
            bitmap[field.getId() - 1] = true;
            String varLen = generateVariableLength(field, value);
            if (varLen != null) {
                elements.append(varLen);
            }
            elements.append(value);
        }
    }

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
    
    private String generateVariableLength(ISOSubField field, String value) {
        ISOLength length = field.getLength();
        int len = length != null && !length.isFixed() ? value.length() : 0;
        String varLen = null;
        if (len > 0) {
            int digit = MathUtils.digit(length.getValue());
            Padder padder = new Padder('0', true);
            varLen = padder.pad(Integer.toString(len), digit);
        }
        return varLen;
    }
    
    public String pack(ISOSubPackager subPackager, ISOMessage temp) throws Exception {
        keys = subPackager.keys();
        keyIndex = 1;
        boolean hasSecondary = keys.get(keys.size() - 1) > 64;
        bitmap = new boolean[hasSecondary ? 128 : 64];
        bitmap[0] = hasSecondary;
        
        ISOMessage message = new ISOMessage();
        message.setMTI(subPackager.getMTI());
        
        StringBuilder messageString = new StringBuilder();
        messageString.append(subPackager.getMTI());
        
        elements = new StringBuilder();
        
        MessageEvaluator evaluator = new MessageEvaluator(subPackager, this);
        
        MessageBundle bundle = new MessageBundle(temp, message);
        evaluator.evaluate(bundle);
        
        int tempHex = 0;
        for (int i = 0; i < bitmap.length; i++) {
            int index = i % 4;
            if (bitmap[i]) {
                tempHex |= 1 << (3 - index);
            }
            if (index == 3) {
                messageString.append(HexString.toChar(tempHex));
                tempHex = 0;
            }
        }
        messageString.append(elements.toString());
        
        message.setLength(messageString.length());
        
        Logger log = LogManager.getLogger();
        log.info("-- Pack Message --\n{}", message);
        return messageString.toString();
    }
}
