
package com.portfolioengine.domain;


public sealed interface Transaction permits TransactionAssociatedToTicker, Fee {

} 
