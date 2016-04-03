INSERT INTO CATEGORIES (ID, DESCRIPTION, OWNER, TITLE, PARENT_CATEGORY_ID)
  VALUES
    (1, 'Get order list', NULL, 'Orders', NULL ),
    (2, 'Get customers', NULL, 'Customers', NULL ),
    (3, 'Get employees', NULL, 'Employees', NULL ),
    (4, 'Get offices', NULL, 'Offices', 3 ),
    (5, 'Get products', NULL, 'Products', NULL ),
    (6, 'Get orders details', NULL, 'Order details', 1 ),
    (7, 'Get product lines', NULL, 'Product lines', 5 ),
    (8, 'Get payments', NULL, 'Payments', NULL );

INSERT INTO LIB_QUERY (ID, DESCRIPTION, TEXT, TITLE, CATEGORY_ID)
    VALUES
      (1, 'Get all orders', 'SELECT * FROM ORDERS', 'All orders', 1),
      (2, 'Get orders which on hold', 'SELECT * FROM ORDERS SELECT * FROM ORDERS WHERE STATUS = ''On Hold''', 'On hold orders', 1),
      (3, 'Get orders which canceled', 'SELECT * FROM ORDERS WHERE STATUS = ''Cancelled''', 'Canceled orders', 1),
      (4, 'Get orders which disputed', 'SELECT * FROM ORDERS WHERE STATUS = ''Disputed''', 'Disputed orders', 1),
      (5, 'Get orders which in process', 'SELECT * FROM ORDERS WHERE STATUS = ''In Process''', 'In process orders', 1),
      (6, 'Get all customers', 'SELECT * FROM CUSTOMERS', 'All customers', 2),
      (7, 'Get customers from Sweden', 'SELECT * FROM CUSTOMERS WHERE COUNTRY = ''Sweden''', 'Sweden customers', 2),
      (8, 'Get all employees', 'SELECT * FROM EMPLOYEES', 'All employees', 3),
      (9, 'Get managers', 'SELECT * FROM EMPLOYESS WHERE JOB_TITLE LIKE ''%Manager%''', 'All managers', 3),
      (10, 'Get managers', 'SELECT * FROM EMPLOYESS WHERE JOB_TITLE LIKE ''%Manager%''', 'All managers', 3),
      (11, 'Get all offices', 'SELECT * FROM OFFICES', 'All offices', 4),
      (12, 'Get all offices in NA', 'SELECT * FROM OFFICES WHERE TERRITORY = ''NA''', 'All offices in NA', 4),
      (13, 'Get employee - offices', 'SELECT t1.FIRST_NAME, t1.LAST_NAME, t2.CITY, t2.COUNTRY FROM EMPLOYEES as t1, OFFICES as t2 WHERE t1.OFFICE_CODE = t2.OFFICE_CODE', 'Employees - offices', 4),
      (14, 'Get all products', 'SELECT * FROM PRODUCTS ', 'All products', 5),
      (15, 'Get all motorcycles', 'SELECT * FROM PRODUCTS WHERE PRODUCT_LINE = ''Motorcycles''', 'Get motorcycles', 5),
      (16, 'Get product which more 5000pcs', 'FROM PRODUCTS WHERE QUANTITY_IN_STOCK > 5000', 'In stock > 5000pcs', 5),
      (17, 'Get all orders details', 'SELECT * FROM ORDER_DETAILS', 'All orders detail', 6),
      (18, 'Get orders where ordered quantity > 30', 'SELECT * FROM ORDER_DETAILS WHERE QUANTITY_ORDERED > 30', 'Ordered quantity > 30', 6),
      (19, 'Get all product lines', 'SELECT * FROM PRODUCT_LINES', 'All product lines', 7),
      (20, 'Get all payments', 'SELECT * FROM PAYMENTS', 'All payments', 8),
      (21, 'Get full payments info', 'SELECT t1.CUSTOMER_NAME, t1.CONTACT_LAST_NAME, t1.CONTACT_FIRST_NAME, t1.PHONE, t1.CREDIT_LIMIT, t2.CHECK_NUMBER , t2.PAYMENT_DATE , t2.AMOUNT  FROM CUSTOMERS as t1 LEFT JOIN PAYMENTS AS t2 on t1.CUSTOMER_NUMBER = t2.CUSTOMER_NUMBER ', 'Full payments info', 8);

