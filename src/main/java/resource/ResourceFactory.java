package resource;

import java.util.HashMap;
import sax.ReadXMLFileSAX;

public class ResourceFactory {
    final private HashMap<String, Object> resources = new HashMap<>();
    private static ResourceFactory resourceFactory = null;

    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    public Object getResource(String resourceWay) {
        if (resourceWay == null) {
            return null;
        }
        Object object = resources.get(resourceWay);

        if(object == null ) {
            object = ReadXMLFileSAX.readXML(resourceWay + ".xml");
            resources.put(resourceWay, object);
        }
        return object;
    }

    private ResourceFactory () {};
}
