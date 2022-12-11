package com.github.uladzimirkalesny.techbank.cqrs.core.queries;

import com.github.uladzimirkalesny.techbank.cqrs.core.domain.BaseEntity;

import java.util.List;

@FunctionalInterface
public interface QueryHandlerMethod<T extends BaseQuery> {
    List<BaseEntity> handle(T query);
}
