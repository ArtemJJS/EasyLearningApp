create
    definer = root@localhost procedure updateLesson(IN updated_name varchar(100), IN updated_content varchar(1000),
                                                    IN updated_duration mediumtext, IN id_for_update int)
begin
    update course_lesson
    set lesson_name            = updated_name,
        lesson_content_address = updated_content,
        lesson_duration        = updated_duration
    where lesson_id = id_for_update;

#     select @updated_chapter_id := course_chapter_id from course_lesson where lesson_id = id_for_update;
#
#     update course_chapter
#     set chapter_duration =
#             (select AVG(lesson_duration)
#              from course_lesson
#              where course_lesson.course_chapter_id = @updated_chapter_id)
#         where course_chapter_id = @updated_chapter_id;
end;

