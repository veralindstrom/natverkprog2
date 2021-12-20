DROP TABLE results;
DROP TABLE selector;
DROP TABLE questions;
DROP TABLE users;
DROP TABLE quizzes;


CREATE TABLE IF NOT EXISTS users (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(32) UNIQUE NOT NULL,
	password VARCHAR(32) NOT NULL
);
INSERT INTO users (username,password) VALUES ('ada@kth.se', md5('qwerty'));
INSERT INTO users (username,password) VALUES ('beda@kth.se', md5('123456'));

CREATE TABLE IF NOT EXISTS questions (
	id INT AUTO_INCREMENT PRIMARY KEY,
	text VARCHAR(255) NOT NULL,
	options VARCHAR(255) NOT NULL,
	answer VARCHAR(255) NOT NULL
);
INSERT INTO questions (text,options,answer) VALUES ('Which planets are larger than earth?', 'Mercury/Mars/Saturn', '0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('Which planets are farther away from the sun than earth?', 'Mercury/Mars/Saturn', '0/1/1');
INSERT INTO questions (text,options,answer) VALUES ('Which planets have rings?', 'Mercury/Mars/Saturn', '0/0/1');

INSERT INTO questions (text,options,answer) VALUES ('Which are the two largest countries in the world?', 'Russia/USA/China/Canada', '1/0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('Which is the smallest country in the world?', 'Monaco/Vatican City/San Marino', '0/1/0');
INSERT INTO questions (text,options,answer) VALUES ('Which is are in the Arctic region?', 'North Pole/South Pole', '1/0');
INSERT INTO questions (text,options,answer) VALUES ('Which is colder?', 'North Pole/South Pole', '0/1');

INSERT INTO questions (text,options,answer) VALUES ('When was the French Revolution?', '1790/1800/1795/1815', '1/0/1/0');
INSERT INTO questions (text,options,answer) VALUES ('Where was Christofer Columbus from?', 'Italy/Portugal/Spain', '0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('Which are the two largest empires in history?', 'Mongol/Russian/British', '0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('Which countries were neutral during World War II?', 'Schweiz/Norway/Holland', '1/1/0');

INSERT INTO questions (text,options,answer) VALUES ('What animal has the most powerful bite?', 'Grizzly bear/Hippopotamus/Shark', '0/1/0');
INSERT INTO questions (text,options,answer) VALUES ('What animal handstands to pee?', 'Koala/Sloth/Bear/Panda', '0/0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('Where do dogs sweat?', 'Tongue/Skin/Paws', '0/0/1');
INSERT INTO questions (text,options,answer) VALUES ('What animals stripes or on their skin as well as fur?', 'Zebra/Tiger/Okapi', '0/1/0');
INSERT INTO questions (text,options,answer) VALUES ('What animal is known by the nickname "sea cow"?', 'Octopus/Walrus/Manatees', '0/0/1');

INSERT INTO questions (text,options,answer) VALUES ('How old is Donald Duck?', '86/97/78', '1/0/0');
INSERT INTO questions (text,options,answer) VALUES ('Mirror mirror on the wall who’s ...?', '..fairest of them all/..fairest out of all/..the fairest one of all/..the fairest of them all', '0/0/1/0');
INSERT INTO questions (text,options,answer) VALUES ('Where can Disneyland be found?', 'Paris/Orlando/Shanghai', '1/1/1');
INSERT INTO questions (text,options,answer) VALUES ('Which was the first Disney princess?', 'Cinderella/Belle/Aurora/Snow White', '0/0/0/1');




CREATE TABLE IF NOT EXISTS quizzes (
	id INT AUTO_INCREMENT PRIMARY KEY,
	subject VARCHAR(255) NOT NULL
);
INSERT INTO quizzes (subject) VALUES ('Astronomy');
INSERT INTO quizzes (subject) VALUES ('Geography');
INSERT INTO quizzes (subject) VALUES ('History');
INSERT INTO quizzes (subject) VALUES ('Animal Kingdom');
INSERT INTO quizzes (subject) VALUES ('Disney');


CREATE TABLE IF NOT EXISTS selector(
	quiz_id INT NOT NULL,
	question_id INT NOT NULL,
	FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
	FOREIGN KEY (question_id) REFERENCES questions(id)
);
INSERT INTO selector (quiz_id, question_id) VALUES (1,1);
INSERT INTO selector (quiz_id, question_id) VALUES (1,2);
INSERT INTO selector (quiz_id, question_id) VALUES (1,3);

INSERT INTO selector (quiz_id, question_id) VALUES (2,4);
INSERT INTO selector (quiz_id, question_id) VALUES (2,5);
INSERT INTO selector (quiz_id, question_id) VALUES (2,6);
INSERT INTO selector (quiz_id, question_id) VALUES (2,7);

INSERT INTO selector (quiz_id, question_id) VALUES (3,8);
INSERT INTO selector (quiz_id, question_id) VALUES (3,9);
INSERT INTO selector (quiz_id, question_id) VALUES (3,10);
INSERT INTO selector (quiz_id, question_id) VALUES (3,11);

INSERT INTO selector (quiz_id, question_id) VALUES (4,12);
INSERT INTO selector (quiz_id, question_id) VALUES (4,13);
INSERT INTO selector (quiz_id, question_id) VALUES (4,14);
INSERT INTO selector (quiz_id, question_id) VALUES (4,15);
INSERT INTO selector (quiz_id, question_id) VALUES (4,16);

INSERT INTO selector (quiz_id, question_id) VALUES (5,17);
INSERT INTO selector (quiz_id, question_id) VALUES (5,18);
INSERT INTO selector (quiz_id, question_id) VALUES (5,19);
INSERT INTO selector (quiz_id, question_id) VALUES (5,20);


CREATE TABLE IF NOT EXISTS results (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	quiz_id INT NOT NULL,
	score INT NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);
INSERT INTO results (user_id,quiz_id,score) VALUES (1,1,0);
INSERT INTO results (user_id,quiz_id,score) VALUES (2,2,1);
INSERT INTO results (user_id,quiz_id,score) VALUES (2,1,3);
INSERT INTO results (user_id,quiz_id,score) VALUES (1,3,2);

INSERT INTO results (user_id,quiz_id,score) VALUES (2,2,3);
INSERT INTO results (user_id,quiz_id,score) VALUES (2,1,1);
INSERT INTO results (user_id,quiz_id,score) VALUES (1,2,2);


