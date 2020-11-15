package command;

import model.Account;
import model.Transaction;

import java.util.*;

public class RemoveTransactionsCommand implements Command{

    private List<Transaction> transactionsToRemove;
    private Account account;
    private int length;

    public RemoveTransactionsCommand(List<Transaction> transactionToRemove, Account account) {
        this.transactionsToRemove = transactionToRemove;
        this.account = account;
        this.length = transactionToRemove.size();
    }

    @Override
    public void execute() {
        for(Transaction transaction : transactionsToRemove){
            account.removeTransaction(transaction);
        }
    }

    @Override
    public String getName() {
        return this.length + " transactions removed.";
    }

    @Override
    public void undo() {
        for(Transaction transaction : transactionsToRemove){
            account.addTransaction(transaction);
        }
    }

    @Override
    public void redo() {
        this.execute();
    }
}
