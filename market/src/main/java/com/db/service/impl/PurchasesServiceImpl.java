package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import com.db.repo.PurchasesRepo;
import com.db.service.PurchasesService;
import com.db.utility.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile({"prod", "!auth-service-disabled"})
public class PurchasesServiceImpl implements PurchasesService {
  protected final PurchasesRepo purchasesRepo;
  protected final AuthClient authClient;

  @Override
  @Transactional(readOnly = true)
  public List<Purchase> getAllPurchases(int page, int size) {
    return purchasesRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public Purchase findPurchaseById(int id) throws PurchasesServiceException {
    Optional<Purchase> purchase = purchasesRepo.findById(id);

    if (purchase.isEmpty()) {
      throw new PurchasesServiceException(PurchasesServiceException.PURCHASE_NOT_FOUND);
    }

    return purchase.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase insertPurchase(Purchase purchase) throws PurchasesServiceException {
     if (!authClient
        .checkUsersExistence(List.of(purchase.getCustomerId(), purchase.getSellerId()), true)
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

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase updatePurchase(Purchase purchase) throws PurchasesServiceException {
    Purchase old = findPurchaseById(purchase.getId());

    List<Integer> usersList = new ArrayList<>();
    if (Objects.nonNull(purchase.getCustomerId())) {
      usersList.add(purchase.getCustomerId());
    }
    if (Objects.nonNull(purchase.getSellerId())) {
      usersList.add(purchase.getSellerId());
    }
    if (!usersList.isEmpty()
        && !authClient.checkUsersExistence(usersList, true).stream().allMatch(val -> val)) {
      throw new PurchasesServiceException(PurchasesServiceException.BAD_SELLER_OR_CUSTOMER_ID);
    }

    Utilities.merge(purchase, old);

    try {
      return purchasesRepo.save(purchase);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deletePurchase(int id) throws PurchasesServiceException {
    try {
      purchasesRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }
}
