package com.github.rkq.fiap.focus.test;

import com.github.rkq.fiap.focus.grabber.HexunStockFocusGrabber;
import com.github.rkq.fiap.focus.model.StockFocus;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 9/5/15.
 */
public class HexunStockFocusGrabberTest {
    @Test
    public void testGetDataAt() {
        try {
            HexunStockFocusGrabber grabber = new HexunStockFocusGrabber();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:00");
            Date date = format.parse("2015-09-02_23:45:00");
            List<StockFocus> focuses = grabber.getDataAt(date);
            for (StockFocus focus : focuses) {
                System.out.println(focus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetLatest() {
        try {
            HexunStockFocusGrabber grabber = new HexunStockFocusGrabber();
            List<StockFocus> focuses = grabber.getLatest();
            for (StockFocus focus : focuses) {
                System.out.println(focus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
