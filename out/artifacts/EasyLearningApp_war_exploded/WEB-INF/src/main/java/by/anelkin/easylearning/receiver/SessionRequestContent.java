package by.anelkin.easylearning.receiver;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class SessionRequestContent {
    private static final String ATTR_DESTROY_SESSION = "destroy_session";

    private HashMap<String, Object> requestAttributes = new HashMap<>();
    private Map<String, String[]> requestParameters;
    private HashMap<String, Object> sessionAttributes = new HashMap<>();
    private ResponseType responseType;
    private String path;
    private String requestReferer;

    public enum ResponseType {
        REDIRECT,
        FORWARD
    }

    public void extractValues(@NonNull HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        String referer = request.getHeader("referer");
        referer = referer.substring(referer.indexOf(request.getContextPath()));
        referer = referer.replace("/easyLearning", "");
        requestReferer = referer;

        System.out.println(requestReferer);

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


