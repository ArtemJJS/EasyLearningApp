UPDATE course SET avg_mark=
    (SELECT AVG(mark_value) from course_mark where target_id = 4)
    where course_id = 4;

create procedure InsertMark(in targ_id INT, in account_id INT, in mark_val TINYINT,
in mark_comm VARCHAR(1000), in date_of_mark DATE)
begin
    INSERT into course_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
        mark_comment = mark_comm, mark_date = date_of_mark;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = targ_id)
        where course_id = targ_id;
end;



create procedure deleteCourseMark(in id int)
begin
    SELECT target_id AS count into @targ_id from course_mark where mark_id = id;
    DELETE from course_mark where mark_id = id;
    UPDATE course set avg_mark=(select avg(mark_value) from course_mark where target_id = @targ_id)
     where course_id = @targ_id;
end;

create procedure deleteAuthorMark(in id int)
begin
    SELECT target_id AS count into @author_id from author_mark where mark_id = id;
    DELETE from author_mark where mark_id = id;
    UPDATE account set avg_mark=(select avg(mark_value) from author_mark where target_id = @author_id)
    where acc_id = @author_id;
end;



SELECT target_id AS count into @c_id from course_mark where mark_id = 4;
delete from course_mark where mark_id = @c_id;
update course set avg_mark=(select avg(mark_value) from course_mark
where target_id = @c_id) where course_id = @c_id;

# select * from course where course_id = @c_id;
SELECT target_id AS count into @c_id from course_mark where mark_id = 4;
select * from course where course_id = @c_id;

