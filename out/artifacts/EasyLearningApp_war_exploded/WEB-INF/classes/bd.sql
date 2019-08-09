select t1.acc_name, t2.course_name, t2.course_price
from account as t1
         JOIN course as t2 on t1.acc_id = t2.course_author_id;


select course_author_id, count(*)
from course
group by course_author_id
having count(*) > 1;


select (case when count(*) = 10 then true else false end) as result
from course;


select t1.acc_id, t1.acc_name, avg(t2.course_price) over (partition by t1.acc_id)
from account as t1
         join course as t2
              on t1.acc_id = t2.course_author_id and t1.acc_type = 2
group by t1.acc_id;


select t1.acc_id, t1.acc_name, sum(t2.payment_amount) over (partition by t2.payment_amount)
from account t1
         join user_payment t2 on t1.acc_id = t2.acc_id and t2.payment_code in (10, 11)
group by t1.acc_id;


select t1.acc_id, t1.acc_name, t2.sum_payment, t2.count
from account as t1
         join
     (select acc_id, sum(payment_amount) over (partition by user_payment.acc_id) as sum_payment, count(*) as count
      from user_payment
      where payment_code = 10
      group by acc_id
     ) as t2
     on t1.acc_id = t2.acc_id and t2.sum_payment > 0;

# правильно (выбор всех пользователей у которых сумма покупок по карте больше 0):
select t1.acc_id, t1.acc_name, sum(t2.payment_amount) as total_amount
from account t1
         right join user_payment t2 on t1.acc_id = t2.acc_id
where t2.payment_code = 10
group by t1.acc_id
having sum(t2.payment_amount) > 0