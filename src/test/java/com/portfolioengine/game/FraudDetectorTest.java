package com.portfolioengine.game;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FraudDetectorTest {
    @Test
    public void shoudConsiderThreeTransactionLessTanSixtySecond() {

        List<Transact> transactions = List.of(
            new Transact("A", 10, 100),
            new Transact("A", 30, 200),
            new Transact("A", 60, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
        assertEquals(List.of("A"), listAccounts);

    }

    @Test
    public void shoudConsiderOnlyTwoTransaction() {
        List<Transact> transactions = List.of(
            new Transact("A", 10, 100),
            new Transact("A", 30, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
        assertTrue(listAccounts.isEmpty());
    }

    @Test
    public void shoudConsiderWindowEqualsToSixtySecond() {
        List<Transact> transactions = List.of(
            new Transact("A", 10, 100),
            new Transact("A", 40, 200),
            new Transact("A", 70, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
         assertEquals(List.of("A"), listAccounts);
    }

    @Test
    public void shoudConsiderWindowMoreThanToSixtySecond() {
        List<Transact> transactions = List.of(
            new Transact("A", 10, 100),
            new Transact("A", 40, 200),
            new Transact("A", 71, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
         assertTrue(listAccounts.isEmpty());
    }

    @Test
    public void shoudConsiderTransactionsNotSorted() {
        List<Transact> transactions = List.of(
            new Transact("A", 65, 100),
            new Transact("A", 10, 200),
            new Transact("A", 30, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
         assertEquals(List.of("A"), listAccounts);
    }

    @Test
    public void shoudConsiderMutlipleAccounts() {
        List<Transact> transactions = List.of(
            new Transact("B", 1, 100),
            new Transact("B", 2, 200),
            new Transact("B", 3, 200),
            new Transact("A", 10, 100),
            new Transact("A", 20, 200),
            new Transact("A", 30, 200)
        );
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
         assertEquals(List.of("A", "B"), listAccounts);
    }

        @Test
    public void shoudConsiderEmptyTransaction() {
        List<Transact> transactions =new ArrayList<>();
        List<String> listAccounts = FraudDetector.findSuspiciousAccounts(transactions);
        assertTrue(listAccounts.isEmpty());
    }

    @Test
void shouldReturnAccountsSortedAlphabetically() {
    List<Transact> transactions = List.of(
        new Transact("Z", 1, 100),
        new Transact("Z", 2, 100),
        new Transact("Z", 3, 100),
        new Transact("A", 1, 100),
        new Transact("A", 2, 100),
        new Transact("A", 3, 100),
        new Transact("M", 1, 100),
        new Transact("M", 2, 100),
        new Transact("M", 3, 100)
    );

    assertEquals(
        List.of("A", "M", "Z"),
        FraudDetector.findSuspiciousAccounts(transactions)
    );
}

}