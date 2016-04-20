package net.digitalid.utility.generator.information.field;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.generator.annotations.Default;
import net.digitalid.utility.generator.information.ElementInformationImplementation;
import net.digitalid.utility.processing.utility.ProcessingUtility;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This class implements the {@link FieldInformation} interface.
 * 
 * @see NonDirectlyAccessibleFieldInformation
 * @see DirectlyAccessibleFieldInformation
 */
public abstract class FieldInformationImplementation extends ElementInformationImplementation implements FieldInformation {
    
    /* -------------------------------------------------- Default Value -------------------------------------------------- */
    
    private final @Nullable String defaultValue;
    
    @Pure
    @Override
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }
    
    @Pure
    @Override
    @SuppressWarnings("null")
    public @Nonnull String getDefaultValue() {
        Require.that(hasDefaultValue()).orThrow("This field has to have a default value.");
        
        return defaultValue;
    }
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    protected FieldInformationImplementation(@Nonnull Element element, @Nonnull TypeMirror type, @Nonnull DeclaredType containingType) {
        super(element, type, containingType);
        
        this.defaultValue = ProcessingUtility.getStringValue(element, Default.class);
    }
    
}
