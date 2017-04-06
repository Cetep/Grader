package testUtils;

import java.util.List;

/**
 * Created by Petec on 22.11.2016.
 */
public class Test {
    private String className;
    private String testElement;
    private List<TestSpec> testSpec;

    public Test(String className, String testElement, List<TestSpec> testSpec) {
        this.className = className;
        this.testElement = testElement;
        this.testSpec = testSpec;
        System.out.println("cn: " + className);
        System.out.println("te: " + testElement);
        int i = 0;
        testSpec.forEach(s -> {
            System.out.println("ts: " + s.getTestSpecification());
            System.out.println("tv: " + s.getTestValue());

        });

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTestElement() {
        return testElement;
    }

    public void setTestElement(String testElement) {
        this.testElement = testElement;
    }

    public List<TestSpec> getTestSpec() {
        return testSpec;
    }

    public void setTestSpec(List<TestSpec> testSpec) {
        this.testSpec = testSpec;
    }
}
