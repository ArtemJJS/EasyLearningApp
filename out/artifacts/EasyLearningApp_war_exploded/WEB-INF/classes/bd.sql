UPDATE course
SET course_name          = 'third course'
  , course_description   = 'third very very very very very very very very very interesting course'
  , course_creation_date = '2019-07-04'
  , course_picture       = '/resources/course_avatar/default_course_avatar.png'
  , course_price         = '11.55'
  , state          = '0'
WHERE course_id = '5';

insert into temp_bit_test set column_1 = 1;
select * from temp_bit_test;