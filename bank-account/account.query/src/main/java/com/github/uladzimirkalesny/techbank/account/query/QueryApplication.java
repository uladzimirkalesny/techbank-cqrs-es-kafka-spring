package com.github.uladzimirkalesny.techbank.account.query;

import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountByAccountHolderQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountsWithBalanceQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.QueryHandler;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor

@SpringBootApplication
public class QueryApplication {

	private final QueryDispatcher queryDispatcher;

	private final QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByAccountHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountsWithBalanceQuery.class, queryHandler::handle);
	}

}
