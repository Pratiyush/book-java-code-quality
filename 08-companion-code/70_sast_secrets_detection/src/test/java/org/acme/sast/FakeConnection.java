package org.acme.sast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A minimal in-memory test double for the {@code java.sql} surface the SQL examples use, so the module
 * exercises {@link OrderLookup} and {@link VulnerableOrderLookup} without a JDBC driver or a live
 * database. It records the SQL text each lookup builds and the single bound value, which is what the
 * injection tests assert on: the fix's query text is the constant {@code ?} form with the input bound,
 * while the concatenated form folds the input into the text.
 *
 * <p>The wide JDBC interfaces are backed by {@link Proxy} handlers so only the handful of methods the
 * examples call carry behavior; any other call returns a JDBC-shaped default. This is test scaffolding,
 * not production code, and lives only under {@code src/test}.
 */
final class FakeConnection implements InvocationHandler {

    private final List<String> ids;
    private String lastSql;
    private String lastBoundValue;

    FakeConnection(List<String> ids) {
        this.ids = List.copyOf(ids);
    }

    /** The {@link Connection} view callers pass to the lookups. */
    Connection asConnection() {
        return proxy(Connection.class, this);
    }

    String lastSql() {
        return lastSql;
    }

    String lastBoundValue() {
        return lastBoundValue;
    }

    @Override
    public Object invoke(Object target, Method method, Object[] args) {
        return switch (method.getName()) {
            case "createStatement" -> proxy(Statement.class, new StatementHandler());
            case "prepareStatement" -> {
                lastSql = (String) args[0];
                yield proxy(PreparedStatement.class, new StatementHandler());
            }
            case "close" -> null;
            default -> defaultFor(method.getReturnType());
        };
    }

    /** Backs both {@link Statement} and {@link PreparedStatement}. */
    private final class StatementHandler implements InvocationHandler {
        @Override
        public Object invoke(Object target, Method method, Object[] args) {
            switch (method.getName()) {
                case "setString" -> lastBoundValue = (String) args[1];
                case "executeQuery" -> {
                    if (args != null && args.length == 1) {
                        lastSql = (String) args[0];                 // Statement.executeQuery(sql)
                    }
                    return proxy(ResultSet.class, new ResultSetHandler(ids.iterator()));
                }
                default -> { /* no-op for the rest of the wide interface */ }
            }
            return defaultFor(method.getReturnType());
        }
    }

    private static final class ResultSetHandler implements InvocationHandler {
        private final Iterator<String> remaining;
        private String current;

        ResultSetHandler(Iterator<String> remaining) {
            this.remaining = remaining;
        }

        @Override
        public Object invoke(Object target, Method method, Object[] args) {
            return switch (method.getName()) {
                case "next" -> {
                    boolean has = remaining.hasNext();
                    current = has ? remaining.next() : null;
                    yield has;
                }
                case "getString" -> current;
                default -> defaultFor(method.getReturnType());
            };
        }
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, handler));
    }

    private static Object defaultFor(Class<?> returnType) {
        if (returnType == boolean.class) {
            return Boolean.FALSE;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == List.class) {
            return new ArrayList<>();
        }
        return null;
    }
}
