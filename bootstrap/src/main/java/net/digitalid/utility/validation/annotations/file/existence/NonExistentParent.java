package net.digitalid.utility.validation.annotations.file.existence;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the {@link File#getParentFile() parent directory} of the annotated {@link File file} does not {@link File#exists() exist}.
 * 
 * @see ExistentParent
 */
@Documented
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonExistentParent {}
