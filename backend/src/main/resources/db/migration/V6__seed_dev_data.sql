-- USERS (15 total)
INSERT INTO users (id, name, email, password, is_admin) VALUES
                                                            (1,'Alice','alice@test.com','{noop}password',false),
                                                            (2,'Bob','bob@test.com','{noop}password',false),
                                                            (3,'Charlie','charlie@test.com','{noop}password',false),
                                                            (4,'Diana','diana@test.com','{noop}password',false),
                                                            (5,'Evan','evan@test.com','{noop}password',false),
                                                            (6,'Fiona','fiona@test.com','{noop}password',false),
                                                            (7,'George','george@test.com','{noop}password',false),
                                                            (8,'Hannah','hannah@test.com','{noop}password',false),
                                                            (9,'Ivan','ivan@test.com','{noop}password',false),
                                                            (10,'Julia','julia@test.com','{noop}password',false),
                                                            (11,'Kevin','kevin@test.com','{noop}password',false),
                                                            (12,'Laura','laura@test.com','{noop}password',false),
                                                            (13,'Mark','mark@test.com','{noop}password',false),
                                                            (14,'Nina','nina@test.com','{noop}password',false),
                                                            (15,'Admin','admin@test.com','{noop}password',true);


-- CATEGORIES (20 total, 5 parents + 15 children)
INSERT INTO categories (id, name, parent_id) VALUES
                                                  (1,'Fitness',NULL),
                                                  (2,'Nutrition',NULL),
                                                  (3,'Mindfulness',NULL),
                                                  (4,'Learning',NULL),
                                                  (5,'Productivity',NULL),
                                                  (6,'Drink Water',2),
                                                  (7,'Read Book',4),
                                                  (8,'Workout',1),
                                                  (9,'Meditation',3),
                                                  (10,'Sleep Early',3),
                                                  (11,'No Sugar',2),
                                                  (12,'Learn Coding',4),
                                                  (13,'Journal',3),
                                                  (14,'Morning Walk',1),
                                                  (15,'Stretching',1),
                                                  (16,'Language Learning',4),
                                                  (17,'Yoga',1),
                                                  (18,'Cold Shower',1),
                                                  (19,'Healthy Breakfast',2),
                                                  (20,'Pomodoro Focus',5);


-- HABITS (~30, mix of different categories)
INSERT INTO practices (id, name, description, streak, last_updated_streak, threshold_days, category_id, creator_id) VALUES
                                                                                                                     (1,'Drink Water','At least 2L daily',10,'2026-01-07',7,6,1),
                                                                                                                     (2,'Workout','Strength training',4,'2026-01-05',5,8,1),
                                                                                                                     (3,'Learn Coding','Solve problems on LeetCode',14,'2026-01-07',7,12,1),
                                                                                                                     (4,'Read Book','Read 20 pages',5,'2026-01-06',7,7,2),
                                                                                                                     (5,'Meditation','10 minutes',8,'2026-01-06',5,9,2),
                                                                                                                     (6,'Sleep Early','In bed by 11PM',6,'2026-01-07',7,10,3),
                                                                                                                     (7,'Morning Walk','Early 5k walk',9,'2026-01-07',5,14,4),
                                                                                                                     (8,'Pomodoro Focus','Focus sessions',12,'2026-01-07',7,20,5),
                                                                                                                     (9,'Yoga','Light flexibility',3,'2025-12-29',5,17,6),
                                                                                                                     (10,'No Sugar','Avoid candies/desserts',5,'2026-01-04',10,11,7),
                                                                                                                     (11,'Language Learning','Duolingo - Spanish',4,'2026-01-06',5,16,8),
                                                                                                                     (12,'Journal','Write daily reflections',11,'2026-01-06',7,13,9),
                                                                                                                     (13,'Healthy Breakfast','Smoothies or oats',8,'2026-01-07',7,19,10),
                                                                                                                     (14,'Stretching','After work routine',2,'2025-12-28',5,15,11),
                                                                                                                     (15,'Cold Shower','Start strong',3,'2026-01-05',3,18,12),
                                                                                                                     (16,'Evening Reflection','Gratitude notes',5,'2026-01-06',7,13,13),
                                                                                                                     (17,'Step Count 10k','Walk a full 10k steps',7,'2026-01-06',5,14,14),
                                                                                                                     (18,'Drink Water','Stay hydrated',2,'2026-01-03',7,6,2),
                                                                                                                     (19,'Workout','Cardio style',6,'2026-01-06',5,8,3),
                                                                                                                     (20,'Coding Challenge','1 coding task daily',12,'2026-01-07',5,12,4),
                                                                                                                     (21,'Pomodoro Focus','Session tracking',7,'2026-01-05',7,20,7),
                                                                                                                     (22,'Yoga Breathing','Calm mind sessions',3,'2026-01-04',5,17,10),
                                                                                                                     (23,'Read Book','Finish one per month',9,'2026-01-07',10,7,11),
                                                                                                                     (24,'Journal','Affirmations + progress',8,'2026-01-05',7,13,14),
                                                                                                                     (25,'Sleep Early','Before midnight',4,'2026-01-06',7,10,9),
                                                                                                                     (26,'Daily Coding','Personal projects',10,'2026-01-07',5,12,5),
                                                                                                                     (27,'Morning Run','Workout outdoors',6,'2026-01-07',5,14,6),
                                                                                                                     (28,'Drink Water','2L target',5,'2026-01-07',7,6,3),
                                                                                                                     (29,'Stretching','Office breaks',4,'2026-01-07',5,15,8),
                                                                                                                     (30,'Read Book','Tech/Non-fiction',10,'2026-01-07',7,7,13);


-- CHECK_INS
INSERT INTO check_ins (practice_id, date, streak_value, content, is_public, mood, created_at) VALUES
(1,'2025-12-01',1,NULL,false,NULL,'2025-12-01'),
(1,'2025-12-02',2,NULL,false,NULL,'2025-12-02'),
(1,'2025-12-03',3,'Hit 2.5L today!',true,'GOOD','2025-12-03'),
(1,'2025-12-07',1,NULL,false,NULL,'2025-12-07'),
(1,'2025-12-08',2,NULL,false,NULL,'2025-12-08'),
(1,'2025-12-10',3,NULL,false,NULL,'2025-12-10'),
(1,'2025-12-15',1,NULL,false,NULL,'2025-12-15'),
(3,'2025-12-01',1,NULL,false,NULL,'2025-12-01'),
(3,'2025-12-02',2,NULL,false,NULL,'2025-12-02'),
(3,'2025-12-05',3,NULL,false,NULL,'2025-12-05'),
(3,'2025-12-20',1,NULL,false,NULL,'2025-12-20'),
(3,'2025-12-21',2,NULL,false,NULL,'2025-12-21'),
(3,'2025-12-23',3,NULL,false,NULL,'2025-12-23'),
(3,'2026-01-02',1,NULL,false,NULL,'2026-01-02'),
(3,'2026-01-03',2,'Coding block today',false,'OKAY','2026-01-03'),
(3,'2026-01-06',5,'Finally cracked the algorithm!',true,'AWESOME','2026-01-06'),
(5,'2025-12-10',1,NULL,false,NULL,'2025-12-10'),
(5,'2025-12-11',2,NULL,false,NULL,'2025-12-11'),
(5,'2025-12-17',1,NULL,false,NULL,'2025-12-17'),
(5,'2025-12-18',2,NULL,false,NULL,'2025-12-18'),
(8,'2025-12-26',1,NULL,false,NULL,'2025-12-26'),
(8,'2025-12-27',2,NULL,false,NULL,'2025-12-27'),
(8,'2025-12-28',3,'Deep focus session',true,'GOOD','2025-12-28'),
(8,'2025-12-30',4,NULL,false,NULL,'2025-12-30'),
(8,'2026-01-02',5,NULL,false,NULL,'2026-01-02'),
(8,'2026-01-04',6,NULL,false,NULL,'2026-01-04'),
(8,'2026-01-07',7,'Pomodoro method rocks',true,'AWESOME','2026-01-07'),
(20,'2025-12-25',1,NULL,false,NULL,'2025-12-25'),
(20,'2025-12-26',2,NULL,false,NULL,'2025-12-26'),
(20,'2025-12-27',3,NULL,false,NULL,'2025-12-27'),
(20,'2025-12-30',4,'Steady progress on code',true,'GOOD','2025-12-30'),
(20,'2026-01-03',1,NULL,false,NULL,'2026-01-03'),
(20,'2026-01-04',2,NULL,false,NULL,'2026-01-04'),
(20,'2026-01-06',3,'Back on track!',true,'GOOD','2026-01-06');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));
SELECT setval('practices_id_seq', (SELECT MAX(id) FROM practices));
SELECT setval('check_ins_id_seq', (SELECT MAX(id) FROM check_ins));
