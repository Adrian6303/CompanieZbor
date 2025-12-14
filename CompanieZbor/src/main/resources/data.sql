INSERT INTO users (username, password, admin)
VALUES ('admin', 'admin123', true);

INSERT INTO users (username, password, admin)
VALUES ('Ion Popescu', 'parola1', false);

INSERT INTO users (username, password, admin)
VALUES ('John Doe', 'parola2', false);

INSERT INTO users (username, password, admin)
VALUES ('Mike Michael', 'parola3', false);

INSERT INTO users (username, password, admin)
VALUES ('Andrei Popovici', 'manager123', true);

INSERT INTO flight (departure, arrival, departure_time, duration, nr_seats, plane_name, price)
VALUES
    ('Bucuresti','Cluj-Napoca','2025-01-10 08:00:00',55,180,'Airbus A320',89.99),
    ('Cluj-Napoca','Bucuresti','2025-01-10 18:30:00',55,180,'Airbus A320',95.50),
    ('Bucuresti','Timisoara','2025-01-11 09:15:00',60,150,'ATR 72',79.99),
    ('Iasi','Bucuresti','2025-01-11 20:45:00',65,150,'ATR 72',85.00);

INSERT INTO flight (departure, arrival, departure_time, duration, nr_seats, plane_name, price)
VALUES
    ('Bucuresti','Paris','2025-01-12 07:30:00',180,180,'Boeing 737',199.99),
    ('Paris','Bucuresti','2025-01-18 19:00:00',175,180,'Boeing 737',189.99),
    ('Bucuresti','Roma','2025-01-13 06:45:00',140,160,'Airbus A321',149.99),
    ('Roma','Bucuresti','2025-01-20 21:15:00',145,160,'Airbus A321',159.99),
    ('Bucuresti','Berlin','2025-01-14 10:00:00',155,180,'Boeing 737',169.99),
    ('Berlin','Bucuresti','2025-01-22 17:30:00',150,180,'Boeing 737',164.99);

INSERT INTO flight (departure, arrival, departure_time, duration, nr_seats, plane_name, price)
VALUES
    ('Bucuresti','Dubai','2025-01-15 23:50:00',300,250,'Boeing 787 Dreamliner',399.99),
    ('Dubai','Bucuresti','2025-01-23 04:10:00',310,250,'Boeing 787 Dreamliner',389.99),
    ('Bucuresti','New York','2025-01-16 12:00:00',600,280,'Boeing 777',699.99),
    ('New York','Bucuresti','2025-01-25 16:00:00',620,280,'Boeing 777',679.99);

INSERT INTO flight (departure, arrival, departure_time, duration, nr_seats, plane_name, price)
VALUES
    ('Bucuresti','Sofia','2025-01-17 05:30:00',45,90,'Embraer 190',59.99),
    ('Bucuresti','Viena','2025-01-17 22:10:00',80,120,'Bombardier Q400',99.99),
    ('Bucuresti','Madrid','2025-01-18 14:20:00',210,200,'Airbus A330',249.99),
    ('Bucuresti','Londra','2025-01-19 06:00:00',200,220,'Airbus A320neo',229.99),
    ('Bucuresti','Amsterdam','2025-01-19 19:40:00',170,180,'Boeing 737 MAX',189.99),
    ('Bucuresti','Atena','2025-01-20 11:30:00',120,150,'ATR 72',119.99);


INSERT INTO reservation (user_id, flight_id) VALUES (2, 1);
INSERT INTO reservation (user_id, flight_id) VALUES (2, 5);
INSERT INTO reservation (user_id, flight_id) VALUES (2, 14);

INSERT INTO reservation (user_id, flight_id) VALUES (3, 6);
INSERT INTO reservation (user_id, flight_id) VALUES (3, 10);
INSERT INTO reservation (user_id, flight_id) VALUES (3, 19);

INSERT INTO reservation (user_id, flight_id) VALUES (4, 3);
INSERT INTO reservation (user_id, flight_id) VALUES (4, 7);
INSERT INTO reservation (user_id, flight_id) VALUES (4, 8);