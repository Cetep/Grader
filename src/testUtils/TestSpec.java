package testUtils;

/**
 * Created by Petec on 4.12.2016.
 */
public class TestSpec {
    private String testSpecification;
    private String testValue;

    public TestSpec(String testSpecification, String testValue) {
        this.testSpecification = testSpecification;
        this.testValue = testValue;
    }

    public String getTestSpecification() {
        return testSpecification;
    }

    public void setTestSpecification(String testSpecification) {
        this.testSpecification = testSpecification;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }
}
