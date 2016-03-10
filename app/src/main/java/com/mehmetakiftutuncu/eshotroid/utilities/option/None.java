package com.mehmetakiftutuncu.eshotroid.utilities.option;

import java.util.NoSuchElementException;

public final class None<T> extends Option<T> {
    public None() {
        this.isDefined = false;
        this.isEmpty   = true;
        this.value     = null;
    }

    @Override public T get() {
        throw new NoSuchElementException("Called get() method on a None!");
    }
}
