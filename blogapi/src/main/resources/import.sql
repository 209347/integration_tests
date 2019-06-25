--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
insert into user (account_status, email, first_name) values ('REMOVED', 'removed@domain.com', 'Removed')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'blogpostowner@domain.com', 'BlogPost', 'Owner')

insert into blog_post values (null, 'Post Created by a confirmed user', 4)