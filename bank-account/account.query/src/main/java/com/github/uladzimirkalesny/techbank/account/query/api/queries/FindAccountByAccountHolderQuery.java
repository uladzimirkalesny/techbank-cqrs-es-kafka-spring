package com.github.uladzimirkalesny.techbank.account.query.api.queries;

import com.github.uladzimirkalesny.techbank.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountByAccountHolderQuery extends BaseQuery {
    private String accountHolder;
}
