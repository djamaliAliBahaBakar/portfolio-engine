
package com.portfolioengine.async;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.portfolioengine.domain.Price;
import com.portfolioengine.domain.Ticker;

public class PriceCache {

    private record CachedPrice(
            Price price,
            Instant expiresAt
    ) {
        boolean isValid(Instant now) {
            return now.isBefore(expiresAt);
        }
    }

    private final ConcurrentHashMap<Ticker, CachedPrice> cache =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Ticker, CompletableFuture<Price>> inFlight =
            new ConcurrentHashMap<>();

    private final MarketPriceService priceService;
    private final Duration ttl;
    private final Clock clock;

    public PriceCache(
            MarketPriceService priceService,
            Duration ttl,
            Clock clock
    ) {
        this.priceService = Objects.requireNonNull(priceService);
        this.ttl = Objects.requireNonNull(ttl);
        this.clock = Objects.requireNonNull(clock);

        if (ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("TTL must be positive");
        }
    }

    public Price getPrice(Ticker ticker) {
        Objects.requireNonNull(ticker);

        CachedPrice cached = cache.get(ticker);

        if (cached != null && cached.isValid(clock.instant())) {
            return cached.price();
        }

        CompletableFuture<Price> newFuture = new CompletableFuture<>();

        CompletableFuture<Price> existing =
                inFlight.putIfAbsent(ticker, newFuture);

        if (existing != null) {
            return existing.join();
        }

        try {
            // Relecture après acquisition du rôle de chargeur.
            cached = cache.get(ticker);

            if (cached != null && cached.isValid(clock.instant())) {
                newFuture.complete(cached.price());
                return cached.price();
            }

            // Appel réseau HORS de computeIfAbsent / compute.
            Price price = Objects.requireNonNull(
                    priceService.getPrice(ticker),
                    "Price provider returned null"
            );

            cache.put(
                    ticker,
                    new CachedPrice(
                            price,
                            clock.instant().plus(ttl)
                    )
            );

            newFuture.complete(price);
            return price;

        } catch (Throwable error) {
            newFuture.completeExceptionally(error);
            throw error;

        } finally {
            inFlight.remove(ticker, newFuture);
        }
    }

    public void invalidate(Ticker ticker) {
        cache.remove(ticker);
    }
}