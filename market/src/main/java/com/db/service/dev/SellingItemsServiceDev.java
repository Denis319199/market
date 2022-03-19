package com.db.service.dev;

import com.db.client.AuthClient;
import com.db.exception.SellingItemsServiceException;
import com.db.model.SellingItem;
import com.db.repo.SellingItemsRepo;
import com.db.service.impl.SellingItemsServiceImpl;
import com.db.utility.Utilities;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("auth-service-disabled")
public class SellingItemsServiceDev extends SellingItemsServiceImpl {
  public SellingItemsServiceDev(SellingItemsRepo sellingItemsRepo, AuthClient authClient) {
    super(sellingItemsRepo, authClient);
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public SellingItem insertSellingItem(SellingItem sellingItem)
      throws SellingItemsServiceException {
    try {
      return sellingItemsRepo.save(sellingItem);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public SellingItem updateSellingItem(SellingItem sellingItem)
      throws SellingItemsServiceException {
    SellingItem old = findSellingItemById(sellingItem.getId());
    Utilities.merge(sellingItem, old);

    try {
      return sellingItemsRepo.save(sellingItem);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }
}
