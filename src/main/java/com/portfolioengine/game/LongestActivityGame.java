package com.portfolioengine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class LongestActivityGame {



    public int execute(List<Integer> numbers) {


        Set<Integer> setNumbers = new TreeSet<>(numbers);
        List<Integer> res = new ArrayList<>();
        int maxStreak = 0;
        int currentStreak = 0;
        for (Integer number : setNumbers) {
            if (res.isEmpty()) {
                currentStreak++;
                res.add(number);
            } else {
                int lastNumber = res.getLast();
                if (lastNumber != Integer.MAX_VALUE
                    && number == lastNumber + 1) {
                    res.add(number);
                    currentStreak++;
                } else  {
                    res.clear();
                    res.add(number);
                    currentStreak = 1;
                }

            }
            if (currentStreak > maxStreak) {
                    maxStreak = currentStreak;
            }
            
        }
        return maxStreak;

    }
}