package org.example;
import org.example.AutoInjectable;

public class SomeBean {
    @AutoInjectable
    SomeInterface field1;
    @AutoInjectable
    SomeOtherInterface field2;
    public void foo() {
        if(field1 !=null) {
            field1.doSomething();
        }
        if(field2 !=null) {
            field2.doSomethingElse();
        }
    }
}
