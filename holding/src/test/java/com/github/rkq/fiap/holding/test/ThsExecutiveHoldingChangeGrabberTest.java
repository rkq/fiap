package com.github.rkq.fiap.holding.test;

import com.github.rkq.fiap.holding.grabber.ThsExecutiveHoldingChangeGrabber;
import com.github.rkq.fiap.holding.model.ExecutiveHoldingChange;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by rick on 9/5/15.
 */
public class ThsExecutiveHoldingChangeGrabberTest {
//    @Test
//    public void testGetDefaultChange() {
//        try {
//            ThsExecutiveHoldingChangeGrabber grabber = new ThsExecutiveHoldingChangeGrabber();
//            List<ExecutiveHoldingChange> changes = grabber.getDefaultChange();
//            for (ExecutiveHoldingChange change : changes) {
//                System.out.println(change);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testGetStockChange() {
//        try {
//            ThsExecutiveHoldingChangeGrabber grabber = new ThsExecutiveHoldingChangeGrabber();
//            List<ExecutiveHoldingChange> changes = grabber.getStockChange("002282");
//            for (ExecutiveHoldingChange change : changes) {
//                System.out.println(change);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testGetRangeChange() {
        try {
            ThsExecutiveHoldingChangeGrabber grabber = new ThsExecutiveHoldingChangeGrabber();
            Date today = new Date();
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(GregorianCalendar.MONTH, -6);
            Date sixMonthAgo = gc.getTime();
            List<ExecutiveHoldingChange> changes = grabber.getRangeChange(sixMonthAgo, today);
            for (ExecutiveHoldingChange change : changes) {
                System.out.println(change);
            }
        } catch (Exception e) {

        }
    }
}
