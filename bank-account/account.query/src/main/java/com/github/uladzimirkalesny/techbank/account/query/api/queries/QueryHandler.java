package com.github.uladzimirkalesny.techbank.account.query.api.queries;

import com.github.uladzimirkalesny.techbank.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {
    List<BaseEntity> handle(FindAllAccountsQuery findAllAccountsQuery);

    List<BaseEntity> handle(FindAccountByIdQuery findAccountByIdQuery);

    List<BaseEntity> handle(FindAccountByAccountHolderQuery findAccountByAccountHolderQuery);

    List<BaseEntity> handle(FindAccountsWithBalanceQuery findAccountsWithBalanceQuery);
}
