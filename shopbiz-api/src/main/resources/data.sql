INSERT INTO `category` VALUES (1,'other','Other'),(2,'clothing','Clothing'),(3,'sports','Sports'),(4,'Electronics','Electronics');

INSERT INTO `product` (`id`, `description`, `name`, `price`, `sku`, `image_path`, `is_active`, `featured`, `category_id`, `available_quantities`, `created_at`, `updated_at` ) VALUES
  (1,'LEGO Cup test','Coffee Cup',4.99,'1A2S3D4F','lego-cup.jpg',NULL,NULL,1,5,now(),NULL),
  (2,'We are providing Mike Headphones. Our products are enormously well-liked by patrons owing to their top features.','Mike Headphone',24.99,'B07T1GVZ3J','earphone.jpg',NULL,NULL,4,10,now(),NULL),
  (3,'The ultimate hydration companion.','Water Bottle',9.99,'1Q2W3E4R5T','water-bottle.jpg',NULL,NULL,1,3,now(),NULL),
  (4,'Monitor Stand ','Monitor Stand',99.99,'A9B8C7D6','monitor-stand.jpg',NULL,NULL,1,4,now(),NULL);


INSERT INTO `permission` VALUES
  (1,'ROLE_ORDER_CREATE'),
  (2,'ROLE_USER_CREATE'),
  (3,'ROLE_MANAGE_PRODUCT'),
  (4,'ROLE_ADD_CATEGORY'),
  (5,'ROLE_ADMINISTRATOR');

INSERT INTO `role` VALUES (1,'ADMIN'),(2,'CUSTOMER'),(3,'SELLER');

INSERT INTO `roles_permissions` VALUES (1,4),(1,2),(3,1),(2,1),(1,1),(1,3),(3,3),(1,5);

INSERT INTO `user` (`id`, `email`, `password`, `user_name`, `created_at`, `updated_at`) VALUES
  (1,'admin@test.com','$2a$10$oLEOhvAhzPWLd2uxBvjl7uTCDPJ9bZ2q6cMB9Mf5OBgoPPRz3fPEe','admin', now(), NULL ),
  (2,'customer@test.com','$2a$10$MT/ERiRU6C0xPbbsr1lvpeTeD0t0PtXA1YLRX2WMQ./Bfp9H9h7s2','customer', now(), NULL ),
  (3,'seller@test.com','$2a$10$2Wi4/wZg3SBpUMrhUu53sOEJiXyWpT746k8ARvzDWuQmZtSM9G39e','seller', now(), NULL );

INSERT INTO `users_roles` VALUES (1,1),(2,2),(3,3);

