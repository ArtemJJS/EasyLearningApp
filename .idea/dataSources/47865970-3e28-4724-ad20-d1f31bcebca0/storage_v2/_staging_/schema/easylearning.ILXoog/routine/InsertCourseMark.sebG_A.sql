create
    definer = root@localhost procedure InsertCourseMark(IN targ_id int, IN account_id int, IN mark_val tinyint,
                                                        IN mark_comm varchar(1000), IN date_of_mark bigint)
begin
    INSERT into course_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
        mark_comment = mark_comm, mark_date = date_of_mark;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = targ_id)
        where course_id = targ_id;
end;

