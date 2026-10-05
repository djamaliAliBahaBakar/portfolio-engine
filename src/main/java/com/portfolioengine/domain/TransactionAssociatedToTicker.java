package  com.portfolioengine.domain;




public sealed interface TransactionAssociatedToTicker extends Transaction  permits  Buy, Sell, Dividend {
    Ticker ticker();
}