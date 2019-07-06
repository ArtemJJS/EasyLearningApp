select * from account where acc_type = '3';

select * from account where acc_type = '2';

select * from account where acc_login = 'admin';

select * from user_purchased_course as t1 LEFT JOIN course as t2 ON t1.course_id = t2.course_id
where t1.user_id = ?;

select * from user_purchased_course as t1
            LEFT JOIN course as t2 ON t1.course_id = t2.course_id where t1.user_id = 1