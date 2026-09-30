
package com.portfolioengine.domain;


public sealed interface Transaction permits Buy, Sell, Dividend, Fee {

} 
