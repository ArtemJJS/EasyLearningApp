create
    definer = root@localhost procedure updateLesson(IN new_name text, IN new_duration mediumtext, IN id int)
begin
    update course_lesson set lesson_name = new_name, lesson_duration = new_duration where lesson_id = id;
end;

