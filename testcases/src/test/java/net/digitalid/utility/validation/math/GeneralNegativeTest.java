package net.digitalid.utility.validation.math;

import net.digitalid.utility.testing.LoggerSetup;
import net.digitalid.utility.validation.annotations.math.Negative;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 */
public class GeneralNegativeTest extends LoggerSetup {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static class TestClassIncorrectFieldType {
        
        @Negative
        public final String negative;
        
        public TestClassIncorrectFieldType(String negative) {
            this.negative = negative;
        }
        
    }
    
//    @Test
//    public void isUnsupportedType() throws Exception {
//        expectedException.expect(ValidationFailedException.class);
//        expectedException.expectMessage("String is not a supported type for @Negative validation.");
//        TestClassIncorrectFieldType testClassIncorrectFieldType = new TestClassIncorrectFieldType("1");
//        AnnotationValidator.validate(testClassIncorrectFieldType);
//    }
    
}