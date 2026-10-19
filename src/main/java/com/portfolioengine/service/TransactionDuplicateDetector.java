package com.portfolioengine.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TransactionDuplicateDetector {

    public static List<String> findDuplicates(List<String> transactionIds) {

        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new LinkedHashSet<>();

        for (String transactionId : transactionIds) {
            // À toi de compléter
            boolean isFirstOccurrence = seen.add(transactionId);
            if (!isFirstOccurrence) {
                duplicates.add(transactionId);
            }

        }
        return List.copyOf(duplicates);
        // À toi de retourner le résultat
    }


}