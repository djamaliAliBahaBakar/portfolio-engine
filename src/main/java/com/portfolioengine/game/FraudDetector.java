package com.portfolioengine.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

record Transact(
    String accountId,
    long timestamp,
    long amount
) {}

public class FraudDetector {

    public static List<String> findSuspiciousAccounts(List<Transact> transactions) {
            Map<String, List<Long>> transactionByAccount = new HashMap<>();
            List<String> accountsFraud = new ArrayList<>();
            // Regrouper par accountId
            for (Transact transaction : transactions) {

                List<Long> timestampsList = transactionByAccount.computeIfAbsent(transaction.accountId(), key -> new ArrayList<>());
                timestampsList.add(transaction.timestamp());
            }

            // Trier les timestamp
            for (String account: transactionByAccount.keySet()) {
                List<Long> listTimestamps = transactionByAccount.get(account);
                Collections.sort(listTimestamps);
                int max = listTimestamps.size();
               for (int idx = 0; idx + 2 < max; idx++) {
                    if (listTimestamps.get(idx + 2)
                            - listTimestamps.get(idx) <= 60) {
                        accountsFraud.add(account);
                        break;
                    }
                }

            }
        Collections.sort(accountsFraud);
        return accountsFraud;
            
    }

        
}
