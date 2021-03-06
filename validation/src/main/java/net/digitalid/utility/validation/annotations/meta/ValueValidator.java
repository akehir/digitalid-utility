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
package net.digitalid.utility.validation.annotations.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.validator.ValueAnnotationValidator;

/**
 * This meta-annotation indicates the value validator that validates the (return) value of the annotated variable (or method).
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ValueValidator {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value validator that validates the (return) value of the annotated variable (or method).
     */
    @Nonnull Class<? extends ValueAnnotationValidator> value();
    
}
