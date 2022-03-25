package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.Developer;
import com.db.model.Purchase;
import com.db.model.SellingItem;
import com.db.repo.SellingItemsRepo;
import com.db.service.PurchasesService;
import com.db.service.SellingItemsService;
import com.db.service.UsersItemsService;
import com.db.utility.SqlQueryBuilder;
import com.db.utility.SqlQueryExecutor;
import com.db.utility.Utilities;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile({"prod", "!auth-service-disabled"})
public class SellingItemsServiceImpl implements SellingItemsService {
  protected final SellingItemsRepo sellingItemsRepo;
  protected final AuthClient authClient;
  protected final UsersItemsService usersItemsService;
  protected final PurchasesService purchasesService;
  protected final SqlQueryExecutor sqlQueryExecutor;

  @Override
  @Transactional(readOnly = true)
  public List<SellingItem> getAllSellingItems(int page, int size) {
    return sellingItemsRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SellingItem> getAllSellingItemsWithFilters(
      int page,
      int size,
      boolean isOwn,
      int userId,
      Integer game,
      String orderBy,
      boolean ascOrder) {
    SqlQueryBuilder queryBuilder =
        SqlQueryBuilder.builder().select("s.*").from("selling_items").as("s");

    if (Objects.nonNull(game)) {
      queryBuilder.innerJoin("items").as("i").on("s.item_id = i.id");
    }

    String comparingSign;
    if (isOwn) {
      comparingSign = "=";
    } else {
      comparingSign = "!=";
    }
    queryBuilder.where("s.seller_id %s %o", comparingSign, userId);

    if (Objects.nonNull(game)) {
      queryBuilder.and("i.game_id = %o", game);
    }

    String query = queryBuilder.orderBy(ascOrder, orderBy).limit(size).offset(page * size).build();

    return sqlQueryExecutor.execute(query, SellingItem.class);
  }

  @Override
  @Transactional(readOnly = true)
  public SellingItem findSellingItemById(int id) throws SellingItemsServiceException {
    Optional<SellingItem> sellingItem = sellingItemsRepo.findById(id);

    if (sellingItem.isEmpty()) {
      throw new SellingItemsServiceException(SellingItemsServiceException.SELLING_ITEM_NOT_FOUND);
    }

    return sellingItem.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public SellingItem insertSellingItem(SellingItem sellingItem)
      throws SellingItemsServiceException {
    if (!authClient.checkUsersExistence(List.of(sellingItem.getSellerId()), true).get(0)) {
      throw new SellingItemsServiceException(SellingItemsServiceException.BAD_SELLER_ID);
    }

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

    if (Objects.nonNull(sellingItem.getSellerId())) {
      if (!authClient.checkUsersExistence(List.of(sellingItem.getSellerId()), true).get(0)) {
        throw new SellingItemsServiceException(SellingItemsServiceException.BAD_SELLER_ID);
      }
    }

    Utilities.merge(sellingItem, old);

    try {
      return sellingItemsRepo.save(sellingItem);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteSellingItem(int id) throws SellingItemsServiceException {
    try {
      sellingItemsRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void sellItem(SellingItem sellingItem)
      throws SellingItemsServiceException, ServiceException {
    try {
      usersItemsService.takeItemFromUser(sellingItem.getSellerId(), sellingItem.getItemId());

      insertSellingItem(sellingItem);
    } catch (UsersItemsServiceException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void removeItemFromSale(int id, int sellerId)
      throws SellingItemsServiceException, ServiceException {
    SellingItem sellingItem = findSellingItemById(id);

    if (sellingItem.getSellerId() != sellerId) {
      throw new SellingItemsServiceException(SellingItemsServiceException.USER_IS_NOT_AN_OWNER);
    }

    usersItemsService.addItemToUser(sellerId, sellingItem.getItemId());
    deleteSellingItem(id);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void purchaseItem(int id, int customerId)
      throws SellingItemsServiceException, ServiceException {
    SellingItem sellingItem = findSellingItemById(id);

    if (sellingItem.getSellerId() == customerId) {
      throw new SellingItemsServiceException(
          SellingItemsServiceException.USER_CANT_BUY_THEIR_OWN_ITEM);
    }

    usersItemsService.addItemToUser(customerId, sellingItem.getItemId());
    try {
      purchasesService.insertPurchase(
          Purchase.builder()
              .sellerId(sellingItem.getSellerId())
              .customerId(customerId)
              .itemId(sellingItem.getItemId())
              .purchaseDate(LocalDate.now())
              .price(sellingItem.getPrice())
              .build());
    } catch (PurchasesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    deleteSellingItem(id);
  }
}
