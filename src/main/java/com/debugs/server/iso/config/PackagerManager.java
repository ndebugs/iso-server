/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.config;

import com.debugs.messaging.Message;
import com.debugs.server.iso.message.ISOField;
import com.debugs.server.iso.message.ISOMessage;
import com.debugs.server.iso.message.ISOPackager;
import com.debugs.server.iso.message.ISOSubField;
import com.debugs.server.iso.message.ISOSubPackager;
import com.debugs.server.iso.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class PackagerManager {
    
    private ISOPackager packager;
    private final Map<String, ISOSubPackager> packagers;
    private final Logger log;

    public PackagerManager() {
        packagers = new HashMap();
        log = LogManager.getLogger();
    }

    public ISOPackager getPackager() {
        return packager;
    }

    public void setPackager(ISOPackager packager) {
        this.packager = packager;
    }
    
    public ISOSubPackager loadRequest(MessageType type) throws FileNotFoundException, JAXBException {
        String filename = "config/req-" + type.getAlias() + ".xml";
        ISOSubPackager subPackager = packagers.get(filename);
        if (subPackager == null) {
            File file = new File(filename);
            subPackager = loadSubPackager(file);
            subPackager.setMTI(type.getValue());
            packagers.put(filename, subPackager);
        } else {
            log.info("Using subpackager:\n{}", filename);
        }
        return subPackager;
    }
    
    public ISOSubPackager loadResponse(Route route, ISOMessage message) throws FileNotFoundException, JAXBException {
        int[] keys = route.getFieldKeys();
        int keysCount = keys != null ? keys.length : 0;
        ISOSubPackager subPackager = null;
        String defaultFilename = "config/res-" + route.getResponseType();
        List<String> keyList = new ArrayList();
        do {
            String filename = (keysCount > 0 ?
                    defaultFilename + mergeValues(keys, message, keysCount) :
                    defaultFilename) +
                    ".xml";
            subPackager = packagers.get(filename);
            if (subPackager == null) {
                File file = new File(filename);
                log.info("Finding subpackager:\n{}", filename);
                
                if (file.exists()) {
                    subPackager = loadSubPackager(file);
                    subPackager.setMTI(route.getResponseType());
                }
                keyList.add(filename);
            } else {
                log.info("Using subpackager:\n{}", filename);
            }
        } while(subPackager == null && --keysCount > -1);
        
        if (subPackager != null) {
            for (String key : keyList) {
                packagers.put(key, subPackager);
            }
        } else {
            log.info("Subpackager not found.");
        }
        return subPackager;
    }
    
    private String mergeValues(int[] keys, Message message, int length) {
        if (keys != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                Object fieldValue = message.get(keys[i]);
                sb.append('-');
                if (fieldValue != null) {
                    sb.append(fieldValue.toString());
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }
    
    private ISOSubPackager loadSubPackager(File file) throws FileNotFoundException, JAXBException {
        if (packager == null) {
            String path = "config/packager.xml";
            log.info("Loading packager:\n{}", path);
            InputStream is = new FileInputStream(path);
            XMLParser<ISOPackager> parser = new XMLParser(ISOPackager.class);
            packager = parser.unmarshall(is);
            log.info("Packager loaded:\n{}", path);
        }
        
        log.info("Loading subpackager:\n{}", file.getPath());
        InputStream is = new FileInputStream(file);
        XMLParser<ISOSubPackager> parser = new XMLParser(ISOSubPackager.class);
        ISOSubPackager subPackager = parser.unmarshall(is);
        for (ISOSubField subField : subPackager.getFields().values()) {
            ISOField field = packager.getFields().get(subField.getId());
            subField.setParent(field);
        }
        log.info("Subpackager loaded:\n{}", file.getPath());
        return subPackager;
    }
}
