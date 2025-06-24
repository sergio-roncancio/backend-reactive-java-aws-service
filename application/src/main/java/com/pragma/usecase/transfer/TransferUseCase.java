package com.pragma.usecase.transfer;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.port.out.AccountPort;
import com.pragma.domain.commons.transactions.Transaction;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.port.out.TransferPort;
import com.pragma.domain.transfer.request.RequestTransfer;
import com.pragma.domain.transfer.response.Source;
import com.pragma.domain.transfer.response.Destination;
import com.pragma.domain.transfer.response.TransferResponse;
import com.pragma.usecase.account.ValidationAccount;
import com.pragma.usecase.transfer.factory.TransferFactory;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class TransferUseCase {

    private final TransferPort transferPort;
    private final AccountPort accountPort;
    private final Transaction transaction;
    private final TransferFactory transferFactory;
    private final ValidationAccount validationAccount;

    public Mono<TransferResponse> makeTransfer(RequestTransfer requestTransfer) {
        return validationAccount.validNumberAccountsAreEquals(
                        requestTransfer.numberAccountFrom(), requestTransfer.numberAccountTo())
                .then(accountPort.findAccountsByNumbers(
                                List.of(requestTransfer.numberAccountFrom(), requestTransfer.numberAccountTo()))
                        .flatMap(accounts -> {
                            var accountFrom = accounts.get(requestTransfer.numberAccountFrom());
                            var accountTo = accounts.get(requestTransfer.numberAccountTo());
                            var transfer = transferFactory.createTransfer(accountFrom, accountTo, requestTransfer);
                            return transactionTransfer(accountFrom, accountTo, transfer)
                                    .map(idTransfer -> createTransferResponse(idTransfer, accountFrom,
                                            accountTo.getNumber(), requestTransfer.amount()));
                        }));
    }

    private TransferResponse createTransferResponse(int idTransfer, Account accountFrom,
                                                    String numberAccountTo, BigDecimal amountTransfer) {
        var source = new Source(accountFrom.getNumber(), accountFrom.getBalance());
        var destination = new Destination(numberAccountTo, amountTransfer);
        return new TransferResponse(idTransfer, source, destination);
    }

    private Mono<Integer> transactionTransfer(Account accountFrom, Account accountTo, Transfer transfer) {
        return transaction.createTransaction(accountPort.updateAccountsBalance(accountFrom, accountTo)
                .then(transferPort.makeTransfer(transfer)));
    }

}
