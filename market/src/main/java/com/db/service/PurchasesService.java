package com.db.service;

import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import java.util.List;

public interface PurchasesService {
  Purchase insertPurchaseWithoutUsersCheck(Purchase purchase) throws PurchasesServiceException;
}
