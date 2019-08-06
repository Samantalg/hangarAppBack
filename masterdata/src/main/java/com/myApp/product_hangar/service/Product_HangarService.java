package com.myApp.product_hangar.service;

import com.myApp.product.model.Product;
import com.myApp.product_hangar.model.ProductInfo_Hangar;
import com.myApp.product_hangar.model.Product_Hangar;

import java.util.List;

public interface Product_HangarService {

    Product_Hangar associateProductToHangar(Product_Hangar product_hangar);

    List<Product_Hangar> getAll();

    List<Product_Hangar> getProductsOfHangar(long id);

    List<Product_Hangar> getHangarsOfProduct(long id);

    List<ProductInfo_Hangar> getInfoProductsOfHangar(List<Product_Hangar> p_h);

    Product_Hangar updateAmount(long product, long hangar, long amount);

    Product_Hangar unlinkProductOfHangar(long product, long hangar);

}
