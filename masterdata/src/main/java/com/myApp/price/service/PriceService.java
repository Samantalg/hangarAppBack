package com.myApp.price.service;

import com.myApp.price.dto.ProductExtended;
import com.myApp.price.model.Price;

import java.util.List;

public interface PriceService {

    List<Price> getAllPrices();

    Price createEntryPriceToProduct(long id, double price);

    List<Price> getAllPricesOfProduct(long id);

    Price getCurrentPriceOfProduct(long id);

    ProductExtended getProductExtendedById(long id);

    List<ProductExtended> getProductsExtended();

}
