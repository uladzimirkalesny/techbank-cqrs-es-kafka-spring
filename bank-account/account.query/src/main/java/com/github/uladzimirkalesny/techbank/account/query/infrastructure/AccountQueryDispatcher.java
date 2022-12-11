package com.github.uladzimirkalesny.techbank.account.query.infrastructure;

import com.github.uladzimirkalesny.techbank.cqrs.core.domain.BaseEntity;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.github.uladzimirkalesny.techbank.cqrs.core.queries.BaseQuery;
import com.github.uladzimirkalesny.techbank.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, list -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No query handler was registered");
        }
        if (handlers.size() > 1) {
            throw new RuntimeException("Cannot send query to more than one handler");
        }

        return handlers.get(0).handle(query);
    }
}
