package org.example;

import java.lang.reflect.Field;
import java.util.Properties;

public class Injector {
    private Properties properties;

    public Injector() {
        PropertyLoader propertyLoader = new PropertyLoader();
        this.properties = propertyLoader.loadProperties();
    }

    public <T> T inject(T object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String implementationClassName = properties.getProperty(fieldType.getName());
                try {
                    Class<?> implementationClass = Class.forName(implementationClassName);
                    Object instance = implementationClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(object, instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}
