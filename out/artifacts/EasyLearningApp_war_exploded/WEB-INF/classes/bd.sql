create procedure updateLesson(in new_name text, in new_path text, in new_duration long, in curr_lesson_id int)
begin
    update course_lesson
    set lesson_name            = new_name,
        lesson_content_address = new_path,
        lesson_duration        = new_duration
    where lesson_id = curr_lesson_id;

    select @curr_chapter := course_chapter_id from course_lesson where lesson_id = curr_lesson_id;

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

# =========================================

create procedure insertLesson(in new_chapter_id int, in new_date text, in new_name text, in new_path text,
                              in new_duration long)
begin
    insert into course_lesson
    set course_chapter_id      = new_chapter_id,
        lesson_name            = new_name,
        lesson_content_address = new_path,
        lesson_creation_date   = new_date,
        lesson_duration        = new_duration;

    update course_chapter
    set chapter_duration      = (select sum(lesson_duration)
                                 from course_lesson
                                 where course_lesson.course_chapter_id = new_chapter_id),
        chapter_lesson_amount = (select count(*)
                                 from course_lesson
                                 where course_lesson.course_chapter_id = new_chapter_id)
    where course_chapter_id = new_chapter_id;

    select @curr_course := course_id from course_chapter where course_chapter_id = new_chapter_id;

    update course
    set course_duration      = (select sum(chapter_duration)
                                from course_chapter
                                where course_chapter.course_id = @curr_course),
        course_lesson_amount = (select sum(chapter_lesson_amount)
                                from course_chapter
                                where course_chapter.course_id = @curr_course)
    where course_id = @curr_course;

end;

call insertLesson(5, '2019-07-15', 'Вторая глава',
                  'https://drive.google.com/file/d/14YMnWZJAMhkCqFhJI8E40QrYaVpoqAer/preview',
                  1824);


# =========================================

create procedure deleteLesson(in curr_lesson_id int)
begin
    select @curr_chapter := course_chapter_id from course_lesson where lesson_id = curr_lesson_id;

    delete from course_lesson where lesson_id = curr_lesson_id;

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

call deleteLesson(9);