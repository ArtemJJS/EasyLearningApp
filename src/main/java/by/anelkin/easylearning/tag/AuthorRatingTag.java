package by.anelkin.easylearning.tag;

import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@Setter
public class AuthorRatingTag extends TagSupport {
    private static final String NOT_RATED_MSG = "not rated";
    private double rating;

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        try {
            if (rating == 0) {
                writer.write(NOT_RATED_MSG);
            } else {
                writer.write(String.valueOf(rating));
            }
        } catch (IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
