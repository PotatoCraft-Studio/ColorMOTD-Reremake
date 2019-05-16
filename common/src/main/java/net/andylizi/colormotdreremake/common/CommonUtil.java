/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.andylizi.colormotdreremake.common;

import java.util.stream.Collector;

import com.google.common.collect.ImmutableList;

public final class CommonUtil {
    private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST =
            Collector.of(ImmutableList::builder,
                    ImmutableList.Builder::add,
                    (a, b) -> a.addAll(b.build()),
                    ImmutableList.Builder::build);

    @SuppressWarnings("unchecked")
    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return (Collector) TO_IMMUTABLE_LIST;
    }

    private CommonUtil() {}
}
