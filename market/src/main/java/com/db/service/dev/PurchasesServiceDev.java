package com.db.service.dev;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import com.db.repo.PurchasesRepo;
import com.db.service.impl.PurchasesServiceImpl;
import com.db.utility.Utilities;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("auth-service-disabled")
public class PurchasesServiceDev extends PurchasesServiceImpl {
  public PurchasesServiceDev(PurchasesRepo purchasesRepo, AuthClient authClient) {
    super(purchasesRepo, authClient);
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase insertPurchase(Purchase purchase) throws PurchasesServiceException {
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
    Utilities.merge(purchase, old);

    try {
      return purchasesRepo.save(purchase);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }
}
