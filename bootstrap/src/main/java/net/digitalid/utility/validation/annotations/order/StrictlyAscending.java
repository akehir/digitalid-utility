package net.digitalid.utility.validation.annotations.order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the elements of an {@link Iterable iterable} are strictly ascending.
 * 
 * @see Ascending
 * @see Descending
 * @see StrictlyDescending
 */
@Documented
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrictlyAscending {}