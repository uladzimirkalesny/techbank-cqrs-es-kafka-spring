package com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure;

import com.github.uladzimirkalesny.techbank.cqrs.core.domain.BaseEntity;
import com.github.uladzimirkalesny.techbank.cqrs.core.queries.BaseQuery;
import com.github.uladzimirkalesny.techbank.cqrs.core.queries.QueryHandlerMethod;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);

    <U extends BaseEntity> List<U> send(BaseQuery query);
}
