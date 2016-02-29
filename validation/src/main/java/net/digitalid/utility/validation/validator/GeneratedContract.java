package net.digitalid.utility.validation.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

import net.digitalid.utility.contracts.exceptions.ContractViolationException;
import net.digitalid.utility.string.FormatString;
import net.digitalid.utility.string.QuoteString;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.state.Unmodifiable;
import net.digitalid.utility.validation.annotations.string.JavaExpression;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.processing.ProcessingUtility;

/**
 * This class wraps a {@link #getCondition() condition} and {@link #getMessage() message} for annotation processing.
 * 
 * @see AnnotationValidator
 */
@Immutable
public class GeneratedContract {
    
    /* -------------------------------------------------- Condition -------------------------------------------------- */
    
    private final @Nonnull @JavaExpression String condition;
    
    /**
     * Returns the condition which is evaluated during runtime to determine whether the contract is fulfilled.
     */
    @Pure
    public @Nonnull @JavaExpression String getCondition() {
        return condition;
    }
    
    /* -------------------------------------------------- Message -------------------------------------------------- */
    
    private final @Nonnull String message;
    
    /**
     * Returns the message with which a {@link ContractViolationException} is thrown if the condition is violated.
     */
    @Pure
    public @Nonnull String getMessage() {
        return message;
    }
    
    /* -------------------------------------------------- Arguments -------------------------------------------------- */
    
    private final @Unmodifiable @Nonnull @NonNullableElements List<String> arguments;
    
    /**
     * Returns the arguments with which the {@link #getMessage() message} is {@link FormatString#format(java.lang.CharSequence, java.lang.Object...) formatted}.
     */
    @Pure
    public @Unmodifiable @Nonnull @NonNullableElements List<String> getArguments() {
        return arguments;
    }
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    protected GeneratedContract(@Nonnull @JavaExpression String condition, @Nonnull String message, @Nonnull String... arguments) {
        this.condition = condition;
        this.message = message;
        this.arguments = Collections.unmodifiableList(Arrays.asList(arguments));
    }
    
    /**
     * Returns an object that wraps the given {@link #getCondition() condition}, {@link #getMessage() message} and {@link #getArguments() arguments}.
     */
    @Pure
    public static @Nonnull GeneratedContract with(@Nonnull @JavaExpression String condition, @Nonnull String message, @Nonnull String... arguments) {
        return new GeneratedContract(condition, message, arguments);
    }
    
    /**
     * Returns an object that wraps the given {@link #getCondition() condition} and {@link #getMessage() message} with the element name as {@link #getArguments() argument}.
     * Each number sign in the condition and the message is replaced with the {@link AnnotationValidator#getName(javax.lang.model.element.Element) name} of the element.
     */
    @Pure
    public static @Nonnull GeneratedContract with(@Nonnull String condition, @Nonnull String message, @Nonnull Element element, @Nonnull String suffix) {
        final @Nonnull String name = AnnotationValidator.getName(element);
        return new GeneratedContract(condition.replace("#", name), message.replace("#", name), name + (suffix.isEmpty() ? "" : " == null ? null : " + name + suffix));
    }
    
    /**
     * Returns an object that wraps the given {@link #getCondition() condition} and {@link #getMessage() message} with the element name as {@link #getArguments() argument}.
     * Each number sign in the condition and the message is replaced with the {@link AnnotationValidator#getName(javax.lang.model.element.Element) name} of the element.
     */
    @Pure
    public static @Nonnull GeneratedContract with(@Nonnull String condition, @Nonnull String message, @Nonnull Element element) {
        return with(condition, message, element, "");
    }
    
    /**
     * Returns an object that wraps the given {@link #getCondition() condition} and {@link #getMessage() message} with the element name as {@link #getArguments() argument}.
     * Each number sign in the condition and the message is replaced with the {@link AnnotationValidator#getName(javax.lang.model.element.Element) name} of the element.
     * Each at sign in the condition and the message is replaced with the {@link AnnotationValue#getValue() value} of the given annotation mirror.
     */
    @Pure
    public static @Nonnull GeneratedContract with(@Nonnull String condition, @Nonnull String message, @Nonnull Element element, @Nonnull AnnotationMirror annotationMirror, @Nonnull String suffix) {
        final @Nonnull String name = AnnotationValidator.getName(element);
        final @Nullable AnnotationValue annotationValue = ProcessingUtility.getAnnotationValue(annotationMirror);
        final @Nonnull String value = QuoteString.inCode(annotationValue != null ? annotationValue.getValue() : null);
        return new GeneratedContract(condition.replace("#", name).replace("@", value), message.replace("#", name).replace("@", value), name + (suffix.isEmpty() ? "" : " == null ? null : " + name + suffix));
    }
    
    /**
     * Returns an object that wraps the given {@link #getCondition() condition} and {@link #getMessage() message} with the element name as {@link #getArguments() argument}.
     * Each number sign in the condition and the message is replaced with the {@link AnnotationValidator#getName(javax.lang.model.element.Element) name} of the element.
     * Each at sign in the condition and the message is replaced with the {@link AnnotationValue#getValue() value} of the given annotation mirror.
     */
    @Pure
    public static @Nonnull GeneratedContract with(@Nonnull String condition, @Nonnull String message, @Nonnull Element element, @Nonnull AnnotationMirror annotationMirror) {
        return with(condition, message, element, annotationMirror, "");
    }
    
}
