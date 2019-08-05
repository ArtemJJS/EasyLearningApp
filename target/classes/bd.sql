select * from course where course_id not in (
    select course_id from user_purchased_course where user_id = 30)
order by rand() limit 4;