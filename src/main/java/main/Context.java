package main;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<Class<?>, Object> context = new HashMap<>();

    public Context () { }

    public void add(Class<?> clazz, Object object) {
        if (!context.containsKey(clazz)) {
            context.put(clazz, object);
        }
    }

    public Object get(Class<?> clazz) {
        if (context.containsKey(clazz)) {
            return context.get(clazz);
        } else {
            return null;
        }
    }

    public void remove(Class<?> clazz) {
        context.remove(clazz);
    }
}
