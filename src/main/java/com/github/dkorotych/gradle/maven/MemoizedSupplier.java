/**
 * Copyright 2022 Dmitry Korotych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dkorotych.gradle.maven;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public abstract class MemoizedSupplier<T> implements Supplier<T> {
    private final AtomicReference<T> cached = new AtomicReference<>();

    public static <O> MemoizedSupplier<O> create(final Supplier<O> creator) {
        return new MemoizedSupplier<O>() {
            @Override
            protected O create() {
                return creator.get();
            }
        };
    }

    public T get() {
        T value = cached.get();
        if (value == null) {
            value = create();
            if (value != null) {
                cached.set(value);
            }
        }
        return value;
    }

    protected abstract T create();
}
