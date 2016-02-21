package com.debugs.server.iso;

import com.debugs.server.iso.config.MessageType;
import com.debugs.server.iso.config.PackagerManager;
import com.debugs.server.iso.config.Route;
import com.debugs.server.iso.config.Settings;
import com.debugs.server.iso.message.ISOMessage;
import com.debugs.server.iso.message.ISOPackager;
import com.debugs.server.iso.message.ISOSubPackager;
import com.debugs.server.iso.message.parser.ISOPacker;
import com.debugs.server.iso.message.parser.ISOUnpacker;
import com.debugs.server.iso.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void test() throws Exception {
        System.setProperty("log4j.configurationFile", "config/log4j2.xml");
        
        testXML("config/settings.xml", Settings.class);
        testXML("config/packager.xml", ISOPackager.class);
        File parent = new File("config/");
        for (String file : parent.list()) {
            if (file.startsWith("req-") || file.startsWith("res-")) {
                testXML("config/" + file, ISOSubPackager.class);
            }
        }
        
        InputStream is = new FileInputStream("config/settings.xml");
        XMLParser<Settings> parser = new XMLParser(Settings.class);
        Settings settings = parser.unmarshall(is);
        
        PackagerManager manager = new PackagerManager();
        testMessage(manager, settings, "080082200000800000000400000000000000101405194684506606770924001");
        testMessage(manager, settings, "0200F23E400188E08008000000000200000016451497170001022938009900000000000010140519568450721619561014091210156010037700377000000000007112345678123456789011111PT. TELKOM - DIVISI MULTIMEDIA         A360013002100730606606001001");
    }
    
    public void testXML(String file, Class cls) throws Exception {
        System.out.println("# --- TEST XML [" + file + "] --- #");
        InputStream is = new FileInputStream(file);
        XMLParser parser = new XMLParser(cls);
        Object target = parser.unmarshall(is);
        
        parser.marshall(target, System.out);
        System.out.println();
    }
    
    public void testMessage(PackagerManager manager, Settings settings, String data) throws Exception {
        System.out.println("# --- TEST MESSAGE --- #");
        String mti = data.substring(0, 4);
        MessageType type = settings.getMessageTypes().get(mti);
        Map<String, Route> routes = settings.getRoutes();
        Route route = routes.get(type.getAlias());
        
        ISOUnpacker packer = new ISOUnpacker(data.getBytes(), data.length());
        ISOSubPackager packagerIn = manager.loadRequest(type);
        ISOMessage request = packer.unpack(packagerIn);
        
        ISOPacker unpacker = new ISOPacker();
        ISOSubPackager packagerOut = manager.loadResponse(route, request);
        String response = unpacker.pack(packagerOut, request);
        System.out.println(response);
        System.out.println();
    }
}
