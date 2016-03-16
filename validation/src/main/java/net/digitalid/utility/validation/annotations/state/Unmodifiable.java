package net.digitalid.utility.validation.annotations.state;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;

/**
 * This annotation indicates that an object is unmodifiable.
 * Only {@link Pure pure} methods can be called on unmodifiable objects.
 * (This annotation is intended for wrappers that still expose modifying methods but
 * throw, for example, an {@link UnsupportedOperationException} if they are called.)
 * 
 * @see Collections#unmodifiableCollection(java.util.Collection)
 * @see Collections#unmodifiableList(java.util.List)
 * @see Collections#unmodifiableMap(java.util.Map)
 * @see Collections#unmodifiableSet(java.util.Set)
 * 
 * @see Modifiable
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Unmodifiable {}