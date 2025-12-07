## TO DO

* db + hibernate
* spring boot config
* rest services
* react config 
* login page
* buy tickets page



Entitati DB

## User -Admin, Customer
* id int auto generated
* username string
* password string
* admin bool

## Reservation
* id_user fk int
* id_flight fk int

## Flight 
* id int auto generated
* departure string
* arival string
* nr_seats int
* plane_name string
* start_time date
* duration int (minute)
* price double
