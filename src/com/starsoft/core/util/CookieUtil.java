package com.starsoft.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * CookieUtil to simplify cookie operation.
 * 
 * @author Xuefeng
 */
public class CookieUtil {

    private static final int MAX_AGE = (-1); // session scope!
    private static final String PATH = "/cart";
    private static final String BOOK_COOKIE = "BC#";

    private static Log log = LogFactory.getLog(CookieUtil.class);

    /**
     * Return a book cookie to delete immediately.
     * 
     * @param bookId Book's id.
     */
    public static Cookie deleteBookCookie(String bookId) {
        String name = BOOK_COOKIE + bookId;
        Cookie cookie = new Cookie(name, "any");
        cookie.setMaxAge(0);
        cookie.setPath(PATH);
        return cookie;
    }


    private static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String decode(String s) throws UnsupportedEncodingException {
        return URLDecoder.decode(s, "UTF-8");
    }
}
