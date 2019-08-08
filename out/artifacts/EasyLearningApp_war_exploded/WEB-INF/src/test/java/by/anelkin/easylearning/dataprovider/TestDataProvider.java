package by.anelkin.easylearning.dataprovider;

import by.anelkin.easylearning.specification.account.*;
import by.anelkin.easylearning.specification.chapter.SelectAllChapterSpecification;
import by.anelkin.easylearning.specification.chapter.SelectAllFromCourseSpecification;
import by.anelkin.easylearning.specification.chapter.SelectChapterByNameAndCourseIdSpecification;
import by.anelkin.easylearning.specification.course.*;
import by.anelkin.easylearning.specification.lesson.SelectAllLessonSpecification;
import by.anelkin.easylearning.specification.lesson.SelectByChapterIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectByTargetIdWithWriterInfoSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarkByTargetIdSpecification;
import by.anelkin.easylearning.specification.mark.SelectMarksMadeByUserSpecification;
import by.anelkin.easylearning.specification.payment.SelectAllPaymentSpecification;
import by.anelkin.easylearning.specification.payment.SelectPaymentByAccountIdSpecification;
import by.anelkin.easylearning.specification.restorepass.SelectRestoreByAccIdSpecification;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static by.anelkin.easylearning.entity.Account.AccountType.*;
import static by.anelkin.easylearning.entity.Course.CourseState.*;
import static by.anelkin.easylearning.entity.Mark.MarkType.*;

public class TestDataProvider {

    //check id of queried accounts
    @DataProvider(name = "getAccountSpecifications")
    public static Object[][] getAccountSpecifications() {
        return new Object[][]{
                {new SelectAllAccountSpecification(), new ArrayList<>(Arrays.asList(1, 2, 3))},
                {new SelectAccByIdSpecification(1), new ArrayList<>(Collections.singletonList(1))},
                {new SelectAuthorOfCourseSpecification(1), new ArrayList<>(Collections.singletonList(2))},
                {new SelectAccByChangePassUuidSpecification("12345"), new ArrayList<>(Collections.singletonList(1))},
                {new SelectAccByTypeSpecification(ADMIN), new ArrayList<>(Collections.singletonList(1))},
                {new SelectAccToPhotoApproveSpecification(), new ArrayList<>(Collections.singletonList(1))},
                {new SelectAccByLoginSpecification("Author"), new ArrayList<>(Collections.singletonList(2))},
        };
    }

    @DataProvider(name = "getChapterSpecifications")
    public static Object[][] getChapterSpecifications() {
        return new Object[][]{
                {new SelectAllChapterSpecification(), new ArrayList<>(Arrays.asList(1, 2, 3, 4))},
                {new SelectAllFromCourseSpecification(2), new ArrayList<>(Arrays.asList(3, 4))},
                {new SelectChapterByNameAndCourseIdSpecification("Updated chapter name", 1), new ArrayList<>(Collections.singletonList(2))},
        };
    }

    @DataProvider(name = "getCourseSpecifications")
    public static Object[][] getCourseSpecifications() {
        return new Object[][]{
                {new SelectAllCourseSpecification(), new ArrayList<>(Arrays.asList(1, 2, 3))},
                {new SelectByAuthorIdSpecification(2), new ArrayList<>(Arrays.asList(1, 2, 3))},
                {new SelectByStateSpecification(APPROVED), new ArrayList<>(Collections.singletonList(1))},
                {new SelectCourseByIdSpecification(1), new ArrayList<>(Collections.singletonList(1))},
                {new SelectCourseByNameSpecification("Second Course"), new ArrayList<>(Collections.singletonList(2))},
                {new SelectCourseSearchSpecification("n", 3, 0), new ArrayList<>(Collections.singletonList(1))},
                {new SelectCoursesPurchasedByUserSpecification(3), new ArrayList<>(Collections.singletonList(1))},
                {new SelectCourseUpdateImgSpecification(), new ArrayList<>(Collections.emptyList())},
        };
    }

    @DataProvider(name = "getLessonSpecifications")
    public static Object[][] getLessonSpecifications() {
        return new Object[][]{
                {new SelectAllLessonSpecification(), new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7))},
                {new SelectByChapterIdSpecification(1), new ArrayList<>(Arrays.asList(1, 3))},
        };
    }

    @DataProvider(name = "getMarkSpecifications")
    public static Object[][] getMarkSpecifications() {
        return new Object[][]{
                {new SelectByTargetIdWithWriterInfoSpecification(COURSE_MARK, 3), new ArrayList<>(Collections.singletonList(1))},
                {new SelectMarkByIdSpecification(COURSE_MARK, 1), new ArrayList<>(Collections.singletonList(1))},
                {new SelectMarkByTargetIdSpecification(COURSE_MARK, 3), new ArrayList<>(Collections.singletonList(1))},
                {new SelectMarksMadeByUserSpecification(COURSE_MARK, 3), new ArrayList<>(Collections.singletonList(1))},
        };
    }

    @DataProvider(name = "getPaymentSpecifications")
    public static Object[][] getPaymentSpecifications() {
        return new Object[][]{
                {new SelectAllPaymentSpecification(), new ArrayList<>(Arrays.asList(1, 2))},
                {new SelectPaymentByAccountIdSpecification(3), new ArrayList<>(Collections.singletonList(1))},
        };
    }

    @DataProvider(name = "getRestorePassRequestSpecifications")
    public static Object[][] getRestorePassRequestSpecifications() {
        return new Object[][]{
                {new SelectRestoreByAccIdSpecification(1), new ArrayList<>(Collections.singletonList(1))},
        };
    }

}
