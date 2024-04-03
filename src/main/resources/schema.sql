create table if not exists weather(
    weatherId int auto_increment,
    station varchar(255) NOT NULL,
    WMO int NOT NULL,
    airTemp int NOT NULL,
    windSpeed int NOT NULL,
    phenomenon varchar(255),
    timestamp datetime NOT NULL
);