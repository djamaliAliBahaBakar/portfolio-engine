package com.portfolioengine.async;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.portfolioengine.domain.Money;
import com.portfolioengine.domain.Portfolio;
import com.portfolioengine.domain.Position;
import com.portfolioengine.domain.Price;
import com.portfolioengine.domain.Quantity;


public class PortfolioValuationService {
    private final MarketPriceService marketPriceService;
    private final Executor executor;

    public PortfolioValuationService(MarketPriceService marketPriceService, Executor executor) {
        this.marketPriceService = marketPriceService;
        this.executor = executor;
    }

    private List<CompletableFuture<Money>> valuatePositionsAsync(
        Portfolio portfolio) {

        return portfolio.positions()
            .stream()
            .map(position -> valuatePositionAsync(position))
            .toList();
    }




    private Money valuatePosition(Position position) {
        Price price = this.marketPriceService.getPrice(position.ticker());
        Money valuation = price.multiply(new Quantity(position.quantity().quantity()));
        return valuation;
    }

    private CompletableFuture<Money> valuatePositionAsync(Position position) {
        
        CompletableFuture<Money> valuationFuture = CompletableFuture.supplyAsync(
            () -> valuatePosition(position),
            executor
        );
        return  valuationFuture;
    }






    // Orchestre la valorisation du portefeuille
    public CompletableFuture<Money> valuate(Portfolio portfolio) {

        // 1. Lancer les calculs
        List<CompletableFuture<Money>> futures =
            valuatePositionsAsync(portfolio);

        // 2. Attendre leur achèvement collectif
        CompletableFuture<Void> allDone =
            CompletableFuture.allOf(
                futures.toArray(new CompletableFuture<?>[0])
            );

        // 3. Rassembler les résultats
        CompletableFuture<List<Money>> valuationsFuture =
            allDone.thenApply(ignored ->
                futures.stream()
                    .map(CompletableFuture::join)
                    .toList()
            );

        // 4. TODO : additionner les Money et retourner
        //    un CompletableFuture<Money>
        return valuationsFuture.thenApply(valuations ->
            valuations.stream()
                .reduce(
                    new Money(BigDecimal.ZERO, Currency.getInstance("EUR")),
                    Money::add
                )
        );

    }


}