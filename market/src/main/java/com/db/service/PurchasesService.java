package com.db.service;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import com.db.repo.PurchasesRepo;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchasesService {
  private final PurchasesRepo purchasesRepo;
  private final AuthClient authClient;

  @Transactional(readOnly = true)
  public List<Purchase> getAllPurchases(int page, int size) {
    return purchasesRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(readOnly = true)
  public Purchase findPurchaseById(int id) throws PurchasesServiceException {
    Optional<Purchase> purchase = purchasesRepo.findById(id);

    if (purchase.isEmpty()) {
      throw new PurchasesServiceException(PurchasesServiceException.PURCHASE_NOT_FOUND);
    }

    return purchase.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase insertPurchase(Purchase purchase) throws PurchasesServiceException {
    if (purchasesRepo.existsById(purchase.getId())) {
      throw new PurchasesServiceException(PurchasesServiceException.PURCHASE_ALREADY_EXISTS);
    }

    if (!authClient
        .checkExistence(List.of(purchase.getCustomerId(), purchase.getSellerId()), true)
        .stream()
        .allMatch(val -> val)) {
      throw new PurchasesServiceException(PurchasesServiceException.BAD_SELLER_OR_CUSTOMER_ID);
    }

    try {
      return purchasesRepo.save(purchase);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase updatePurchase(Purchase purchase) throws PurchasesServiceException {
    Purchase old = findPurchaseById(purchase.getId());
    Utilities.merge(purchase, old);

    try {
      return purchasesRepo.save(purchase);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deletePurchase(int id) throws PurchasesServiceException {
    try {
      purchasesRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }
}
