create definer = root@localhost procedure InsertAuthorMark(IN targ_id int, IN account_id int, IN mark_val tinyint, IN mark_comm varchar(1000), IN date_of_mark bigint)
begin
    INSERT into author_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
                                mark_comment = mark_comm, mark_date = date_of_mark;
    UPDATE account set avg_mark=(select avg(mark_value) from author_mark where target_id = targ_id)
    where acc_id = targ_id;
end;

create definer = root@localhost procedure InsertCourseMark(IN targ_id int, IN account_id int, IN mark_val tinyint, IN mark_comm varchar(1000), IN date_of_mark bigint)
begin
    INSERT into course_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
                                mark_comment = mark_comm, mark_date = date_of_mark;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = targ_id)
    where course_id = targ_id;
end;

create definer = root@localhost procedure deleteAuthorMark(IN id int)
begin
    SELECT target_id AS count into @author_id from author_mark where mark_id = id;
    DELETE from author_mark where mark_id = id;
    UPDATE account set avg_mark=(select avg(mark_value) from author_mark where target_id = @author_id)
    where acc_id = @author_id;
end;

create definer = root@localhost procedure deleteCourseMark(IN id int)
begin
    SELECT target_id AS count into @targ_id from course_mark where mark_id = id;
    DELETE from course_mark where mark_id = id;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = @targ_id)
    where course_id = @targ_id;
end;

create definer = root@localhost procedure deleteLesson(IN curr_lesson_id int)
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

create definer = root@localhost procedure insertLesson(IN new_chapter_id int, IN new_date text, IN new_name text, IN new_path text, IN new_duration mediumtext)
begin
    insert into course_lesson set course_chapter_id = new_chapter_id, lesson_name = new_name,
                                  lesson_content_address = new_path, lesson_creation_date = new_date,
                                  lesson_duration = new_duration;

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

create definer = root@localhost procedure insertPaymentAndUpdateBalance(IN curr_acc_id int, IN curr_course_id int, IN curr_payment_code int, IN curr_amount decimal(10,2), IN curr_date mediumtext, IN curr_currency_id int, IN curr_description text)
begin
    select @curr_course_name := course_name from course where course_id = curr_course_id;

    insert into user_payment
    set acc_id              = curr_acc_id,
        course_id           = curr_course_id,
        payment_code        = curr_payment_code,
        payment_amount      = curr_amount,
        payment_date        = curr_date,
        currency_id         = curr_currency_id,
        payment_description = coalesce(concat(curr_description, @curr_course_name), curr_description);

    select @actual_user_balance := acc_balance from account where acc_id = curr_acc_id;
    update account set acc_balance = (@actual_user_balance + curr_amount) where acc_id = curr_acc_id;

    #     select @actual_course_author_balance := acc_balance from account where acc_id = curr_author_id;
#     update account set acc_balance = (@actual_course_author_balance - curr_amount) where  acc_id = curr_author_id;
end;

create definer = root@localhost procedure insertPurchaseCourseByCard(IN curr_acc_id int, IN curr_course_id int, IN curr_payment_code int, IN curr_amount decimal(10,2), IN curr_date mediumtext, IN curr_currency_id int, IN curr_description text)
begin
    select @curr_course_name := course_name from course where course_id = curr_course_id;

    insert into user_payment
    set acc_id              = curr_acc_id,
        course_id           = curr_course_id,
        payment_code        = curr_payment_code,
        payment_amount      = curr_amount,
        payment_date        = curr_date,
        currency_id         = curr_currency_id,
        payment_description = coalesce(concat(curr_description, @curr_course_name), curr_description);

    insert into user_purchased_course set user_id = curr_acc_id, course_id = curr_course_id;
end;

create definer = root@localhost procedure insertPurchaseCourseFromBalance(IN curr_acc_id int, IN curr_course_id int, IN curr_payment_code int, IN curr_amount decimal(10,2), IN curr_date mediumtext, IN curr_currency_id int, IN curr_description text)
begin
    select @curr_course_name := course_name from course where course_id = curr_course_id;

    insert into user_payment
    set acc_id              = curr_acc_id,
        course_id           = curr_course_id,
        payment_code        = curr_payment_code,
        payment_amount      = curr_amount,
        payment_date        = curr_date,
        currency_id         = curr_currency_id,
        payment_description = coalesce(concat(curr_description, @curr_course_name), curr_description);

    select @actual_user_balance := acc_balance from account where acc_id = curr_acc_id;
    update account set acc_balance = (@actual_user_balance + curr_amount) where acc_id = curr_acc_id;

    insert into user_purchased_course set user_id = curr_acc_id, course_id = curr_course_id;
end;

create definer = root@localhost procedure updateAuthorMark(IN targ_id int, IN account_id int, IN mark_val tinyint, IN mark_comm varchar(1000), IN date_of_mark bigint, IN id int)
begin
    UPDATE author_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
                           mark_comment = mark_comm, mark_date = date_of_mark where mark_id = id;
    UPDATE account set avg_mark=(select avg(mark_value) from author_mark where target_id = targ_id)
    where acc_id = targ_id;
end;

create definer = root@localhost procedure updateCourseMark(IN targ_id int, IN account_id int, IN mark_val tinyint, IN mark_comm varchar(1000), IN date_of_mark bigint, IN id int)
begin
    UPDATE course_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
                           mark_comment = mark_comm, mark_date = date_of_mark where mark_id = id;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = targ_id)
    where course_id = targ_id;
end;

create definer = root@localhost procedure updateLesson(IN new_name text, IN new_path text, IN new_duration mediumtext, IN curr_lesson_id int)
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

