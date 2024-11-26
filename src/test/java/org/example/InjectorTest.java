package org.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class InjectorTest {

    private Injector injector;
    private Properties properties;

    @Before
    public void setUp() throws IOException {
        // Создание временного файла config.properties для тестов
        properties = new Properties();
        properties.setProperty("org.example.SomeInterface", "org.example.SomeImpl");
        properties.setProperty("org.example.SomeOtherInterface", "org.example.SODoer");

        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
        }

        injector = new Injector();
    }

    @Test
    public void testInjectSomeBean() {
        SomeBean someBean = new SomeBean();
        injector.inject(someBean);

        assertNotNull("SomeBean should not be null", someBean);
        assertTrue("field1 should be an instance of SomeImpl", someBean.field1 instanceof SomeImpl);
        assertTrue("field2 should be an instance of SODoer", someBean.field2 instanceof SODoer);
    }

    @Test
    public void testInjectWithInvalidImplementation() {
        properties.setProperty("org.example.SomeInterface", "org.example.NonExistentImpl");
        properties.setProperty("org.example.SomeOtherInterface", "org.example.ValidImpl");

        // Перезагрузка инжектора для применения новых свойств
        injector = new Injector();

        SomeBean someBean = new SomeBean();
        injector.inject(someBean);

        assertNotNull("SomeBean should not be null", someBean.field1);
        assertNotNull("field2 should not be null", someBean.field2);
    }

    @Test
    public void testInjectWithMissingPropertiesFile() {
        new File("configur.properties");

        SomeBean someBean = new SomeBean();
        injector.inject(someBean);

        assertNotNull("SomeBean should not be null", someBean.field1);
        assertNotNull("SomeBean should not be null", someBean.field2);
    }

    @After
    public void tearDown() {
        //Удаление временного файла после тестов
        new File("config.properties").delete();
        new File("configur.properties").delete();
    }
}