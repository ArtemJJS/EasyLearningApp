select * from account where acc_id in

            (select distinct acc_id from user_payment where course_id = 1 and payment_code in (21, 11))