INSERT INTO DATA_SOURCE(ID, TYPE, NAME, DESCRIPTION)
  VALUES
    (1, 'JDBC', 'H2 Embedded DB - test data source #1', '#1 This data source is for the same database which holds category data'),
    (2, 'JDBC', 'H2 Embedded DB - test data source #2', '#2 This data source is for the same database which holds category data');

INSERT INTO DATA_SOURCE_PROPERTIES(ID, PROPERTY, VALUE, DATASOURCE_ID)
  VALUES
     (1, 'user', 'sa', 1),
     (2, 'password', 'encrypted:xHox3vNFBD/rljWMPkVFzw==', 1),
     (3, 'url', 'jdbc:h2:file:./h2_data', 1),
     (4, 'driverClassName', 'org.h2.Driver', 1);

INSERT INTO DATA_SOURCE_PROPERTIES(ID, PROPERTY, VALUE, DATASOURCE_ID)
  SELECT ID + 4, PROPERTY, VALUE, 2 FROM DATA_SOURCE_PROPERTIES
    WHERE DATASOURCE_ID = 1;

INSERT INTO CATEGORY (ID, DESCRIPTION, OWNER, TITLE, PARENT_CATEGORY_ID)
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
      (2, 'Get orders which on hold', 'SELECT * FROM ORDERS WHERE STATUS = ''On Hold''', 'On hold orders', 1),
      (3, 'Get orders which canceled', 'SELECT * FROM ORDERS WHERE STATUS = ''Cancelled''', 'Canceled orders', 1),
      (4, 'Get orders which disputed', 'SELECT * FROM ORDERS WHERE STATUS = ''Disputed''', 'Disputed orders', 1),
      (5, 'Get orders which in process', 'SELECT * FROM ORDERS WHERE STATUS = ''In Process''', 'In process orders', 1),
      (6, 'Get all customers', 'SELECT * FROM CUSTOMERS', 'All customers', 2),
      (7, 'Get customers from Sweden', 'SELECT * FROM CUSTOMERS WHERE COUNTRY = ''Sweden''', 'Sweden customers', 2),
      (8, 'Get all employees', 'SELECT * FROM EMPLOYEES', 'All employees', 3),
      (9, 'Get managers', 'SELECT * FROM EMPLOYEES WHERE JOB_TITLE LIKE ''%Manager%''', 'All managers', 3),
      (10, 'Get all offices', 'SELECT * FROM OFFICES', 'All offices', 4),
      (11, 'Get all offices in NA', 'SELECT * FROM OFFICES WHERE TERRITORY = ''NA''', 'All offices in NA', 4),
      (12, 'Get employee - offices', 'SELECT t1.FIRST_NAME, t1.LAST_NAME, t2.CITY, t2.COUNTRY FROM EMPLOYEES as t1, OFFICES as t2 WHERE t1.OFFICE_CODE = t2.OFFICE_CODE', 'Employees - offices', 4),
      (13, 'Get all products', 'SELECT * FROM PRODUCTS ', 'All products', 5),
      (14, 'Get all motorcycles', 'SELECT * FROM PRODUCTS WHERE PRODUCT_LINE = ''Motorcycles''', 'Get motorcycles', 5),
      (15, 'Get product which more 5000pcs', 'SELECT * FROM PRODUCTS WHERE QUANTITY_IN_STOCK > 5000', 'In stock > 5000pcs', 5),
      (16, 'Get all orders details', 'SELECT * FROM ORDER_DETAILS', 'All orders detail', 6),
      (17, 'Get orders where ordered quantity > 30', 'SELECT * FROM ORDER_DETAILS WHERE QUANTITY_ORDERED > 30', 'Ordered quantity > 30', 6),
      (18, 'Get all product lines', 'SELECT * FROM PRODUCT_LINES', 'All product lines', 7),
      (19, 'Get all payments', 'SELECT * FROM PAYMENTS', 'All payments', 8),
      (20, 'Get full payments info', 'SELECT t1.CUSTOMER_NAME, t1.CONTACT_LAST_NAME, t1.CONTACT_FIRST_NAME, t1.PHONE, t1.CREDIT_LIMIT, t2.CHECK_NUMBER , t2.PAYMENT_DATE , t2.AMOUNT  FROM CUSTOMERS as t1 LEFT JOIN PAYMENTS AS t2 on t1.CUSTOMER_NUMBER = t2.CUSTOMER_NUMBER ', 'Full payments info', 8),
      (42, 'Get employees by job title', 'SELECT * FROM EMPLOYEES WHERE JOB_TITLE LIKE '':job_title''', 'All managers', 3),
      (43, 'Get product by quantity', 'SELECT * FROM PRODUCTS WHERE QUANTITY_IN_STOCK > :quantity AND BUY_PRICE > :price', 'In stock > any quantity', 5);

INSERT INTO QUERY_PARAM  (ID, NAME, TYPE, QUERY_ID)
    VALUES
      (1, 'job_title', 'STRING', 42),
      (2, 'quantity', 'NUMBER', 43),
      (3, 'price', 'NUMBER', 43);

INSERT INTO CATEGORY (ID, DESCRIPTION, OWNER, TITLE, PARENT_CATEGORY_ID)
  VALUES
    (9, 'Get order list', 'sergey', 'Orders', NULL ),
    (10, 'Get customers', 'sergey', 'Customers', NULL ),
    (11, 'Get employees', 'sergey', 'Employees', NULL ),
    (12, 'Get offices', 'sergey', 'Offices', 11 ),
    (13, 'Get products', 'sergey', 'Products', NULL ),
    (14, 'Get orders details', 'sergey', 'Order details', 9 ),
    (15, 'Get product lines', 'sergey', 'Product lines', 13 ),
    (16, 'Get payments', 'sergey', 'Payments', NULL );

INSERT INTO LIB_QUERY (ID, DESCRIPTION, TEXT, TITLE, CATEGORY_ID)
VALUES
  (21, 'Get all orders', 'SELECT * FROM ORDERS', 'All orders', 9),
  (22, 'Get orders which on hold', 'SELECT * FROM ORDERS WHERE STATUS = ''On Hold''', 'On hold orders', 9),
  (23, 'Get orders which canceled', 'SELECT * FROM ORDERS WHERE STATUS = ''Cancelled''', 'Canceled orders', 9),
  (24, 'Get orders which disputed', 'SELECT * FROM ORDERS WHERE STATUS = ''Disputed''', 'Disputed orders', 9),
  (25, 'Get orders which in process', 'SELECT * FROM ORDERS WHERE STATUS = ''In Process''', 'In process orders', 9),
  (26, 'Get all customers', 'SELECT * FROM CUSTOMERS', 'All customers', 10),
  (27, 'Get customers from Sweden', 'SELECT * FROM CUSTOMERS WHERE COUNTRY = ''Sweden''', 'Sweden customers', 10),
  (28, 'Get all employees', 'SELECT * FROM EMPLOYEES', 'All employees', 11),
  (29, 'Get managers', 'SELECT * FROM EMPLOYEES WHERE JOB_TITLE LIKE ''%Manager%''', 'All managers', 11),
  (30, 'Get managers', 'SELECT * FROM EMPLOYEES WHERE JOB_TITLE LIKE ''%Manager%''', 'All managers', 11),
  (31, 'Get all offices', 'SELECT * FROM OFFICES', 'All offices', 12),
  (32, 'Get all offices in NA', 'SELECT * FROM OFFICES WHERE TERRITORY = ''NA''', 'All offices in NA', 12),
  (33, 'Get employee - offices', 'SELECT t1.FIRST_NAME, t1.LAST_NAME, t2.CITY, t2.COUNTRY FROM EMPLOYEES as t1, OFFICES as t2 WHERE t1.OFFICE_CODE = t2.OFFICE_CODE', 'Employees - offices', 12),
  (34, 'Get all products', 'SELECT * FROM PRODUCTS ', 'All products', 13),
  (35, 'Get all motorcycles', 'SELECT * FROM PRODUCTS WHERE PRODUCT_LINE = ''Motorcycles''', 'Get motorcycles', 13),
  (36, 'Get product which more 5000pcs', 'SELECT * FROM PRODUCTS WHERE QUANTITY_IN_STOCK > 5000', 'In stock > 5000pcs', 13),
  (37, 'Get all orders details', 'SELECT * FROM ORDER_DETAILS', 'All orders detail', 14),
  (38, 'Get orders where ordered quantity > 30', 'SELECT * FROM ORDER_DETAILS WHERE QUANTITY_ORDERED > 30', 'Ordered quantity > 30', 14),
  (39, 'Get all product lines', 'SELECT * FROM PRODUCT_LINES', 'All product lines', 15),
  (40, 'Get all payments', 'SELECT * FROM PAYMENTS', 'All payments', 16),
  (41, 'Get full payments info', 'SELECT t1.CUSTOMER_NAME, t1.CONTACT_LAST_NAME, t1.CONTACT_FIRST_NAME, t1.PHONE, t1.CREDIT_LIMIT, t2.CHECK_NUMBER , t2.PAYMENT_DATE , t2.AMOUNT  FROM CUSTOMERS as t1 LEFT JOIN PAYMENTS AS t2 on t1.CUSTOMER_NUMBER = t2.CUSTOMER_NUMBER ', 'Full payments info', 16);