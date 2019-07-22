create
    definer = root@localhost procedure updateAuthorMark(IN targ_id int, IN account_id int, IN mark_val tinyint,
                                                        IN mark_comm varchar(1000), IN date_of_mark bigint, IN id int)
begin
    UPDATE author_mark set target_id=targ_id, acc_id = account_id, mark_value = mark_val,
                                mark_comment = mark_comm, mark_date = date_of_mark where mark_id = id;
    UPDATE account set avg_mark=(select avg(mark_value) from author_mark where target_id = targ_id)
     where acc_id = targ_id;
end;

