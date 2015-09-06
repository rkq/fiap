package com.github.rkq.fiap.aggregator.app;

import com.github.rkq.fiap.aggregator.model.ConsolidationInfo;
import com.github.rkq.fiap.focus.grabber.HexunStockFocusGrabber;
import com.github.rkq.fiap.focus.model.StockFocus;
import com.github.rkq.fiap.holding.grabber.ThsExecutiveHoldingChangeGrabber;
import com.github.rkq.fiap.holding.model.ExecutiveHoldingChange;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 9/5/15.
 */
public class ConsolidationApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsolidationApp.class);

    public static void main(String[] args) {
        try {
            HexunStockFocusGrabber focusGrabber = new HexunStockFocusGrabber();
            LOGGER.info("Getting focus rate information...");
            List<StockFocus> focuses = focusGrabber.getLatest();
            LOGGER.info("The number of focus rate information is {}.", focuses.size());
            LOGGER.info("Getting holding change information...");
            Map<String, Integer> holdingChanges = getHoldingChangeData();
            LOGGER.info("The number of holding change information is {}.", holdingChanges.size());
            List<ConsolidationInfo> info = new ArrayList<ConsolidationInfo>();
            for (StockFocus focus : focuses) {
                String code = focus.getCode();
                String name = focus.getName();
                int focusRate = focus.getRate();
                int holdingChange = 0;
                if (holdingChanges.containsKey(code)) {
                    holdingChange = holdingChanges.get(code);
                }
                info.add(new ConsolidationInfo(code, name, focusRate, holdingChange));
            }
            LOGGER.info("Creating final file...");
            createFile(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> getFocusData() throws IOException {
        HexunStockFocusGrabber grabber = new HexunStockFocusGrabber();
        List<StockFocus> focuses = grabber.getLatest();
        Map<String, Integer> data = new HashMap<String, Integer>();
        for (StockFocus focus : focuses) {
            data.put(focus.getCode(), focus.getRate());
        }
        return data;
    }

    private static Map<String, Integer> getHoldingChangeData() throws IOException {
        ThsExecutiveHoldingChangeGrabber grabber = new ThsExecutiveHoldingChangeGrabber();
        Date today = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, -6);
        Date sixMonthAgo = gc.getTime();
        List<ExecutiveHoldingChange> changes = grabber.getRangeChange(sixMonthAgo, today);
        Map<String, Integer> data = new HashMap<String, Integer>();
        for (ExecutiveHoldingChange change : changes) {
            if (data.containsKey(change.getStockCode())) {
                int count = data.get(change.getStockCode());
                data.put(change.getStockCode(), count + change.getHoldingChange());
            } else {
                data.put(change.getStockCode(), change.getHoldingChange());
            }
        }
        return data;
    }

    private static void createFile(List<ConsolidationInfo> info) throws IOException {
        try {
            String fileName = "stock-" + (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) + ".xls";
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet sheet = workbook.createSheet("A股信息", 0);
            Label codeLabel = new Label(0, 0, "股票代码");
            sheet.addCell(codeLabel);
            Label nameLabel = new Label(1, 0, "股票简称");
            sheet.addCell(nameLabel);
            Label focusRateLabel = new Label(2, 0, "关注度");
            sheet.addCell(focusRateLabel);
            Label holdChangeLabel = new Label(3, 0, "最近六个月高管持股变化");
            sheet.addCell(holdChangeLabel);

            int row = 1;
            for (ConsolidationInfo cinfo : info) {
                sheet.addCell(new Label(0, row, cinfo.getCode()));
                sheet.addCell(new Label(1, row, cinfo.getName()));
                sheet.addCell(new Number(2, row, cinfo.getFocusRate()));
                sheet.addCell(new Number(3, row, cinfo.getHoldingChange()));
                ++row;
            }

            workbook.write();
            workbook.close();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
