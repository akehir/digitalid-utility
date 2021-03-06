/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.utility.validation.annotations.elements;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.processing.utility.TypeImporter;
import net.digitalid.utility.validation.annotations.meta.ValueValidator;
import net.digitalid.utility.validation.annotations.type.Stateless;
import net.digitalid.utility.validation.contract.Contract;
import net.digitalid.utility.validation.validators.ElementsValidator;

/**
 * This annotation indicates that the elements of an {@link Iterable iterable} or array are {@link Nonnull non-nullable}.
 * Even though Java 1.8 now supports type annotations, this annotation is still useful for contract generation and arrays.
 * 
 * @see NullableElements
 */
@Documented
@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
@ValueValidator(NonNullableElements.Validator.class)
public @interface NonNullableElements {
    
    /* -------------------------------------------------- Validator -------------------------------------------------- */
    
    /**
     * This class checks the use of and generates the contract for the surrounding annotation.
     */
    @Stateless
    public static class Validator extends ElementsValidator {
        
        /**
         * Returns whether all elements in the given iterable are non-null.
         */
        @Pure
        public static boolean validate(@NonCaptured @Unmodified @Nullable Iterable<?> iterable) {
            if (iterable == null) { return true; }
            for (@Nullable Object element : iterable) {
                if (element == null) { return false; }
            }
            return true;
        }
        
        /**
         * Returns whether all elements in the given array are non-null.
         */
        @Pure
        public static boolean validate(@NonCaptured @Unmodified @Nullable Object[] array) {
            if (array == null) { return true; }
            for (@Nullable Object element : array) {
                if (element == null) { return false; }
            }
            return true;
        }
        
        /**
         * Returns whether all keys and values in the given map are non-null.
         */
        @Pure
        public static boolean validate(@NonCaptured @Unmodified @Nullable Map<?, ?> map) {
            return map == null || !map.containsKey(null) && !map.containsValue(null);
        }
        
        @Pure
        @Override
        public @Nonnull Contract generateContract(@Nonnull Element element, @Nonnull AnnotationMirror annotationMirror, @NonCaptured @Modified @Nonnull TypeImporter typeImporter) {
            return Contract.with(typeImporter.importIfPossible(NonNullableElements.class) + ".Validator.validate(#)", "The # may not contain null.", element);
        }
        
    }
    
}
