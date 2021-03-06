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
package net.digitalid.utility.collections.collection;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.ownership.Shared;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.collections.iterator.FreezableIterator;
import net.digitalid.utility.collections.list.BackedFreezableList;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.set.BackedFreezableSet;
import net.digitalid.utility.freezable.FreezableInterface;
import net.digitalid.utility.freezable.annotations.Freezable;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.freezable.annotations.NonFrozenRecipient;
import net.digitalid.utility.functional.iterators.ReadOnlyIterableIterator;
import net.digitalid.utility.functional.iterators.ReadOnlyIterator;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.type.ReadOnly;

/**
 * This class implements a {@link Collection collection} that can be {@link FreezableInterface frozen}.
 * It is recommended to use only {@link ReadOnly} or {@link Immutable} types for the elements.
 * The implementation is backed by an ordinary {@link Collection collection}. 
 * 
 * @see BackedFreezableList
 * @see BackedFreezableSet
 */
@GenerateSubclass
@Freezable(ReadOnlyCollection.class)
public abstract class BackedFreezableCollection<E> extends RootClass implements FreezableCollection<E> {
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    /**
     * Stores a reference to the underlying freezable.
     */
    protected final @Shared @Nonnull FreezableInterface freezable;
    
    /**
     * Stores the underlying collection.
     */
    private final @Nonnull Collection<E> collection;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected BackedFreezableCollection(@Nonnull FreezableInterface freezable, @Nonnull Collection<E> collection) {
        this.freezable = freezable;
        this.collection = collection;
    }
    
    /**
     * Returns a new freezable collection backed by the given freezable and collection.
     */
    @Pure
    public static @Capturable <E> @Nonnull BackedFreezableCollection<E> with(@Shared @Modified @Nonnull FreezableInterface freezable, @Captured @Nonnull Collection<E> collection) {
        return new BackedFreezableCollectionSubclass<>(freezable, collection);
    }
    
    /* -------------------------------------------------- Freezable -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isFrozen() {
        return freezable.isFrozen();
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public @Nonnull @Frozen ReadOnlyCollection<E> freeze() {
        freezable.freeze();
        return this;
    }
    
    /* -------------------------------------------------- Cloneable -------------------------------------------------- */
    
    @Pure
    @Override
    public @Capturable @Nonnull @NonFrozen FreezableCollection<E> clone() {
        return FreezableArrayList.withElementsOf(collection);
    }
    
    /* -------------------------------------------------- Collection -------------------------------------------------- */
    
    @Pure
    @Override
    public int size() {
        return collection.size();
    }
    
    @Pure
    @Override
    public @Capturable @Nonnull ReadOnlyIterator<E> iterator() {
        return ReadOnlyIterableIterator.with(collection.iterator());
    }
    
    @Pure
    @Override
    public @Capturable @Nonnull FreezableIterator<E> freezableIterator() {
        return FreezableIterator.with(collection.iterator(), this);
    }
    
    /* -------------------------------------------------- Operations -------------------------------------------------- */
    
    @Impure
    @Override
    @NonFrozenRecipient
    public boolean add(@Captured E element) {
        return collection.add(element);
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public boolean addAll(@NonCaptured @Unmodified @Nonnull Collection<? extends E> c) {
        return collection.addAll(c);
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public boolean remove(@NonCaptured @Unmodified @Nullable Object object) {
        return collection.remove(object);
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public boolean removeAll(@NonCaptured @Unmodified @Nonnull Collection<?> c) {
        return collection.removeAll(c);
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public boolean retainAll(@NonCaptured @Unmodified @Nonnull Collection<?> c) {
        return collection.retainAll(c);
    }
    
    @Impure
    @Override
    @NonFrozenRecipient
    public void clear() {
        collection.clear();
    }
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean equals(@Nullable Object object) {
        return collection.equals(object);
    }
    
    @Pure
    @Override
    public int hashCode() {
        return collection.hashCode();
    }
    
    @Pure
    @Override
    public @Nonnull String toString() {
        return collection.toString();
    }
    
}
