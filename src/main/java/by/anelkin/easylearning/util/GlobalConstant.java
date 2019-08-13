package by.anelkin.easylearning.util;

import java.text.SimpleDateFormat;

/**
 * Represents constants, used in different parts of application
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
public class GlobalConstant {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String URI_SPACE_REPRESENT = "%20";
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String PATH_SPLITTER = "/";
    public static final String EMPTY_STRING = "";
    public static final String LOCALE_SPLITTER = "_";
    public static final String CARD_NUMBER_SPLITTER = " ";
    public static final String COLON_SYMBOL = ":";
    public static final String DEFAULT_ACC_AVATAR = "default_acc_avatar";
    public static final int ERROR_403 = 403;
    public static final int ERROR_500 = 500;
    public static final int ERROR_404 = 404;

    //for services, filters etc.
    public static final String ATTR_USER = "user";
    public static final String ATTR_ROLE = "role";
    public static final String ATTR_PWD = "password";
    public static final String ATTR_UPDATED_PWD = "updated_password";
    public static final String ATTR_REPEATED_PWD = "repeated_password";
    public static final String ATTR_OPERATION_RESULT = "operation_result";
    public static final String ATTR_AVAILABLE_COURSES = "coursesAvailable";
    public static final String ATTR_RECOMMENDED_COURSES = "courses_recommended";
    public static final String ATTR_LOGIN = "login";
    public static final String ATTR_WRONG_LOGIN_MSG = "wrong-login";
    public static final String ATTR_REQUESTED_AUTHOR_LOGIN = "requested_author_login";
    public static final String ATTR_REQUESTED_AUTHOR = "requested_author";
    public static final String ATTR_AUTHOR_COURSE_LIST = "author_course_list";
    public static final String ATTR_FILE_EXTENSION = "file_extension";
    public static final String ATTR_BIRTHDATE = "birthdate";
    public static final String ATTR_ACCS_TO_AVATAR_APPROVE = "acc_avatar_approve_list";
    public static final String ATTR_IS_AUTHOR_MARKED_ALREADY = "is_author_marked_already";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_UUID = "uuid";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_SURNAME = "surname";
    public static final String ATTR_EMAIL = "email";
    public static final String ATTR_PHONENUMBER = "phonenumber";
    public static final String ATTR_ABOUT = "about";
    public static final String ATTR_COURSES_LIST = "courses_list";
    public static final String ATTR_COURSE_ID = "course_id";
    public static final String ATTR_COURSE_NAME = "course_name";
    public static final String ATTR_COURSE_DESCRIPTION = "course_description";
    public static final String ATTR_COURSE_PRICE = "course_price";
    public static final String ATTR_CHAPTER_NAME = "chapter_name";
    public static final String ATTR_COURSE_ALREADY_EXISTS = "course_exists_msg";
    public static final String ATTR_SEARCH_KEY = "search_key";
    public static final String ATTR_PAGE = "page";
    public static final String ATTR_HAS_MORE_PAGES = "has_more_pages";
    public static final String ATTR_LOCALE = "locale";
    public static final String ATTR_CURR_COURSE_MARKS = "currentCourseMarks";
    public static final String ATTR_REQUESTED_COURSE = "requestedCourse";
    public static final String ATTR_CURR_COURSE_CONTENT = "currentCourseContent";
    public static final String ATTR_AUTHOR_OF_COURSE = "author_of_course";
    public static final String ATTR_COURSE_ID_DEFIS = "course-id";
    public static final String ATTR_COMMENT = "comment";
    public static final String ATTR_TARGET_ID = "target_id";
    public static final String ATTR_MARK_VALUE = "mark_value";
    public static final String ATTR_AUTHOR_LOGIN = "author_login";
    public static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";
    public static final String ATTR_AMOUNT = "amount";
    public static final String ATTR_CURRENCY = "currency";
    public static final String ATTR_CARD = "card";
    public static final String ATTR_PAYMENTS = "payments";
    public static final String ATTR_MARK_ID = "mark_id";
    public static final String ATTR_MARK_COMMENT = "mark_comment";
    public static final String ATTR_COURSE_MARK_COUNT = "course_mark_count";
    public static final String ATTR_COURSE_PURCHASE_COUNT = "course_purchase_count";
    public static final String ATTR_AUTHOR_MARKS = "author_marks";
    public static final String ATTR_COMMAND_NAME = "command_name";
    public static final String ATTR_IS_NEED_COOKIE = "is_need_cookie";
    public static final String ATTR_IMG_TO_UPLOAD = "img_to_upload";


    public static final String PATH_RELATIVE_COURSE_IMG_FOLDER = "resources/course_img/";
    public static final String DEFAULT_COURSE_AVATAR = "default_course_avatar";
    public static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    public static final String PWD_CHANGED_SUCCESSFULLY_MSG = "You password has been successfully changed!!!";
    public static final String RESOURCE_BUNDLE_BASE = "text_resources";
    public static final String PWD_NOT_CHANGED_MSG = "You password wasn't changed! Check inserted data!";
    public static final String MSG_AVATAR_APPROVED = "Avatar changed successfully to account: ";
    public static final String MSG_AVATAR_DECLINED = "Avatar change was declined to account: ";
    public static final String FILE_STORAGE_BUNDLE_BASE = "file_storage";
    public static final String BUNDLE_PICTURE_APPROVED = "msg.course_picture_changed_successfully";
    public static final String BUNDLE_PICTURE_DECLINED = "msg.course_picture_change_was_declined";
    public static final String BUNDLE_COURSE_APPROVED = "msg.course_was_approved";
    public static final String BUNDLE_COURSE_FROZEN = "msg.course_was_declined";
    public static final String BUNDLE_COURSE_ALREADY_EXISTS = "msg.course_already_exists";
    public static final String BUNDLE_COURSE_SENT_TO_REVIEW = "msg.course_sent_to_review";
    public static final String BUNDLE_INCORRECT_DATA = "msg.incorrect_data";
    public static final String DEFAULT_IMG = "default_course_avatar.png";
    public static final String PATTERN_LESSON_TITLE = "lesson_title_";
    public static final String PATTERN_LESSON_CONTENT = "lesson_content_";
    public static final String PATTERN_LESSON_DURATION = "lesson_duration_";


    public static final String PROP_FILE_FOLDER = "file_folder";
    public static final String PROP_EMAIL_SUBJECT = "email_subject";
    public static final String PROP_EMAIL_ADDRESS = "mail.user.name";
    public static final String PROP_EMAIL_MESSAGE = "email_message";
    public static final String PROP_EMAIL_PASSWORD = "mail.user.password";
    public static final String MAIL_INFO_PROPERTIES = "mail_info.properties";
    public static final String BUNDLE_INCORRECT_AGE = "msg.incorrect_date_of_birth";
    public static final String BUNDLE_CHANGE_AVATAR_REQUEST = "msg.change_avatar_request_was_sent";
    public static final String BUNDLE_INCORRECT_REPEATED_PASS_OR_PATTERN = "msg.incorrect_repeated_password_or_pattern";
    public static final String BUNDLE_LOGIN_NOT_EXISTS = "msg.login_not_exists";
    public static final String BUNDLE_EMAIL_SENT = "msg.email_sent";
    public static final String BUNDLE_PAGE_NOT_FOUND = "error.page_not_found";
    public static final String BUNDLE_ETERNAL_SERVER_ERROR = "error.eternal_server_error";
    public static final String BUNDLE_ACCESS_DENIED = "error.access_denied";
    public static final String BUNDLE_PAYMENT_SUCCEEDED = "msg.payment_approved";
    public static final String BUNDLE_PAYMENT_DECLINED = "msg.payment_declined";
    public static final String BUNDLE_WRONG_DEPOSIT_DATA = "msg.wrong_deposit_data";
    public static final String BUNDLE_LEARNING_PAGE = "tag.learning_page";
    public static final String BUNDLE_EDIT_COURSE = "tag.edit_course";
    public static final String BUNDLE_EDIT_IMAGE = "tag.edit_image";
    public static final String BUNDLE_BUY_COURSE = "tag.buy_course";
    public static final String BUNDLE_MARK_COURSE = "tag.mark_course";
    public static final String BUNDLE_NOT_RATED = "tag.not_rated";
    public static final String USER_LEARNING_PAGE_LINK = "/course/learn?course-id=";
    public static final String USER_MARK_COURSE_LINK = "/user/mark-course?course-id=";
    public static final String USER_BUY_COURSE_LINK = "/user/buy-course?course-id=";
    
    
    
    public static final String DESCRIPTION_DEPOSIT_BY_CARD = "Deposit from card ends with ";
    public static final String DESCRIPTION_CASH_OUT_TO_CARD = "Cash out to card ends with ";
    public static final String DESCRIPTION_BUY_WITH_CARD = "Purchasing by card ends with %s. Course: ";
    public static final String DESCRIPTION_BUY_FROM_BALANCE = "Purchasing from balance. Course: ";
    public static final String DESCRIPTION_SALE_COURSE = "Sale course: ";


    
    

    // for repositories;
    public static final String ACC_ID = "acc_id";
    public static final String ACC_LOGIN = "acc_login";
    public static final String ACC_PWD = "acc_password";
    public static final String ACC_EMAIL = "acc_email";
    public static final String ACC_NAME = "acc_name";
    public static final String ACC_SURNAME = "acc_surname";
    public static final String ACC_BIRTHDATE = "acc_birthdate";
    public static final String ACC_PHONE_NUMBER = "acc_phone_number";
    public static final String ACC_REGISTR_DATE = "acc_registration_date";
    public static final String ACC_ABOUT = "acc_about";
    public static final String ACC_BALANCE = "acc_balance";
    public static final String ACC_PASS_SALT = "acc_pass_salt";
    public static final String ACC_UPDATE_PHOTO_PATH = "update_photo_path";
    public static final String ACC_PHOTO_PATH = "acc_photo_path";
    public static final String ACC_AVG_MARK = "avg_mark";
    public static final String ACC_TYPE = "acc_type";
    public static final String COURSE_CHAPTER_ID = "course_chapter_id";
    public static final String CHAPTER_NAME = "chapter_name";
    public static final String CHAPTER_LESSON_AMOUNT = "chapter_lesson_amount";
    public static final String CHAPTER_DURATION = "chapter_duration";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_AUTHOR_ID = "course_author_id";
    public static final String COURSE_DESCRIPTION = "course_description";
    public static final String COURSE_CREATION_DATE = "course_creation_date";
    public static final String COURSE_PICTURE = "course_picture";
    public static final String COURSE_PRICE = "course_price";
    public static final String COURSE_LESSON_AMOUNT = "course_lesson_amount";
    public static final String COURSE_DURATION = "course_duration";
    public static final String COURSE_STATE = "state";
    public static final String COURSE_AVG_MARK = "avg_mark";
    public static final String COURSE_UPDATE_IMG_PATH = "update_img_path";
    public static final String LESSON_ID = "lesson_id";
    public static final String LESSON_NAME = "lesson_name";
    public static final String LESSON_CREATION_DATE = "lesson_creation_date";
    public static final String LESSON_DURATION = "lesson_duration";
    public static final String LESSON_CONTENT_ADDRESS = "lesson_content_address";
    public static final String MARK_ID = "mark_id";
    public static final String MARK_TARGET_ID = "target_id";
    public static final String MARK_VALUE = "mark_value";
    public static final String MARK_COMMENT = "mark_comment";
    public static final String MARK_DATE = "mark_date";
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_CODE = "payment_code";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_CURRENCY_ID = "currency_id";
    public static final String PAYMENT_DESCRIPTION = "payment_description";
    public static final String RESTORE_PASS_UUID = "uuid";


}
