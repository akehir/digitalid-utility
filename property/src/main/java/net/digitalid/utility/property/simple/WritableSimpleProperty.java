package net.digitalid.utility.property.simple;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

/**
 * This writable property stores a simple value.
 * 
 * @see VolatileWritableSimpleProperty
 */
@Mutable
public abstract class WritableSimpleProperty<V> extends SimpleProperty<V> {
    
    /* -------------------------------------------------- Setter -------------------------------------------------- */
    
    /**
     * Sets the value of this property to the given value.
     * 
     * @return the old value of this property that got replaced by the given value.
     */
    @Impure
    public abstract @Capturable @Valid V set(@Captured @Valid V value);
    
    /* -------------------------------------------------- Notification -------------------------------------------------- */
    
    /**
     * Notifies the observers that the value of this property has been replaced.
     * 
     * @require !Objects.equals(newValue, oldValue) : "The new value may not be the same as the old value.";
     * @require Objects.equals(newValue, get()) : "The new value has to be set for this property.";
     */
    @Pure
    protected void notifyObservers(@NonCaptured @Unmodified @Nonnull @Valid V oldValue, @NonCaptured @Unmodified @Nonnull @Valid V newValue) {
        Require.that(!Objects.equals(newValue, oldValue)).orThrow("The new value $ may not be the same as the old value $.", newValue, oldValue);
        Require.that(Objects.equals(newValue, get())).orThrow("The new value $ has to be set for this property.", newValue);
        
        if (hasObservers()) {
            for (SimpleProperty.@Nonnull Observer<V> observer : getObservers()) {
                observer.replaced(this, oldValue, newValue);
            }
        }
    }
    
}