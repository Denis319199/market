package com.db.endpoint;

import com.db.model.Purchase;
import com.db.service.PurchasesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@Validated
public class PurchasesController {
  private final PurchasesService purchasesService;

  @GetMapping
  List<Purchase> getAllPurchases(int page, int size) {
    return purchasesService.getAllPurchases(page, size);
  }
}
