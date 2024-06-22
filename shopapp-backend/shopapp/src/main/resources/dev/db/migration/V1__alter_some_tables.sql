alter table categories modify name varchar(50);
alter table products modify price decimal(10,2);
alter table products modify thumbnail varchar(255);

alter table users modify column phone_number varchar(15);
alter table users modify column password char(60) not null;
alter table users alter column role_id set default 1;

alter table order_details modify column price decimal(10,2),
modify column total_money decimal(10,2) default 0,
modify column number_of_products int default 1;

