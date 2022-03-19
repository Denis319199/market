package com.db.service;

import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import java.util.List;

public interface PurchasesService {
    List<Purchase> getAllPurchases(int page, int size);

    Purchase findPurchaseById(int id) throws PurchasesServiceException;

    Purchase insertPurchase(Purchase purchase) throws PurchasesServiceException;

    Purchase updatePurchase(Purchase purchase) throws PurchasesServiceException;

    void deletePurchase(int id) throws PurchasesServiceException;
}
