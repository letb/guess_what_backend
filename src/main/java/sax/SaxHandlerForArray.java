package sax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import reflection.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;


public class SaxHandlerForArray extends DefaultHandler {
    static final Logger logger = LogManager.getLogger(SaxHandler.class);
    private static String CLASSNAME = "class";
    private String element = null;
    private List<String> words = new ArrayList();
    private int numberOfWords = 0;


    public void startDocument() throws SAXException {
//        logger.info("Start document");
    }

    public void endDocument() throws SAXException {
  //      logger.info("End document ");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(!qName.equals(CLASSNAME)){
            element = qName;
        } else {
            String className = attributes.getValue(0);
    //        logger.info("Class name: " + className);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        element = null;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if(element != null){
            String value = new String(ch, start, length);
      //      logger.info(element + " = " + value);
            words.add(value);
        }
    }

    public String[] getWords() {
        String[] wordsStr = new String[words.size()];
        for(int i = 0; i < words.size(); i++) {
            wordsStr[i] = words.get(i);
        }
        return wordsStr;
    }
}
