package com.portfolioengine.async;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.portfolioengine.domain.Buy;
import com.portfolioengine.domain.Money;
import com.portfolioengine.domain.Portfolio;
import com.portfolioengine.domain.Price;
import com.portfolioengine.domain.Quantity;
import com.portfolioengine.domain.Ticker;

class PortfolioValuationServiceTest {

  


    private MarketPriceService slowMarketPriceService() {
    return ticker -> {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                "Price retrieval interrupted", e
            );
        }

        return new Price(
            new BigDecimal("100"),
            Currency.getInstance("EUR")
        );
    };
}

    @Test
    void shouldCalculateTotalPortfolioValuation() throws ExecutionException , TimeoutException, InterruptedException{
        // 1. Créer un Portfolio avec trois positions
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("5"));
        Price price2 = new Price(new BigDecimal("410"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("NVDA");
        Quantity quantity3 = new Quantity(new BigDecimal("8"));
        Price price3 = new Price(new BigDecimal("129"), Currency.getInstance("EUR"));
        Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(buy3);


        // 2. Créer un faux MarketPriceService
        MarketPriceService marketPriceService = ticker -> {
        return switch (ticker.value()) {
                    case "AAPL" -> new Price(
                        new BigDecimal("180"),
                        Currency.getInstance("EUR")
                    );
                    case "MSFT" -> new Price(
                        new BigDecimal("400"),
                        Currency.getInstance("EUR")
                    );
                    case "NVDA" -> new Price(
                        new BigDecimal("120"),
                        Currency.getInstance("EUR")
                    );
                    default -> throw new IllegalStateException(
                        "Unknown ticker: " + ticker.value()
                    );
                };
            };

        Executor executor = Runnable::run;
        // 3. Instancier PortfolioValuationService
        PortfolioValuationService portfolioValuationService = new PortfolioValuationService(marketPriceService, executor);
        // 4. Appeler valuate(portfolio)
        CompletableFuture<Money> valuateFuture = portfolioValuationService.valuate(portfolio);

        Money valuation = valuateFuture.get(2, TimeUnit.SECONDS);
        // 5. Vérifier le total de 4760 EUR
        assertEquals(new Money(new BigDecimal("4760"), Currency.getInstance("EUR")), valuation);
    }


    @Test
    void shouldFailWhenMarketPriceIsUnavailable() {
        // 1. Créer un portefeuille avec AAPL et MSFT
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("5"));
        Price price2 = new Price(new BigDecimal("410"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        // 2. Simuler un MarketPriceService qui échoue pour MSFT
         MarketPriceService marketPriceService = ticker -> {
        return switch (ticker.value()) {
                    case "AAPL" -> new Price(
                        new BigDecimal("180"),
                        Currency.getInstance("EUR")
                    );
                    case "MSFT" -> {throw new IllegalArgumentException();}
                    case "NVDA" -> new Price(
                        new BigDecimal("120"),
                        Currency.getInstance("EUR")
                    );
                    default -> throw new IllegalArgumentException(
                        "Unknown ticker: " + ticker.value()
                    );
                };
            };

        // 3. Instancier PortfolioValuationService
        Executor executor = Runnable::run;
        // 3. Instancier PortfolioValuationService
        PortfolioValuationService portfolioValuationService = new PortfolioValuationService(marketPriceService, executor);
 

        // 4. Appeler valuate(portfolio)
        CompletableFuture<Money> valuateFuture = portfolioValuationService.valuate(portfolio);

        // 5. Vérifier que le futur échoue
        ExecutionException exception = assertThrows(ExecutionException.class, () -> valuateFuture.get(2, TimeUnit.SECONDS));
        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    }

    @Test
    public void shouldValuateSequentially() throws InterruptedException, ExecutionException, TimeoutException {
        // 1. Créer un Portfolio avec trois positions
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("5"));
        Price price2 = new Price(new BigDecimal("410"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("NVDA");
        Quantity quantity3 = new Quantity(new BigDecimal("8"));
        Price price3 = new Price(new BigDecimal("129"), Currency.getInstance("EUR"));
        Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(buy3);

        long start = System.nanoTime();

        Executor executor = Runnable::run;
 
            
        
            // 3. Instancier PortfolioValuationService

            MarketPriceService marketPriceService   = slowMarketPriceService();
            PortfolioValuationService portfolioValuationService = new PortfolioValuationService(marketPriceService, executor);
            // 4. Appeler valuate(portfolio)
            CompletableFuture<Money> valuateFuture = portfolioValuationService.valuate(portfolio);

            Money valuation = valuateFuture.get(2, TimeUnit.SECONDS);

            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(
                System.nanoTime() - start
            );
            
              assertEquals(new Money(new BigDecimal("2300"), Currency.getInstance("EUR")), valuation);


            System.out.println("Duration: " + elapsedMs + " ms");
    } 
     

    @Test
    public void shouldValuateWithVirtualThreads() throws InterruptedException, ExecutionException, TimeoutException {
        // 1. Créer un Portfolio avec trois positions
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("5"));
        Price price2 = new Price(new BigDecimal("410"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("NVDA");
        Quantity quantity3 = new Quantity(new BigDecimal("8"));
        Price price3 = new Price(new BigDecimal("129"), Currency.getInstance("EUR"));
        Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(buy3);

        long start = System.nanoTime();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
        // 3. Instancier PortfolioValuationService

            MarketPriceService marketPriceService   = slowMarketPriceService();
            PortfolioValuationService portfolioValuationService = new PortfolioValuationService(marketPriceService, executor);
            // 4. Appeler valuate(portfolio)
            CompletableFuture<Money> valuateFuture = portfolioValuationService.valuate(portfolio);

            Money valuation = valuateFuture.get(2, TimeUnit.SECONDS);

            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(
                System.nanoTime() - start
            );

            // 5. Vérifier le total de 4760 EUR
            assertEquals(new Money(new BigDecimal("2300"), Currency.getInstance("EUR")), valuation);

            System.out.println("Duration: " + elapsedMs + " ms");
        }
     }

     @Test
void shouldStartAllPriceRequestsConcurrently() throws Exception {
    Portfolio portfolio = new Portfolio();
    Ticker ticker1 = new Ticker("AAPL");
    Quantity quantity1 = new Quantity(new BigDecimal("10"));
    Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
    Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
    portfolio.apply(buy1);

    Ticker ticker2 = new Ticker("MSFT");
    Quantity quantity2 = new Quantity(new BigDecimal("5"));
    Price price2 = new Price(new BigDecimal("410"), Currency.getInstance("EUR"));
    Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
    portfolio.apply(buy2);

    Ticker ticker3 = new Ticker("NVDA");
    Quantity quantity3 = new Quantity(new BigDecimal("8"));
    Price price3 = new Price(new BigDecimal("129"), Currency.getInstance("EUR"));
    Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
    portfolio.apply(buy3);

    CountDownLatch started = new CountDownLatch(3);
    CountDownLatch release = new CountDownLatch(1);

    MarketPriceService marketPriceService = ticker -> {
        started.countDown();

        try {
            release.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }

        return new Price(
            new BigDecimal("100"),
            Currency.getInstance("EUR")
        );
    };

    try (ExecutorService executor =
             Executors.newVirtualThreadPerTaskExecutor()) {

        PortfolioValuationService service =
            new PortfolioValuationService(marketPriceService, executor);


        
        CompletableFuture<Money> valuateFuture = service.valuate(portfolio);

        try {
            boolean allStarted = started.await(2, TimeUnit.SECONDS);

            assertTrue(
                allStarted,
                "Les trois appels getPrice() doivent démarrer avant leur libération"
            );
        } finally {

            release.countDown();
        }


        Money valuation = valuateFuture.get(2, TimeUnit.SECONDS);

        assertEquals(new Money(new BigDecimal("2300"), Currency.getInstance("EUR")), valuation);
    }
    }

    @Test
    public void shouldFailWhenMarketPriceTimesOut() throws Exception {
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("100"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);


        // 2. Créer un MarketPriceService qui attend 2 secondes
        MarketPriceService marketPriceService = ticker -> {


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }

            return new Price(
                new BigDecimal("100"),
                Currency.getInstance("EUR")
            );
        };

        // 3. Utiliser un ExecutorService avec virtual threads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            PortfolioValuationService service =
                new PortfolioValuationService(marketPriceService, executor);


        // 4. Appeler service.valuate(portfolio)
            CompletableFuture<Money>  valueFuture = service.valuate(portfolio).orTimeout(200, TimeUnit.MILLISECONDS);



        // 6. Vérifier que get() lève ExecutionException


            ExecutionException exception = assertThrows(
                ExecutionException.class,
                () -> valueFuture.get(1, TimeUnit.SECONDS)
            );

            assertInstanceOf(TimeoutException.class, exception.getCause());
    }
}
    
}