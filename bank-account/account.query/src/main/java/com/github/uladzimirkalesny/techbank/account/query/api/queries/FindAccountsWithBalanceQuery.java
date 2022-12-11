package com.github.uladzimirkalesny.techbank.account.query.api.queries;

import com.github.uladzimirkalesny.techbank.account.query.api.dto.EqualityType;
import com.github.uladzimirkalesny.techbank.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountsWithBalanceQuery extends BaseQuery {
    private EqualityType equalityType;
    private double balance;
}
