package com.portfolioengine.async;

import com.portfolioengine.domain.Price;
import com.portfolioengine.domain.Ticker;


public interface MarketPriceService {
    Price getPrice(Ticker ticker);
}