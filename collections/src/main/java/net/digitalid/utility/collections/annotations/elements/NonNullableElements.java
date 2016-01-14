package net.digitalid.utility.collections.annotations.elements;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import javax.annotation.Nonnull;
import net.digitalid.utility.validation.meta.TargetType;
import net.digitalid.utility.collections.readonly.ReadOnlyCollection;

/**
 * This annotation indicates that the elements of a {@link Collection collection} are {@link Nonnull non-nullable}.
 * (This annotation is only necessary until the source code can be transitioned to Java 1.8 with its type annotations).
 * 
 * @see NullableElements
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@TargetType({Collection.class, ReadOnlyCollection.class, Object[].class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface NonNullableElements {}
