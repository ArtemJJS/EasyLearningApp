create procedure
    insertCourseAndSetAuthor(curr_course_name varchar(200), curr_course_description varchar(1000)
                            , curr_course_creation_date varchar(50), curr_course_picture text
                            , curr_course_price decimal(10, 2), curr_state int, curr_autor_id int)
begin

    INSERT INTO course(course_name, course_description, course_creation_date,
                       course_picture, course_price, state)
    VALUES (curr_course_name, curr_course_description, curr_course_creation_date, curr_course_picture
    , curr_course_price, curr_state);

end;


INSERT INTO course(course_name, course_description, course_creation_date,
                   course_picture, course_price, state)
VALUES (?, ?, ?, ?, ?, ?)