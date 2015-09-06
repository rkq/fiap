package com.github.rkq.fiap.holding.grabber;

import com.github.rkq.fiap.holding.model.ExecutiveHoldingChange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 9/5/15.
 * grab data on web page http://data.10jqka.com.cn/financial/ggjy/
 * actual data could be acquired by ajax request on URL http://data.10jqka.com.cn/financial/ggjy/page/${page}/ajax/1/
 * the response will be HTML fragments like below:
     <div class="table_wrap m_border">
        <div class="table_aside">
            <div class="update_time">更新时间：2015-09-03 23:29:39</div>
        </div>
         <table class="m_table">
             <thead class="rows">
                 <tr>
                 <th width="30" class="first">序号</th>
                 <th width="80"  width="70"><a href="javascript:void(0);" field='stockcode' >股票代码</a></th>
                 <th width="80">股票简称</th>
                 <th width="90"  width="80"><a href="javascript:void(0);" field="bdr" >变动人</a></th>
                 <th class="cur" width="70"><a href="javascript:void(0);" field="enddate" order="asc">变动日期↓</a></th>
                 <th><a href="javascript:void(0);" field="bdgs" >变动股数(股)</a></th>
                 <th><a href="javascript:void(0);" field="cjjj" >成交均价</a></th>
                 <th width="70"><a href="javascript:void(0);" field="bdyy" >变动原因</a></th>
                 <th><a href="javascript:void(0);" field="bdbl" >变动比例</a></th>
                 <th><a href="javascript:void(0);" field="bdhgs" >变动后股数(股)</a></th>
                 <th width="80"><a href="javascript:void(0);" field="djg" >董监高</a></th>
                 <th><a href="javascript:void(0);" field="djgxc" >董监高薪酬<br>(万元)</a></th>
                 <th width="70"><a href="javascript:void(0);" field="djgzw" >董监高职务</a></th>
                 <th><a href="javascript:void(0);" field="djggx" >董监高关系</a></th>
                 </tr>
             </thead>
         <tbody>
             <tr class="even">
                 <td class="first tc">101</td>
                 <td class="tc"><a href="http://stockpage.10jqka.com.cn/002282/" target="_blank">002282</a></td>
                 <td class="tc"><a href="http://stockpage.10jqka.com.cn/002282/" target="_blank">博深工具</a></td>
                 <td class="tl" width="80">吕桂芹</td>
                 <td class="tc cur">2015-09-01</td>
                 <td class="tr c_red">235059</td>
                 <td class="tr">8.93</td>
                 <td class="tl" width="70">竞价交易</td>
                 <td class="tr c_red">0.70&permil;</td>
                 <td class="tr">44545600</td>
                 <td class="tl" width="80"><a href="http://stockpage.10jqka.com.cn/002282/company/#manager" target="_blank">吕桂芹</a></td>
                 <td class="tr">--</td>
                 <td class="tl" width="70">监事</td>
                 <td class="tl">本人</td>
             </tr>
             <tr >
                 <td class="first tc">102</td>
                 <td class="tc"><a href="http://stockpage.10jqka.com.cn/002282/" target="_blank">002282</a></td>
                 <td class="tc"><a href="http://stockpage.10jqka.com.cn/002282/" target="_blank">博深工具</a></td>
                 <td class="tl" width="80">靳发斌</td>
                 <td class="tc cur">2015-09-01</td>
                 <td class="tr c_red">132000</td>
                 <td class="tr">8.44</td>
                 <td class="tl" width="70">竞价交易</td>
                 <td class="tr c_red">0.39&permil;</td>
                 <td class="tr">5064850</td>
                 <td class="tl" width="80"><a href="http://stockpage.10jqka.com.cn/002282/company/#manager" target="_blank">靳发斌</a></td>
                 <td class="tr">--</td>
                 <td class="tl" width="70">董事、高管</td>
                 <td class="tl">本人</td>
             </tr>
         </tbody>
     </table>
     </div>
 * when grabbing the data, start with page number 1, until returned tbody has no children.
 */
public class ThsExecutiveHoldingChangeGrabber {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThsExecutiveHoldingChangeGrabber.class);
    private static final String REQUEST_DEFAULT_URL_PATTERN = "http://data.10jqka.com.cn/financial/ggjy/page/%d/ajax/1";
    private static final String REQUEST_RANGE_URL_PATTERN =
            "http://data.10jqka.com.cn/financial/ggjy/op/time/start/%s/end/%s/ajax/1/page/%d";
    private static final String REQUEST_STOCK_URL_PATTERN =
            "http://data.10jqka.com.cn/financial/ggjy/op/code/code/%s/ajax/1/page/%d";
    private static final int REQUEST_START_PAGE = 1;
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final int REQUEST_TIMEOUT_MILLISECONDS = 10 * 1000;
    private static final String JSOUP_ROW_SELECTOR = "html body div table tbody tr";
    private static final String JSOUP_FIELD_SELECTOR = "td";
    private static final int ROW_FIELD_NUM = 14;
    private static final int ROW_FIELD_IDX_SEQ = 0;
    private static final int ROW_FIELD_IDX_STOCK_CODE = 1;
    private static final int ROW_FIELD_IDX_ENTIRY_NAME = 2;
    private static final int ROW_FIELD_IDX_EXECUTIVE_NAME = 3;
    private static final int ROW_FIELD_IDX_CHANGE_DATE = 4;
    private static final int ROW_FIELD_IDX_CHANGE_COUNT = 5;
    private static final int ROW_FEILD_IDX_AVERAGE_PRICE = 6;
    private static final int ROW_FIELD_IDX_HOLDING_SHARE = 9;
    private static final SimpleDateFormat CHANGE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<ExecutiveHoldingChange> getDefaultChange() throws IOException {
        List<ExecutiveHoldingChange> changes = new ArrayList<ExecutiveHoldingChange>();
        int page = REQUEST_START_PAGE;
        while (true) {
            String url = String.format(REQUEST_DEFAULT_URL_PATTERN, page);
            LOGGER.info("Grabbing {}", url);
            Document document = Jsoup.connect(url).timeout(REQUEST_TIMEOUT_MILLISECONDS).get();
            if (!parse(document, changes)) {
                break;
            }
            ++page;
        }
        return changes;
    }

    public List<ExecutiveHoldingChange> getRangeChange(Date start, Date end) throws IOException {
        List<ExecutiveHoldingChange> changes = new ArrayList<ExecutiveHoldingChange>();
        String startParam = REQUEST_DATE_FORMAT.format(start);
        String endParam = REQUEST_DATE_FORMAT.format(end);
        int page = REQUEST_START_PAGE;
        while (true) {
            String url = String.format(REQUEST_RANGE_URL_PATTERN, startParam, endParam, page);
            LOGGER.info("Grabbing {}", url);
            Document document = Jsoup.connect(url).timeout(REQUEST_TIMEOUT_MILLISECONDS).get();
            if (!parse(document, changes)) {
                break;
            }
            ++page;
        }
        return changes;
    }

    public List<ExecutiveHoldingChange> getStockChange(String code) throws IOException {
        List<ExecutiveHoldingChange> changes = new ArrayList<ExecutiveHoldingChange>();
        int page = REQUEST_START_PAGE;
        while (true) {
            String url = String.format(REQUEST_STOCK_URL_PATTERN, code, page);
            LOGGER.info("Grabbing {}", url);
            Document document = Jsoup.connect(url).timeout(REQUEST_TIMEOUT_MILLISECONDS).get();
            if (!parse(document, changes)) {
                break;
            }
            ++page;
        }
        return changes;
    }

    private boolean parse(Document document, List<ExecutiveHoldingChange> changes) {
        Elements rows = document.select(JSOUP_ROW_SELECTOR);
        if (rows.isEmpty()) {
            return false;
        }
        for (Element row : rows) {
            Elements fields = row.select(JSOUP_FIELD_SELECTOR);
            if (fields.size() != ROW_FIELD_NUM) {
                LOGGER.warn("The number of fields in row {} is not as expected.", row.html());
                continue;
            }
            try {
                String stockCode = fields.get(ROW_FIELD_IDX_STOCK_CODE).text();
                String entityName = fields.get(ROW_FIELD_IDX_ENTIRY_NAME).text();
                String executiveName = fields.get(ROW_FIELD_IDX_EXECUTIVE_NAME).text();
                String changeDate = fields.get(ROW_FIELD_IDX_CHANGE_DATE).text();
                String changeCount = fields.get(ROW_FIELD_IDX_CHANGE_COUNT).text();
                String averagePrice = fields.get(ROW_FEILD_IDX_AVERAGE_PRICE).text();
                String holdingShare = fields.get(ROW_FIELD_IDX_HOLDING_SHARE).text();
                changes.add(new ExecutiveHoldingChange(stockCode, entityName, executiveName,
                        Integer.parseInt(changeCount),
                        Float.parseFloat(averagePrice),
                        CHANGE_DATE_FORMAT.parse(changeDate),
                        Integer.parseInt(holdingShare)));
            } catch (ParseException e) {

            } catch (NumberFormatException e) {

            }
        }
        return true;
    }
}
