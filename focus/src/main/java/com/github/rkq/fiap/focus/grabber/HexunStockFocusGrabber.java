package com.github.rkq.fiap.focus.grabber;

import com.github.rkq.fiap.focus.model.StockFocus;
import com.google.common.base.Preconditions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 9/5/15.
 */
public class HexunStockFocusGrabber {
    private static final Logger LOGGER = LoggerFactory.getLogger(HexunStockFocusGrabber.class);
    private static final String REQUEST_URL_PATTERN =
            "http://focus.stock.hexun.com/service/stock_sort_xml.jsp?date=%s&type=1&start=0&count=4000";
    private static final SimpleDateFormat REQUEST_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH:mm:00");
    private static final int REQUEST_TIMEOUT_IN_MILLISECONDS = 10 * 1000;
    // focus data is generated in every 15 minutes
    private static final long DATA_INTERVAL_IN_MILLISECONDS = 15 * 60 * 1000;

    private static final String JSOUP_ROW_SELECTOR = "stocks stock";
    private static final String JSOUP_FIELD_SELECTOR_SORTID = "sortid";
    private static final String JSOUP_FIELD_SELECTOR_CODE = "code";
    private static final String JSOUP_FIELD_SELECTOR_NAME = "name";
    private static final String JSOUP_FIELD_SELECTOR_RATE = "rate";

    public List<StockFocus> getLatest() throws IOException {
        return getDataWithVersion(getLatestDataVersion());
    }

    public List<StockFocus> getDataAt(Date date) throws IOException {
        long version = roundToVersion(date);
        return getDataWithVersion(version);
    }

    private List<StockFocus> getDataWithVersion(long version) throws IOException {
        Date date = new Date(version);
        String url = String.format(REQUEST_URL_PATTERN, REQUEST_DATE_TIME_FORMAT.format(date));
        LOGGER.info("Grabbing {}", url);
        Document document = Jsoup.connect(url).timeout(REQUEST_TIMEOUT_IN_MILLISECONDS).get();
        List<StockFocus> focuses = new ArrayList<StockFocus>();
        while (!parse(document, version, focuses)) {
            LOGGER.info("{} is not available.", url);
            version = getPreviousDataVersion(version);
            date = new Date(version);
            url = String.format(REQUEST_URL_PATTERN, REQUEST_DATE_TIME_FORMAT.format(date));
            LOGGER.info("Grabbing {}", url);
            document = Jsoup.connect(url).get();
        }
        return focuses;
    }

    private long getLatestDataVersion() {
        long now = System.currentTimeMillis();
        return now - now % DATA_INTERVAL_IN_MILLISECONDS;
    }

    private long getPreviousDataVersion(long now) {
        Preconditions.checkArgument(now % DATA_INTERVAL_IN_MILLISECONDS == 0);
        return now - DATA_INTERVAL_IN_MILLISECONDS;
    }

    private long roundToVersion(Date date) {
        long milliseconds = date.getTime();
        return milliseconds - milliseconds % DATA_INTERVAL_IN_MILLISECONDS;
    }

    private boolean parse(Document document, long version, List<StockFocus> focuses) {
        Elements rows = document.select(JSOUP_ROW_SELECTOR);
        if (rows.isEmpty()) {
            return false;
        }
        for (Element row : rows) {
            String sortId = row.select(JSOUP_FIELD_SELECTOR_SORTID).text();
            String code = row.select(JSOUP_FIELD_SELECTOR_CODE).text();
            String name = row.select(JSOUP_FIELD_SELECTOR_NAME).text();
            String rate = row.select(JSOUP_FIELD_SELECTOR_RATE).text();
            try {
                focuses.add(new StockFocus(Integer.parseInt(sortId),
                        code, name, Integer.parseInt(rate), version));
            } catch (NumberFormatException e) {

            }
        }
        return true;
    }
}
