package net.digitalid.utility.testcases;

import java.util.List;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.method.Pure;

/**
 * Description.
 */
public class GenericClass<G extends GenericClass<G>> {
    
    public <T> T test(@Nonnull T object) {
        return object;
    }
    
    public G get() {
        return null;
    }
    
    public void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }
    
    @Pure
    public static @Nonnull <G extends GenericClass<G>> GenericClass<G> with() {
        return new /*Generated*/GenericClass<>();
    }
    
    public GenericClass() {
        
    }
    
}