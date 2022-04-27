package com.db.endpoint;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/queries")
@Validated
public class QueriesController {

  enum Queries {}

  private static final String[] queries;

  static {
    queries = new String[8];
    queries[0] =
        "SELECT c.id, c.name, COALESCE(tmp.num, 0) AS number_of_items\n"
            + "FROM countries AS c LEFT JOIN (\n"
            + "\tSELECT COUNT(*) AS num, u.country_id\n"
            + "\tFROM selling_items AS s INNER JOIN users AS u ON s.seller_id = u.id\n"
            + "\tGROUP BY u.country_id\n"
            + ") AS tmp\n"
            + "ON tmp.country_id = c.id;";

    queries[1] =
        "SELECT u.id, u.username, u_tmp.num + s_tmp.num AS quantity\n"
            + "FROM (\n"
            + "\tSELECT COUNT(*) AS num, seller_id AS id\n"
            + "\tFROM selling_items\n"
            + "\tGROUP BY seller_id) AS s_tmp\n"
            + "INNER JOIN (\n"
            + "\tSELECT SUM(quantity) AS num, user_id AS id\n"
            + "\tFROM users_items\n"
            + "\tGROUP BY user_id\n"
            + ") AS u_tmp\n"
            + "ON u_tmp.id = s_tmp.id\n"
            + "INNER JOIN users AS u ON u_tmp.id = u.id;";

    queries[2] =
        "SELECT tmp.id, tmp.username, tmp.num\n"
            + "FROM (\n"
            + "\tSELECT tmp.id, tmp.username, tmp.num, MIN(tmp.username) OVER (PARTITION BY tmp.country_id) AS min_username\n"
            + "\tFROM (\n"
            + "\t\tSELECT u.id, u.username, u_tmp.num, u.country_id, MAX(u_tmp.num) OVER (PARTITION BY c.id) AS max_num\n"
            + "\t\tFROM (\n"
            + "\t\t\tSELECT SUM(quantity) AS num, user_id AS id\n"
            + "\t\t\tFROM users_items\n"
            + "\t\t\tGROUP BY user_id\n"
            + "\t\t) AS u_tmp\n"
            + "\t\tINNER JOIN users AS u ON u_tmp.id = u.id\n"
            + "\t\tINNER JOIN countries AS c ON c.id = u.country_id\n"
            + "\t) AS tmp\n"
            + "\tWHERE tmp.max_num = tmp.num\n"
            + ") AS tmp\n"
            + "WHERE tmp.username = tmp.min_username;";

    queries[3] =
        "SELECT u.id, u.username, tmp.num\n"
            + "FROM (\n"
            + "\tSELECT COUNT(*) AS num, customer_id AS id \n"
            + "\tFROM purchases\n"
            + "\tGROUP BY customer_id\n"
            + ") AS tmp\n"
            + "INNER JOIN users AS u ON tmp.id = u.id;";

    queries[4] =
        "SELECT SUM(price) AS price\n"
            + "FROM purchases\n"
            + "WHERE NOW() - INTERVAL '1 hour' <= purchase_date;";

    queries[5] =
        "SELECT g.id, g.name, tmp.price\n"
            + "FROM (\n"
            + "\tSELECT SUM(price) AS price, i.game_id\n"
            + "\tFROM selling_items AS s INNER JOIN items AS i ON s.item_id = i.id\n"
            + "\tGROUP BY i.game_id\n"
            + ") AS tmp\n"
            + "INNER JOIN games AS g ON g.id = tmp.game_id;";

    queries[6] =
        "SELECT AVG(p.price) AS price\n"
            + "FROM purchases AS p INNER JOIN items AS i ON p.item_id = i.id\n"
            + "WHERE NOW() - INTERVAL '7 days' <= purchase_date AND i.game_id = %o;";

    queries[7] =
        "SELECT SUM(tmp.price)\n"
            + "FROM (\n"
            + "\tSELECT u.quantity * tmp.price AS price\n"
            + "\tFROM (\n"
            + "\t\tSELECT AVG(price) AS price, item_id\n"
            + "\t\tFROM selling_items\n"
            + "\t\tWHERE item_id = ANY (SELECT item_id \n"
            + "\t\t\t\t\t\t\t FROM users_items \n"
            + "\t\t\t\t\t\t\t WHERE user_id = 1)\n"
            + "\t\tGROUP BY item_id\n"
            + "\t) AS tmp\n"
            + "\tINNER JOIN users_items AS u ON tmp.item_id = u.item_id\n"
            + ") AS tmp;";
  }
    }
