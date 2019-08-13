package by.anelkin.easylearning.tag;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
@Setter
public class RatingTag extends TagSupport {


    private double rating;

    @Override
    public int doStartTag() throws JspException {
        Locale locale;
        Object localeAttr =  pageContext.getSession().getAttribute(ATTR_LOCALE);
        //to prevent error when logging out on page with tag:
        if (localeAttr == null) {
            locale = Locale.US;
        } else {
            String[] localeParts = localeAttr.toString().split(LOCALE_SPLITTER);
            locale = new Locale(localeParts[0], localeParts[1]);
        }
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        String notRated = rb.getString(BUNDLE_NOT_RATED);
        JspWriter writer = pageContext.getOut();
        try {
            if (rating == 0) {
                writer.write(notRated);
            } else {
                writer.write(String.valueOf(rating));
            }
        } catch (IOException e) {
            log.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
