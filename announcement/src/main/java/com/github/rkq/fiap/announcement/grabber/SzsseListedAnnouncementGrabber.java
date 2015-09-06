package com.github.rkq.fiap.announcement.grabber;

import com.github.rkq.fiap.announcement.model.ListedAnnouncement;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 9/4/15.
 */
public class SzsseListedAnnouncementGrabber {
    private static final Logger LOGGER = LoggerFactory.getLogger(SzsseListedAnnouncementGrabber.class);
    private static final String REQUEST_URL = "http://disclosure.szse.cn/disclosure/fulltext/plate/szlatest_24h.js";
    private static final String REQUEST_FULL_URL_PATTERN = REQUEST_URL + "?ver=%s";
    private static final SimpleDateFormat REQUEST_PARAM_VER_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final String RESPONSE_CONTENT_TYPE = "text/javascript";
    private static final String RESPONSE_BODY_CHARSET = "GBK";
    private static final String FULL_TEXT_URL_PATTERN = "http://disclosure.szse.cn/m/%s";
    private static final String BODY_MESSAGE_BEGIN = "[[";
    private static final String BODY_MESSAGE_ENG = "]]";
    private static final String BODY_MESSAGE_SEP = "\\],\\[";
    private static final String MESSAGE_FIELD_SEP = ",";
    private static final int MESSAGE_FIELD_NUM = 7;
    private static final int FIELD_IDX_CODE = 0;
    private static final int FIELD_IDX_URL = 1;
    private static final int FIELD_IDX_ABSTRACT = 2;
    private static final int FIELD_IDX_DATE = 5;
    private static final SimpleDateFormat DATE_FIELD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<ListedAnnouncement> getLatest() throws IOException {
        String url = String.format(REQUEST_FULL_URL_PATTERN, REQUEST_PARAM_VER_FORMAT.format(new Date()));
        LOGGER.info("Request: {}", url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        if (response.isRedirect() || !response.isSuccessful()) {
            throw new IOException("Unexpected response received.");
        }
        byte[] body = response.body().bytes();
        return parse(body);
    }

    private List<ListedAnnouncement> parse(byte[] bytes) throws UnsupportedEncodingException {
        String body = new String(bytes, RESPONSE_BODY_CHARSET);
        String[] messages = body.substring(body.indexOf(BODY_MESSAGE_BEGIN) + BODY_MESSAGE_BEGIN.length(),
                body.lastIndexOf(BODY_MESSAGE_ENG)).split(BODY_MESSAGE_SEP);
        List<ListedAnnouncement> announcements = new ArrayList<ListedAnnouncement>();
        for (String message : messages) {
            try {
                String[] fields = message.split(MESSAGE_FIELD_SEP);
                if (fields.length != MESSAGE_FIELD_NUM) {
                    LOGGER.warn("The number of fields in message {} is not equal to {}.", message, MESSAGE_FIELD_NUM);
                    continue;
                }
                for (int i = 0; i < fields.length; ++i) {
                    fields[i] = trim(fields[i]);
                }
                String code = fields[FIELD_IDX_CODE];
                String url = String.format(FULL_TEXT_URL_PATTERN, fields[FIELD_IDX_URL]);
                String contentAbstract = fields[FIELD_IDX_ABSTRACT];
                Date date = DATE_FIELD_FORMAT.parse(fields[FIELD_IDX_DATE]);
                announcements.add(new ListedAnnouncement(code, contentAbstract, url, date));
            } catch (ParseException e) {
                LOGGER.warn("Exception {} in process message {}.", e.getMessage(), message);
            }
        }
        return announcements;
    }

    private static String trim(String s) {
        if (s.length() <= 2) {
            return "";
        } else {
            return s.substring(1, s.length() - 1);
        }
    }
}