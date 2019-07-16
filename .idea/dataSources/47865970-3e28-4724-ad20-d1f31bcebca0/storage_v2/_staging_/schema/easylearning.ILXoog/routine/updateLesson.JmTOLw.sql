create
    definer = root@localhost procedure updateLesson(IN new_name text, IN new_path text, IN new_duration mediumtext,
                                                    IN curr_lesson_id int)
begin
    update course_lesson
    set lesson_name            = new_name,
        lesson_content_address = new_path,
        lesson_duration        = new_duration
    where lesson_id = curr_lesson_id;

    select @curr_chapter := course_chapter_id from course_lesson where lesson_id = curr_lesson_id;

#     вот тут можно сразу задать переменные и ниже уже просто подставлять
#         их значения вместо запросов!!!
#     select @sum_duration := sum(lesson_duration), @count_lesson := count(*)
#     from course_lesson
#     where course_lesson.course_chapter_id = @curr_chapter;

    update course_chapter
    set chapter_duration      = (select sum(lesson_duration)
                                 from course_lesson
                                 where course_lesson.course_chapter_id = @curr_chapter),
        chapter_lesson_amount = (select count(*)
                                 from course_lesson
                                 where course_lesson.course_chapter_id = @curr_chapter)
    where course_chapter_id = @curr_chapter;

    select @curr_course := course_id from course_chapter where course_chapter_id = @curr_chapter;

    update course
    set course_duration      = (select sum(chapter_duration)
                                from course_chapter
                                where course_chapter.course_id = @curr_course),
        course_lesson_amount = (select sum(chapter_lesson_amount)
                                from course_chapter
                                where course_chapter.course_id = @curr_course)
    where course_id = @curr_course;

end;

