# Companie Zbor

## Entitati DB

### User -Admin, Customer
* id int PK auto generated
* username string
* password string
* admin bool

### Reservation
* id_user FK int
* id_flight FK int

### Flight 
* id int PK auto generated
* departure string
* arrival string
* nr_seats int
* plane_name string
* start_time date
* duration int (minute)
* price double


## TO DO

* db + hibernate
* spring boot config
* rest services
* react config 
* login page
* buy tickets page