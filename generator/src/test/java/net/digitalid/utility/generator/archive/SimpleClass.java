package net.digitalid.utility.generator.archive;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.math.Positive;

@GenerateBuilder
@GenerateSubclass
@GenerateConverter
public abstract class SimpleClass extends RootClass {

    @Pure
    public abstract @Positive /* @Invariant(condition = "number % 3 == 0", message = "The number has to be a multiple of 3 but was $.") */ int getNumber();
    
    @Pure
    public int getHashCode() {
        return hashCode();
    }
    
}
