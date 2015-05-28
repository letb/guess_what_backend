package sax;
import dbService.DBServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXMLFileSAX {
    static final Logger logger = LogManager.getLogger(DBServiceImpl.class);

    public static Object readXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlFile, handler);
            return handler.getObject();
        } catch (Exception e) {
            logger.info("Error when trying to read file: " + xmlFile + ". Using default settings");
        }
        return null;
    }
}
