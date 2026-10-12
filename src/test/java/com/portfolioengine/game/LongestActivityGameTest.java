package com.portfolioengine.game;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class LongestActivityGameTest {
@Test
void shouldHandleEmptyList() {
    LongestActivityGame game  = new LongestActivityGame();
    assertEquals(0, game.execute(List.of()));
}

@Test
void shouldHandleSingleElement() {
    LongestActivityGame game  = new LongestActivityGame();
    assertEquals(1, game.execute(List.of(42)));
}

@Test
void shouldFindLongestStreak() {
    LongestActivityGame game  = new LongestActivityGame();
    assertEquals(4, game.execute(List.of(100, 4, 200, 1, 3, 2)));
}

@Test
void shouldIgnoreDuplicates() {
    LongestActivityGame game  = new LongestActivityGame();
    assertEquals(3, game.execute(List.of(5, 5, 6, 7)));
}

@Test
void shouldHandleIntegerOverflow() {
    LongestActivityGame game  = new LongestActivityGame();
    assertEquals(1, game.execute(
        List.of(Integer.MIN_VALUE, Integer.MAX_VALUE)
    ));
}
}