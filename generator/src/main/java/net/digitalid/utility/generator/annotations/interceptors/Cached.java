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
package net.digitalid.utility.generator.annotations.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.type.TypeMirror;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.generator.annotations.meta.Interceptor;
import net.digitalid.utility.generator.generators.SubclassGenerator;
import net.digitalid.utility.generator.information.ElementInformation;
import net.digitalid.utility.generator.information.method.MethodInformation;
import net.digitalid.utility.generator.information.method.MethodParameterInformation;
import net.digitalid.utility.generator.information.type.TypeInformation;
import net.digitalid.utility.generator.interceptor.MethodInterceptor;
import net.digitalid.utility.processing.logging.ProcessingLog;
import net.digitalid.utility.processing.utility.ProcessingUtility;
import net.digitalid.utility.processor.generator.JavaFileGenerator;
import net.digitalid.utility.tuples.Tuple;
import net.digitalid.utility.validation.annotations.type.Stateless;

/**
 * This annotation indicates that the return values for given parameters of annotated methods should be cached.
 * 
 * @see SubclassGenerator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Interceptor(Cached.Interceptor.class)
public @interface Cached {
    
    /**
     * This class generates content for the annotated method.
     */
    @Stateless
    public static class Interceptor extends MethodInterceptor {
        
        @Pure
        @Override
        protected @Nonnull String getPrefix() {
            return "cached";
        }
        
        @Pure
        private @Nonnull String getListOfParameterTypes(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull FiniteIterable<@Nonnull MethodParameterInformation> parameters) {
            return parameters.map(parameter -> javaFileGenerator.importIfPossible(ProcessingUtility.getBoxedType(parameter.getType()))).join();
        }
        
        @Pure
        private @Nonnull String getCacheName(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method) {
            final @Nonnull FiniteIterable<@Nonnull MethodParameterInformation> parameters = method.getParameters();
            return method.getName() + getListOfParameterTypes(javaFileGenerator, parameters).replaceAll("@", "").replaceAll(" ", "").replaceAll("\\.", "").replaceAll(",", "And").replaceAll("<", "Of").replaceAll(">", "").replaceAll("\\?", "Unknown").replaceAll("\\?", "Unknown") + "Cache";
        }
        
        @Pure
        @Override
        public void generateFieldsRequiredByMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull TypeInformation typeInformation) {
            final @Nonnull FiniteIterable<@Nonnull MethodParameterInformation> parameters = method.getParameters();
            @Nullable Class<?> keyType = Tuple.getTupleType(parameters.size());
            @Nonnull String keyTypeAsString;
            if (keyType == null) {
                if (parameters.size() == 0) {
                    keyType = Object.class;
                } else if (parameters.size() == 1) {
                    final @Nonnull TypeMirror typeMirror = ProcessingUtility.getBoxedType(parameters.getFirst().getType());
                    keyType = ProcessingUtility.getClass(typeMirror);
                } else {
                    ProcessingLog.error("Cannot create a cache for more than 8 parameters (method: $).", method.getName());
                }
                keyTypeAsString = javaFileGenerator.importIfPossible(keyType);
            } else {
                keyTypeAsString = javaFileGenerator.importIfPossible(keyType) + Brackets.inPointy(getListOfParameterTypes(javaFileGenerator, parameters));
            }
            @Nonnull final TypeMirror valueType = ProcessingUtility.getBoxedType(method.getReturnType());
            javaFileGenerator.addField("private final @" + javaFileGenerator.importIfPossible(Nonnull.class) + " " + javaFileGenerator.importIfPossible(HashMap.class) + Brackets.inPointy(keyTypeAsString + ", " + javaFileGenerator.importIfPossible(valueType)) + " " + getCacheName(javaFileGenerator, method) + " = new HashMap<>()");
        }
        
        @Pure
        @Override
        protected void implementInterceptorMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull String statement, @Nullable String resultVariable, @Nullable String defaultValue) {
            final @Nonnull FiniteIterable<@Nonnull MethodParameterInformation> parameters = method.getParameters();
            @Nullable Class<?> keyType = Tuple.getTupleType(parameters.size());
            final @Nonnull String keyAccess;
            if (keyType != null) {
                keyAccess = javaFileGenerator.importIfPossible(keyType) + ".of(" + parameters.map(ElementInformation::getName).join() + ")";
            } else {
                if (parameters.size() == 0) {
                    keyAccess = "";
                } else if (parameters.size() == 1) {
                    keyAccess = parameters.getFirst().getName();
                } else {
                    ProcessingLog.error("Cannot cache method results for more than 8 parameters (method: $).", method.getName());
                    keyAccess = "";
                }
            }
            final @Nonnull String cacheName = getCacheName(javaFileGenerator, method);
            @Nonnull final TypeMirror valueType = ProcessingUtility.getBoxedType(method.getReturnType());
            javaFileGenerator.addStatement("final @" + javaFileGenerator.importIfPossible(Nullable.class) + " " + javaFileGenerator.importIfPossible(valueType) + " cached = " + cacheName + ".get(" + keyAccess + ")");
            
            javaFileGenerator.beginIf("cached != null");
            javaFileGenerator.addStatement("return cached");
            javaFileGenerator.endIfBeginElse();
            javaFileGenerator.addStatement(javaFileGenerator.importIfPossible(method.getReturnType()) + " result = " + statement);
            javaFileGenerator.addStatement(cacheName + ".put(" + keyAccess + ", result)");
            javaFileGenerator.addStatement("return result");
            javaFileGenerator.endElse();
        }
    
    }
    
    
    // TODO: Implement a usage check so that this annotation can only be used on methods with a return type!
    
}
