package org.lexize.ldocs;

import java.util.Collection;
import java.util.function.Supplier;

public record LListDocs(Collection<?> s, int split, String id, String name) {

}
