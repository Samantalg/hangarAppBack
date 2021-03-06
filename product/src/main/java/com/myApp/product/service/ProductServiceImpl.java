package com.myApp.product.service;

import com.myApp.exception.ApplicationException;
import com.myApp.exception.ApplicationExceptionCause;
import com.myApp.product.dao.ProductDao;
import com.myApp.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
    private ProductDao productDAO;

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = productDAO.getAllProducts();
		if (!products.isEmpty())
			return products;
		throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

	@Override
	public List<Product> getAllActiveProducts() {
		List<Product> products = productDAO.getProductsActive();
		if (!products.isEmpty())
			return products;
		throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

    @Override
    public List<Product> getAllProductsWithNameLike(String name) {
        List<Product> result = productDAO.getProductsByName(name);
        if (!result.isEmpty())
            return result;
        throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
    }

	@Override
	public Page<Product> findByStateTrue(Pageable pageable) {
		Page<Product> items = productDAO.findByStateTrue(pageable);
		if (!items.isEmpty())
			return items;
		throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

	@Override
	public Product getProduct(long id) {
		if (productDAO.isProductById(id))
			return productDAO.getProduct(id);
		throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

    @Override
    public Product create(Product product) {
	    if (!productDAO.isProductByNameAndDescription(product.getName(), product.getDescription()))
	        return productDAO.createProduct(product);
	    throw new ApplicationException(ApplicationExceptionCause.PRODUCT_CONFLICT);
    }

	public void deleteProduct(long id) {
		if (productDAO.isProductById(id))
			productDAO.deleteProduct(id);
		throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

    @Override
	public Product updateState(long id) {
		if (productDAO.isProductById(id)) {
			Product product = productDAO.getProduct(id);
			product.setState(!product.isState());
			return productDAO.updateProduct(product);
		} throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
	}

    @Override
    public Product modifyProduct(Product update) {
		if (productDAO.isProductById(update.getId())) {
			Product product = this.manageUpdate(update);
			return productDAO.editProduct(product);
		} throw new ApplicationException(ApplicationExceptionCause.PROD_NOT_FOUND);
    }

	@Override
	public boolean isProductById(long id) {
		return productDAO.isProductById(id);
	}

    @Override
    public String getNameOfProductById(long id) {
        return productDAO.getNameOfProductById(id);
    }

    @Override
    public boolean isProductByName(String name) {
        return productDAO.isProductByName(name);
    }

    private Product manageUpdate(Product update) {
    	Product product = productDAO.getProduct(update.getId());
		product.setName(update.getName());
		product.setDescription(update.getDescription());
		return product;
	}

	/*Ejercicio java 8
	TODO actualizar al nuevo modelo producto -> sin hangar asignado

	private List<Product> searchByFirstLetter(char letter) {

		List<Product> products = productDAO.getAllProducts();
		return products.stream()
				.filter((product) -> letter == product.getName().charAt(0))
				.collect(Collectors.toList());
	}

	private List<Product> convertToUpperCase(List<Product> products) {

		return products.stream()
				.map(p -> new Product(p.getName().toUpperCase(), p.getHangar()))
				.collect(Collectors.toList());
	}

	private Product filterProductByLength(List<Product> products) {
		return products.stream().max(Comparator.comparing(Product::getName)).get();
	}

	public Product filterName(char letter) {
		List<Product> listLetter = searchByFirstLetter(letter);

		List<Product> listUpper = convertToUpperCase(listLetter);
		return filterProductByLength(listUpper);
	}
	*/
}
