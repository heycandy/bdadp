package com.chinasofti.ark.bdadp.util.process;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A circular buffer of items of a given length. It will grow up to the give size as items are
 * appended, then it will begin to overwrite older items.
 *
 * @param <T> The type of the item contained.
 */
public class LogBuffer<T> implements Iterable<T> {

    private final List<T> lines;
    private final int size;
    private int start;

    public LogBuffer(int size) {
        this.lines = new ArrayList<T>();
        this.size = size;
        this.start = 0;
    }

    public void append(T line) {
        append(line, true);
    }

    public void append(T line, boolean isCircular) {
        if (lines.size() < size) {
            lines.add(line);
        } else {
            lines.set(size - 1, line);
            if (isCircular) {
                lines.set(start, line);
                start = (start + 1) % size;
            } else {
                lines.set(size - 1, line);
            }
        }
    }

    @Override
    public String toString() {
        return "[" + Joiner.on(", ").join(lines) + "]";
    }

    public Iterator<T> iterator() {
        if (start == 0) {
            return lines.iterator();
        } else {
            return Iterators.concat(lines.subList(start, lines.size()).iterator(),
                    lines.subList(0, start).iterator());
        }
    }

    public int getMaxSize() {
        return this.size;
    }

    public int getSize() {
        return this.lines.size();
    }

}
