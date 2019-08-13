package by.anelkin.easylearning.receiver;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static by.anelkin.easylearning.util.GlobalConstant.EMPTY_STRING;

/**
 * this class created to prevent unwanted access to request object
 * contains {@link HttpServletRequest} attributes and parameters,
 * {@link HttpSession} attributes, "referer" header of request
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Setter
@Getter
public class SessionRequestContent {
    private static final String ATTR_DESTROY_SESSION = "destroy_session";
    /**
     * {@link HttpServletRequest} attributes
     */
    private HashMap<String, Object> requestAttributes = new HashMap<>();
    /**
     * {@link HttpServletRequest} parameters
     */
    private Map<String, String[]> requestParameters = new HashMap<>();
    /**
     * {@link HttpSession} attributes
     */
    private HashMap<String, Object> sessionAttributes = new HashMap<>();
    private ResponseType responseType;
    private String path;
    private String requestReferer;
    //this form is used in email sending:
    private String requestFullReferer;

    public enum ResponseType {
        REDIRECT,
        FORWARD
    }

    /**
     * extracts values from request and insert them to {@link SessionRequestContent#requestAttributes},
     * {@link SessionRequestContent#requestParameters}, {@link SessionRequestContent#sessionAttributes}
     *
     * @param request - current {@link HttpServletRequest}
     */
    public void extractValues(@NonNull HttpServletRequest request) {
        String referer = request.getHeader("referer");
        requestFullReferer = referer;
        if (referer!=null) {
            referer = referer.substring(referer.indexOf(request.getContextPath()));
            referer = referer.replace(request.getContextPath(), EMPTY_STRING);
        }
        requestReferer = referer;

        requestParameters = request.getParameterMap();

        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            requestAttributes.put(attributeName, request.getAttribute(attributeName));
        }

        HttpSession session = request.getSession();
        Enumeration<String> sessionAttrs = session.getAttributeNames();
        while (sessionAttrs.hasMoreElements()) {
            String sessionAttributeName = sessionAttrs.nextElement();
            sessionAttributes.put(sessionAttributeName, session.getAttribute(sessionAttributeName));
        }
    }

    /**
     *  takes values from {@link SessionRequestContent#requestAttributes},
     *  {@link SessionRequestContent#requestParameters}, {@link SessionRequestContent#sessionAttributes}
     *  and insert them to {@link HttpServletRequest}
     *
     * @param request - current {@link HttpServletRequest}
     */
    public void insertAttributes(@NonNull HttpServletRequest request) {
        Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String requestAttrName = attributes.nextElement();
            if (!requestAttributes.containsKey(requestAttrName)) {
                request.removeAttribute(requestAttrName);
            }
        }
        requestAttributes.forEach(request::setAttribute);
        HttpSession session = request.getSession(false);
        if (sessionAttributes.containsKey(ATTR_DESTROY_SESSION)) {
            session.invalidate();
        } else {
            sessionAttributes.forEach(session::setAttribute);
        }
    }
}


