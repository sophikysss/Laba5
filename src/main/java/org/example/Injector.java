package org.example;

import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Injector {
    private final Properties properties;
    private static final Logger logger = Logger.getLogger(Injector.class.getName());

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
                System.out.println("Field: " + field.getName() + ", Implementation: " + implementationClassName);
                if (implementationClassName==null) {
                    try
                    {
                        field.setAccessible(true);
                        field.set(object, null);
                    }
                    catch (IllegalAccessException e) {
                        logger.log(Level.SEVERE, "Error setting field to null", e);
                    }

                    continue;
                }

                try {
                    Class<?> implementationClass = Class.forName(implementationClassName);
                    Object instance = implementationClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(object, instance);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error injecting dependency", e);
                    try {
                        field.setAccessible(true);
                        field.set(object, null);
                    } catch (IllegalAccessException illegalAccessException) {
                        logger.log(Level.SEVERE, "Error setting field to null", illegalAccessException);
                    }
                }
            }
        }
        return object;
    }
}
