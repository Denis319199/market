-- 1
SELECT o.id, o.begin_date, c.name, c.address
FROM orders AS o
         INNER JOIN customers AS c ON c.id = o.customer;

-- 2
SELECT c.num,
       c.begin_date,
       e.id,
       CONCAT(e.first_name, ' ', e.last_name, ' ', e.patronymic) AS name,
       e.address,
       r.phone_number
FROM contracts AS c
         INNER JOIN employees AS e ON e.id = c.manager_id
         INNER JOIN rooms AS r ON r.num = e.room;

-- 3
SELECT e.id,
       CONCAT(e.first_name, ' ', e.last_name, ' ', e.patronymic) AS name,
       c.num,
       c.begin_date,
       c.book_id
FROM employees AS e
         LEFT JOIN contracts AS c ON e.id = c.manager_id;

-- 4
SELECT CONCAT(e.first_name, ' ', e.last_name, ' ', e.patronymic) AS name,
       p.name                                                    AS position,
       p.wage
FROM employees AS e
         INNER JOIN positions AS p ON p.id = e.position;

-- 5
SELECT CONCAT(a.first_name, ' ', a.last_name, ' ', a.patronymic) AS name,
       a.address,
       a.phone_number,
       ba.author_pos,
       ba.fee * 100 / MAX(ba.fee) OVER (PARTITION BY ba.book_id) AS percent
FROM books AS b
         INNER JOIN books_authors AS ba ON b.id = ba.book_id
         INNER JOIN authors AS a ON a.id = ba.author_id;

-- 6
SELECT e.id,
       CONCAT(e.first_name, ' ', e.last_name, ' ', e.patronymic) AS name,
       c.num,
       c.begin_date,
       b.name
FROM books AS b
         LEFT JOIN books_editors AS be ON b.id = be.book_id
         INNER JOIN contracts AS c ON b.id = c.book_id
         RIGHT JOIN employees e ON e.id = b.main_editor OR e.id = be.editor_id;

-- 7
SELECT b.name                                                    AS book_name,
       b.amount,
       b.cost,
       CONCAT(a.first_name, ' ', a.last_name, ' ', a.patronymic) AS author_name,
       a.address,
       a.phone_number
FROM books AS b
         INNER JOIN books_authors AS ba ON b.id = ba.book_id
         INNER JOIN authors a ON a.id = ba.author_id;

-- 8
SELECT CONCAT(e.first_name, ' ', e.last_name, ' ', e.patronymic) AS name,
       p.name                                                    AS position
FROM employees AS e
         INNER JOIN positions AS p ON e.position = p.id
WHERE e.id != ALL (
    SELECT main_editor
    FROM books
);
