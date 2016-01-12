package net.digitalid.utility.collections.annotations.order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import net.digitalid.utility.annotations.meta.TargetType;
import net.digitalid.utility.collections.readonly.ReadOnlyList;

/**
 * This annotation indicates that the elements of a {@link List list} are {@link ReadOnlyList#isStrictlyDescending() strictly descending}.
 * 
 * @see Ascending
 * @see Descending
 * @see StrictlyAscending
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@TargetType({List.class, ReadOnlyList.class, Object[].class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface StrictlyDescending {}