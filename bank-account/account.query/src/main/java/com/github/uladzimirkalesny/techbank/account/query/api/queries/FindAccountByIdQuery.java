package com.github.uladzimirkalesny.techbank.account.query.api.queries;

import com.github.uladzimirkalesny.techbank.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountByIdQuery extends BaseQuery {
    private String id;
}
