package com.github.uladzimirkalesny.techbank.account.query.api.dto;

import com.github.uladzimirkalesny.techbank.account.common.dto.BaseResponse;
import com.github.uladzimirkalesny.techbank.account.query.domain.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountLookupResponse extends BaseResponse {
    private List<BankAccount> accounts;

    public AccountLookupResponse(String message) {
        super(message);
    }
}
