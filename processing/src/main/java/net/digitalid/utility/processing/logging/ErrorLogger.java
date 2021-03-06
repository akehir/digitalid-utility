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
package net.digitalid.utility.processing.logging;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.logging.Level;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This class allows to inject other behavior into the usage checks of annotation handlers for testing.
 * This class extends {@link ProcessingLog} only for being able to call {@link ProcessingLog#log(net.digitalid.utility.logging.Level, java.lang.CharSequence, net.digitalid.utility.processing.logging.SourcePosition, java.lang.Object...)} directly.
 */
@Mutable
public class ErrorLogger extends ProcessingLog {
    
    /**
     * Stores an immutable instance of the error logger, which can be shared.
     */
    public static final @Nonnull ErrorLogger INSTANCE = new ErrorLogger();
    
    /**
     * Logs the given message with the given position as an error.
     * Each dollar sign in the message is replaced with the corresponding argument.
     */
    @Impure
    public void log(@Nonnull CharSequence message, @Nullable SourcePosition position, @NonCaptured @Unmodified @Nonnull @NullableElements Object... arguments) {
        log(Level.ERROR, message, position, arguments);
    }
    
}
