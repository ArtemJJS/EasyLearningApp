create procedure insertPaymentAndUpdateBalance(curr_acc_id int, curr_course_id int,
                                               curr_payment_code int, curr_amount decimal(10, 2), curr_date long,
                                               curr_currency_id int,
                                               curr_description text)
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


call insertPaymentAndUpdateBalance(23, null, 15, 10.5, 1563282126825, 1, 'пополнение счета');


call insertPaymentAndUpdateBalance(23, 1, 11, -9.99, 1563282139925, 1, 'покупка курса ');


CALL insertPaymentAndUpdateBalance('23', '-1', '15', '100', '1563285696401', '1', 'Пополнение счета');



create
   procedure insertPaymentByCard(IN curr_acc_id int, IN curr_course_id int,
                                                                     IN curr_payment_code int,
                                                                     IN curr_amount decimal(10, 2),
                                                                     IN curr_date mediumtext, IN curr_currency_id int,
                                                                     IN curr_description text)
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

#     select @actual_user_balance := acc_balance from account where acc_id = curr_acc_id;
#     update account set acc_balance = (@actual_user_balance + curr_amount) where acc_id = curr_acc_id;

    #     select @actual_course_author_balance := acc_balance from account where acc_id = curr_author_id;
#     update account set acc_balance = (@actual_course_author_balance - curr_amount) where  acc_id = curr_author_id;
end;