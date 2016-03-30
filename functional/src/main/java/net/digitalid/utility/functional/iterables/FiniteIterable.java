package net.digitalid.utility.functional.iterables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import net.digitalid.utility.functional.fixes.Fixes;
import net.digitalid.utility.functional.interfaces.BinaryOperator;
import net.digitalid.utility.functional.interfaces.Collector;
import net.digitalid.utility.functional.interfaces.Consumer;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.functional.iterators.ArrayIterator;
import net.digitalid.utility.functional.iterators.CombiningIterator;
import net.digitalid.utility.functional.iterators.CyclingIterator;
import net.digitalid.utility.functional.iterators.FilteringIterator;
import net.digitalid.utility.functional.iterators.FlatteningIterator;
import net.digitalid.utility.functional.iterators.MappingIterator;
import net.digitalid.utility.functional.iterators.PruningIterator;
import net.digitalid.utility.functional.iterators.ReversingIterator;
import net.digitalid.utility.functional.iterators.ZippingIterator;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This interface extends the functional iterable interface to model finite iterables.
 * 
 * @see CollectionIterable
 */
public interface FiniteIterable<E> extends FunctionalIterable<E> {
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Wraps the given collection as a finite iterable.
     */
    @Pure
    public static <E> FiniteIterable<E> of(Collection<? extends E> collection) {
        return new CollectionBasedIterable<>(collection);
    }
    
    /**
     * Wraps the given elements as a finite iterable.
     */
    @Pure
    @SafeVarargs
    public static <E> FiniteIterable<E> of(E... elements) {
        return () -> ArrayIterator.with(elements);
    }
    
    /* -------------------------------------------------- Filtering -------------------------------------------------- */
    
    @Pure
    @Override
    public default FiniteIterable<E> filter(Predicate<? super E> predicate) {
        return () -> FilteringIterator.with(iterator(), predicate);
    }
    
    @Pure
    @Override
    public default FunctionalIterable<E> filterNulls() {
        return filter(element -> element != null);
    }
    
    /* -------------------------------------------------- Mapping -------------------------------------------------- */
    
    @Pure
    @Override
    public default <F> FiniteIterable<F> map(UnaryFunction<? super E, ? extends F> function) {
        return () -> MappingIterator.with(iterator(), function);
    }
    
    /* -------------------------------------------------- Pruning -------------------------------------------------- */
    
    @Pure
    @Override
    public default FiniteIterable<E> skip(long number) {
        return () -> PruningIterator.with(iterator(), number, Long.MAX_VALUE);
    }
    
    /* -------------------------------------------------- Zipping -------------------------------------------------- */
    
    @Pure
    @Override
    public default <F> FiniteIterable<Pair<E, F>> zipShortest(InfiniteIterable<? extends F> iterable) {
        return () -> ZippingIterator.with(iterator(), iterable.iterator(), true);
    }
    
    @Pure
    @Override
    public default <F> FiniteIterable<Pair<E, F>> zipLongest(FiniteIterable<? extends F> iterable) {
        return () -> ZippingIterator.with(iterator(), iterable.iterator(), false);
    }
    
    /* -------------------------------------------------- Flattening -------------------------------------------------- */
    
    @Pure
    @Override
    public default <F> FiniteIterable<F> flatten(long level) {
        return () -> FlatteningIterator.with(iterator(), level);
    }
    
    @Pure
    @Override
    public default <F> FiniteIterable<F> flattenOne() {
        return flatten(1);
    }
    
    @Pure
    @Override
    public default <F> FiniteIterable<F> flattenAll() {
        return flatten(Long.MAX_VALUE);
    }
    
    /* -------------------------------------------------- Size -------------------------------------------------- */
    
    /**
     * Returns the size of this iterable.
     */
    @Pure
    public default long size() {
        return size(Long.MAX_VALUE);
    }
    
    /* -------------------------------------------------- Element -------------------------------------------------- */
    
    /**
     * Returns the first element of this iterable or null if this iterable is empty.
     */
    @Pure
    public default E getFirst() {
        final Iterator<E> iterator = iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
    
    /**
     * Returns the last element of this iterable or null if this iterable is empty.
     */
    @Pure
    public default E getLast() {
        E result = null;
        for (E element : this) { result = element; }
        return result;
    }
    
    /* -------------------------------------------------- Index -------------------------------------------------- */
    
    /**
     * Returns the index of the first occurrence of the given object in this iterable or -1 if this iterable does not contain the given object.
     */
    @Pure
    public default long getFirstIndexOf(E object) {
        long index = 0;
        for (E element : this) {
            if (Objects.equals(object, element)) { return index; }
            index += 1;
        }
        return -1;
    }
    
    /**
     * Returns the index of the last occurrence of the given object in this iterable or -1 if this iterable does not contain the given object.
     */
    @Pure
    public default long getLastIndexOf(E object) {
        long lastIndex = -1;
        long currentIndex = 0;
        for (E element : this) {
            if (Objects.equals(object, element)) { lastIndex = currentIndex; }
            currentIndex += 1;
        }
        return lastIndex;
    }
    
    /* -------------------------------------------------- Counting -------------------------------------------------- */
    
    /**
     * Returns the number of elements in this iterable that equal the given object.
     */
    @Pure
    public default long count(E object) {
        long count = 0;
        for (E element : this) {
            if (Objects.equals(object, element)) { count++; }
        }
        return count;
    }
    
    /* -------------------------------------------------- Containing -------------------------------------------------- */
    
    /**
     * Returns whether this iterable contains the given object.
     */
    @Pure
    public default boolean contains(E object) {
        for (E element : this) {
            if (Objects.equals(object, element)) { return true; }
        }
        return false;
    }
    
    /**
     * Returns whether this iterable contains all of the elements of the given iterable.
     */
    @Pure
    public default boolean containsAll(FiniteIterable<? extends E> iterable) {
        for (E element : iterable) {
            if (!contains(element)) { return false; }
        }
        return true;
    }
    
    /* -------------------------------------------------- Intersecting -------------------------------------------------- */
    
    /**
     * Returns the elements that are contained both in this iterable and the given iterable.
     */
    @Pure
    public default FiniteIterable<E> intersect(FiniteIterable<? super E> iterable) {
        return filter(element -> iterable.contains(element));
    }
    
    /* -------------------------------------------------- Excluding -------------------------------------------------- */
    
    /**
     * Returns the elements that are contained in this iterable but not in the given iterable.
     */
    @Pure
    public default FiniteIterable<E> exclude(FiniteIterable<? super E> iterable) {
        return filter(element -> !iterable.contains(element));
    }
    
    /* -------------------------------------------------- Combining -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable followed by the elements of the given iterable.
     */
    @Pure
    public default FiniteIterable<E> combine(FiniteIterable<? extends E> iterable) {
        return () -> CombiningIterator.with(iterator(), iterable.iterator());
    }
    
    /**
     * Returns the elements of this iterable followed by the elements of the given iterable.
     */
    @Pure
    public default InfiniteIterable<E> combine(InfiniteIterable<? extends E> iterable) {
        return () -> CombiningIterator.with(iterator(), iterable.iterator());
    }
    
    /* -------------------------------------------------- Finding -------------------------------------------------- */
    
    /**
     * Returns the first element of this iterable that fulfills the given predicate or null if no such element is found.
     */
    @Pure
    public default E findFirst(Predicate<? super E> predicate) {
        for (E element : this) {
            if (predicate.evaluate(element)) { return element; }
        }
        return null;
    }
    
    /**
     * Returns the last element of this iterable that fulfills the given predicate or null if no such element is found.
     */
    @Pure
    public default E findLast(Predicate<? super E> predicate) {
        E lastElement = null;
        for (E element : this) {
            if (predicate.evaluate(element)) { lastElement = element; }
        }
        return lastElement;
    }
    
    /**
     * Returns the unique element of this iterable that fulfills the given predicate.
     * 
     * @throws NoSuchElementException if no unique element is found in this iterable.
     */
    @Pure
    public default E findUnique(Predicate<? super E> predicate) {
        E uniqueElement = null;
        boolean found = false;
        for (E element : this) {
            if (predicate.evaluate(element)) {
                if (found) {
                    throw new NoSuchElementException("More than one elements fulfill the given predicate.");
                } else {
                    uniqueElement = element;
                    found = true;
                }
            }
        }
        if (found) { return uniqueElement; }
        else { throw new NoSuchElementException("No element fulfills the given predicate."); }
    }
    
    /* -------------------------------------------------- Matching -------------------------------------------------- */
    
    /**
     * Returns whether any elements of this iterable match the given predicate.
     */
    @Pure
    public default boolean matchAny(Predicate<? super E> predicate) {
        for (E element : this) {
            if (predicate.evaluate(element)) { return true; }
        }
        return false;
    }
    
    /**
     * Returns whether all elements of this iterable match the given predicate.
     */
    @Pure
    public default boolean matchAll(Predicate<? super E> predicate) {
        for (E element : this) {
            if (!predicate.evaluate(element)) { return false; }
        }
        return true;
    }
    
    /**
     * Returns whether no element of this iterable matches the given predicate.
     */
    @Pure
    public default boolean matchNone(Predicate<? super E> predicate) {
        return !matchAny(predicate);
    }
    
    /* -------------------------------------------------- Reducing -------------------------------------------------- */
    
    /**
     * Returns the value reduced by the given operator or the given nullable element if this iterable is empty.
     */
    @Pure
    public default E reduce(BinaryOperator<E> operator, E element) {
        final Iterator<E> iterator = iterator();
        if (iterator.hasNext()) {
            E result = iterator.next();
            while (iterator.hasNext()) {
                result = operator.evaluate(result, iterator.next());
            }
            return result;
        } else {
            return element;
        }
    }
    
    /**
     * Returns the value reduced by the given operator or null if this iterable is empty.
     */
    @Pure
    public default E reduce(BinaryOperator<E> operator) {
        return reduce(operator, null);
    }
    
    /* -------------------------------------------------- Minimum -------------------------------------------------- */
    
    /**
     * Returns the minimum element of this iterable according to the given comparator or null if this iterable is empty.
     */
    @Pure
    public default E min(Comparator<? super E> comparator) {
        return reduce(BinaryOperator.min(comparator));
    }
    
    /**
     * Returns the minimum element of this iterable according to the natural order or null if this iterable is empty.
     * 
     * @throws ClassCastException if the elements of this iterable are not comparable.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public default E min() {
        return reduce((a, b) -> a == null ? b : (b == null ? a : ( ((Comparable<? super E>) a).compareTo(b) <= 0 ? a : b )));
    }
    
    /* -------------------------------------------------- Maximum -------------------------------------------------- */
    
    /**
     * Returns the maximum element of this iterable according to the given comparator or null if this iterable is empty.
     */
    @Pure
    public default E max(Comparator<? super E> comparator) {
        return reduce(BinaryOperator.max(comparator));
    }
    
    /**
     * Returns the maximum element of this iterable according to the natural order or null if this iterable is empty.
     * 
     * @throws ClassCastException if the elements of this iterable are not comparable.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public default E max() {
        return reduce((a, b) -> a == null ? b : (b == null ? a : ( ((Comparable<? super E>) a).compareTo(b) >= 0 ? a : b )));
    }
    
    /* -------------------------------------------------- Summation -------------------------------------------------- */
    
    /**
     * Returns the sum of all {@link Number numbers} in this iterable as a long.
     */
    @Pure
    public default long sumAsLong() {
        long sum = 0;
        for (E element : this) {
            if (element instanceof Number) {
                sum += ((Number) element).longValue();
            }
        }
        return sum;
    }
    
    /**
     * Returns the sum of all {@link Number numbers} in this iterable as a double.
     */
    @Pure
    public default double sumAsDouble() {
        double sum = 0;
        for (E element : this) {
            if (element instanceof Number) {
                sum += ((Number) element).doubleValue();
            }
        }
        return sum;
    }
    
    /* -------------------------------------------------- Average -------------------------------------------------- */
    
    /**
     * Returns the average of all {@link Number numbers} in this iterable.
     */
    @Pure
    public default double average() {
        double sum = 0;
        int counter = 0;
        for (E element : this) {
            if (element instanceof Number) {
                sum += ((Number) element).doubleValue();
                counter++;
            }
        }
        return sum / counter;
    }
    
    /* -------------------------------------------------- Grouping -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable as a capturable map grouped by the given function.
     */
    @Pure
    public default <K> Map<K, List<E>> groupBy(UnaryFunction<? super E, ? extends K> function) {
        final Map<K, List<E>> result = new LinkedHashMap<>((int) size());
        for (E element : this) {
            final K key = function.evaluate(element);
            List<E> list = result.get(key);
            if (list == null) {
                list = new LinkedList<>();
                result.put(key, list);
            }
            list.add(element);
        }
        return result;
    }
    
    /* -------------------------------------------------- Collecting -------------------------------------------------- */
    
    /**
     * Returns the result of the given collector after consuming all elements of this iterable.
     */
    @Pure
    public default <R> R collect(Collector<? super E, ? extends R> collector) {
        for (E element : this) { collector.consume(element); }
        return collector.getResult();
    }
    
    /* -------------------------------------------------- Action -------------------------------------------------- */
    
    /**
     * Performs the given action for each element of this iterable.
     */
    public default void forEach(Consumer<? super E> action) {
        for (E element : this) { action.consume(element); }
    }
    
    /* -------------------------------------------------- Exports -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable as a capturable array.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public default E[] toArray() {
        final Object[] array = new Object[(int) size()];
        int index = 0;
        for (E element : this) {
            array[index++] = element;
        }
        return (E[]) array;
    }
    
    /**
     * Returns the elements of this iterable as a capturable list.
     */
    @Pure
    public default List<E> toList() {
        final List<E> result = new LinkedList<>();
        for (E element : this) {
            result.add(element);
        }
        return result;
    }
    
    /**
     * Returns the elements of this iterable as a capturable set.
     */
    @Pure
    public default Set<E> toSet() {
        final Set<E> result = new LinkedHashSet<>();
        for (E element : this) {
            result.add(element);
        }
        return result;
    }
    
    /**
     * Returns the elements of this iterable as a capturable map with their key determined by the given function.
     * Elements that are mapped to the same key overwrite each other. If this is not desired, use
     * {@link #groupBy(net.digitalid.utility.functional.interfaces.UnaryFunction)} instead.
     */
    @Pure
    public default <K> Map<K, E> toMap(UnaryFunction<? super E, ? extends K> function) {
        final Map<K, E> result = new LinkedHashMap<>();
        for (E element : this) {
            result.put(function.evaluate(element), element);
        }
        return result;
    }
    
    /* -------------------------------------------------- Sorting -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable sorted according to the given comparator.
     */
    @Pure
    public default FiniteIterable<E> sorted(Comparator<? super E> comparator) {
        final List<E> list = toList();
        Collections.sort(list, comparator);
        return FiniteIterable.of(list);
    }
    
    /**
     * Returns the elements of this iterable sorted according to their natural order.
     * 
     * @throws ClassCastException if the elements of this iterable are not comparable.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public default FiniteIterable<E> sorted() {
        return sorted((a, b) -> a == null ? 1 : (b == null ? -1 : ( ((Comparable<? super E>) a).compareTo(b) )));
    }
    
    /* -------------------------------------------------- Reversing -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable in reversed order.
     */
    @Pure
    public default FiniteIterable<E> reversed() {
        return () -> ReversingIterator.with(toArray());
    }
    
    /* -------------------------------------------------- Distinct -------------------------------------------------- */
    
    /**
     * Returns the distinct elements of this iterable.
     */
    @Pure
    public default FiniteIterable<E> distinct() {
        return FiniteIterable.of(toSet());
    }
    
    /* -------------------------------------------------- Cycling -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable repeated indefinitely.
     */
    @Pure
    public default InfiniteIterable<E> repeated() {
        return () -> CyclingIterator.with(this);
    }
    
    /* -------------------------------------------------- Joining -------------------------------------------------- */
    
    /**
     * Returns the elements of this iterable joined by the given delimiter with the given prefix and suffix or the given empty string if this iterable is empty.
     */
    @Pure
    public default String join(CharSequence prefix, CharSequence suffix, CharSequence empty, CharSequence delimiter) {
        if (isEmpty()) {
            return String.valueOf(empty);
        } else {
            final StringBuilder result = new StringBuilder(prefix);
            boolean first = true;
            for (E element : this) {
                if (first) { first = false; }
                else { result.append(delimiter); }
                result.append(String.valueOf(element));
            }
            return result.append(suffix).toString();
        }
    }
    
    /**
     * Returns the elements of this iterable joined by the given delimiter with the given fixes or the given empty string if this iterable is empty.
     */
    @Pure
    public default String join(Fixes fixes, CharSequence empty, CharSequence delimiter) {
        if (fixes == null) { return join("", "", empty, delimiter); }
        else { return join(fixes.getPrefix(), fixes.getSuffix(), empty, delimiter); }
    }
    
    /**
     * Returns the elements of this iterable joined by commas with the given fixes or the given empty string if this iterable is empty.
     */
    @Pure
    public default String join(Fixes fixes, CharSequence empty) {
        return join(fixes, empty, ", ");
    }
    
    /**
     * Returns the elements of this iterable joined by commas with the given fixes.
     */
    @Pure
    public default String join(Fixes fixes) {
        return join(fixes, fixes != null ? fixes.getBoth() : "");
    }
    
    /**
     * Returns the elements of this iterable joined by commas.
     */
    @Pure
    public default String join() {
        return join(null);
    }
    
}