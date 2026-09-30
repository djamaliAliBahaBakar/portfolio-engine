package  com.portfolioengine.domain;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


public class MoneyTest {

    @Test
    void shouldRejectNullAmount(){
        assertThrows(NullPointerException.class, () -> new Money(null,  Currency.getInstance("EUR")));
    }

    @Test
    void shouldRejectNullCurrency() {
        assertThrows(NullPointerException.class, ()->new Money(new BigDecimal(4), null));
    }

    @Test 
    void shouldConsiderEquivalentAmountsEqual() {
        Money money1 = new Money(new BigDecimal("5.00"), Currency.getInstance("EUR"));
        Money money2 = new Money(new BigDecimal("5"), Currency.getInstance("EUR"));
        assertEquals(money1, money2 );

    }

    @Test
    void shouldAcceptNegativeAmount() {
        assertDoesNotThrow(()-> new Money(BigDecimal.valueOf(-5), Currency.getInstance("EUR")));
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money money1 = new Money(BigDecimal.valueOf(-5), Currency.getInstance("EUR"));
        Money money2 = new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"));
        assertEquals(new Money(BigDecimal.valueOf(7), Currency.getInstance("EUR")), money1.add(money2));
    }

    @Test
    void shouldAdditionRejectNullMoney() {
        Money money = new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"));
        assertThrows(NullPointerException.class, () -> money.add(null));
    }

    @Test
    void shouldRejectAdditionWithDifferentCurrency() {
        Money money1 = new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"));
        Money money2 = new Money(BigDecimal.valueOf(10), Currency.getInstance("USD"));
        assertThrows(IllegalArgumentException.class, () -> money1.add(money2));
    }

    @Test
    void shouldConsidereNegativeAmount() {
        Money money = new Money(new BigDecimal("3.00"), Currency.getInstance("EUR"));
        assertEquals(new Money(new BigDecimal("-3.00"), Currency.getInstance("EUR")), money.negate());
    }
}

