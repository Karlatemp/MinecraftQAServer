package io.github.karlatemp.mqaserver.utils;

public interface ResultCallback<Type> {
    void done(Type result, Throwable throwable);

}
