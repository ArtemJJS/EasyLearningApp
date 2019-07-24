package by.anelkin.easylearning.tag;

import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Setter
public class RatingTag extends TagSupport {
    private static final String BUNDLE_NOT_RATED = "tag.not_rated";
    private static final String ATTR_LOCALE = "locale";
    private static final String LOCALE_SPLITTER = "_";
    private static final String RESOURCE_BUNDLE_BASE = "text_resources";
    private double rating;

    @Override
    public int doStartTag() throws JspException {
        String[] localeArr = String.valueOf(pageContext.getSession().getAttribute(ATTR_LOCALE)).split(LOCALE_SPLITTER);
        Locale locale = new Locale(localeArr[0], localeArr[1]);
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
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
