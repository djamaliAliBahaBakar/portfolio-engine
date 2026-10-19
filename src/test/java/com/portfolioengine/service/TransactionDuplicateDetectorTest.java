package com.portfolioengine.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class TransactionDuplicateDetectorTest {
    @Test
    public void shouldReturnEachDuplicateOnlyOnce() {
        List<String> transactionIds = List.of("TX-101", "TX-101", "TX-101", "TX-102", "TX-102");
        List<String> dupplicates = TransactionDuplicateDetector.findDuplicates(transactionIds);
        assertEquals(List.of("TX-101","TX-102"), dupplicates);
    }

    @Test
    public void shouldReturnEmptyListWhenNoDuplicates() {
        List<String> transactionIds = List.of("TX-101",  "TX-102", "TX-103");
        List<String> dupplicates = TransactionDuplicateDetector.findDuplicates(transactionIds);
        assertEquals(List.of(), dupplicates);
    }


    @Test
    public void shouldReturnEmptyListForEmptyInput() {
        List<String> transactionIds = List.of();
        List<String> dupplicates = TransactionDuplicateDetector.findDuplicates(transactionIds);
        assertEquals(List.of(), dupplicates);
    }

    @Test
    public void shouldPreserveDuplicateDetectionOrder() {
        List<String> transactionIds = List.of("B", "A", "A", "B");
        List<String> dupplicates = TransactionDuplicateDetector.findDuplicates(transactionIds);
        assertEquals(List.of("A","B"), dupplicates);
    }

    @Test
    void shouldNotModifyInputList() {
        List<String> transactionIds =
                new ArrayList<>(List.of("A", "B", "A"));

        List<String> original = List.copyOf(transactionIds);

        TransactionDuplicateDetector.findDuplicates(transactionIds);

        assertEquals(original, transactionIds);
    }
}