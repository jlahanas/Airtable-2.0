package com.speakupcambridge.util;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class RestEntityListIterable<T extends List<T>> implements Iterable<T> {
  //  private T initial;
  private final Function<T, T> generatorFunction;
  private final Function<T, Boolean> conditionFunction;

  public RestEntityListIterable(
      Function<T, T> generatorFunction, Function<T, Boolean> conditionFunction) {
    //    this.initial = initial;
    this.generatorFunction = generatorFunction;
    this.conditionFunction = conditionFunction;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      //      private T current = initial;

      @Override
      public boolean hasNext() {
        //        return current < limit;
        return false;
      }

      @Override
      public T next() {
        //        return current++;
        return null;
      }
    };
  }
}
