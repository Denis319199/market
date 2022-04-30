package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.model.Purchase;
import com.db.repo.PurchasesRepo;
import com.db.service.PurchasesService;
import com.db.utility.Utilities;
import com.db.utility.mapper.ModelMapper;
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

/* Any checks for user's id correctness is unnecessary, since these operations can be made by only plain user
 * authenticated, that is, their id is already known and doesn't require check.
 */
@Service
@RequiredArgsConstructor
public class PurchasesServiceImpl implements PurchasesService {
  protected final PurchasesRepo purchasesRepo;
  protected final AuthClient authClient;
  protected final ModelMapper modelMapper;

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Purchase insertPurchaseWithoutUsersCheck(Purchase purchase) throws PurchasesServiceException {
    try {
      return purchasesRepo.save(purchase);
    } catch (DataAccessException ex) {
      throw new PurchasesServiceException(ex.getMessage());
    }
  }
}
