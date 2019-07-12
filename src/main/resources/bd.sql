select * from course where course_id IN (select course_id from author_of_course where author_id = 2)


