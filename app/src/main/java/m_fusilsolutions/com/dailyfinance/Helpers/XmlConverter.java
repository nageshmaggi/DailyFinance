package m_fusilsolutions.com.dailyfinance.Helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.DashBoardData;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;
import m_fusilsolutions.com.dailyfinance.Models.ResultData;
import m_fusilsolutions.com.dailyfinance.R;

/**
 * Created by Android on 23-08-2019.
 */

public class XmlConverter {
    public static NodeList StringToXMLformat(String result)
    {
        NodeList nodeList = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result));

            Document doc = db.parse(is);
            if(doc.hasChildNodes()) {
                nodeList = doc.getChildNodes();
                if(nodeList!=null&&nodeList.getLength()>0) {
                    return nodeList;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodeList;
    }

    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }
            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    public List<DailyFinanceData> PanelDataParentToChildIteration(NodeList nodeList, List<DailyFinanceData> reportList) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("PanelConfigData")) {
                DailyFinanceData parsedata = new DailyFinanceData();
                if(node.getAttributes().getNamedItem("PanelId")!=null)
                {
                    parsedata.setTransId(node.getAttributes().getNamedItem("PanelId").getNodeValue());
                }
                reportList.add(parsedata);
            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                PanelDataParentToChildIteration(nodeList1, reportList);
            }
        }
        return reportList;
    }

    public List<DashBoardData> ParseDashBoardInfoData(NodeList nodeList, List<DashBoardData> dBoardInfoList) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("DFData")) {
                DashBoardData data1 = new DashBoardData("Totals", node.getAttributes().getNamedItem("TotalAmt").getNodeValue(), R.mipmap.totals);
                DashBoardData data2 = new DashBoardData("Collected", node.getAttributes().getNamedItem("CollectedAmt").getNodeValue(), R.mipmap.collected);
                DashBoardData data3 = new DashBoardData("Tobe Collected", node.getAttributes().getNamedItem("ToBeCollectedAmt").getNodeValue(), R.mipmap.tobecollected);
                dBoardInfoList.add(data1);
                dBoardInfoList.add(data2);
                dBoardInfoList.add(data3);
                break;
            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                ParseDashBoardInfoData(nodeList1, dBoardInfoList);
            }
        }
        return dBoardInfoList;
    }

    public DailyFinanceData ParseServerInfoData(NodeList nodeList, DailyFinanceData infoData) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("ServerData")) {
                if(node.getAttributes().getNamedItem("Date")!=null)
                {
                    infoData.setServerDate(node.getAttributes().getNamedItem("Date").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("Time")!=null)
                {
                    infoData.setServerTime(node.getAttributes().getNamedItem("Time").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("TransId")!=null)
                {
                    infoData.setTransId(node.getAttributes().getNamedItem("TransId").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("VN")!=null)
                {
                    infoData.setVN(Integer.parseInt(node.getAttributes().getNamedItem("VN").getNodeValue()));
                }

            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                ParseServerInfoData(nodeList1, infoData);
            }
        }
        return infoData;
    }

    public DailyFinanceData ParseCollectionSearchData(NodeList nodeList, DailyFinanceData infoData) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("SearchData")) {
                if(node.getAttributes().getNamedItem("TransId")!=null)
                {
                    infoData.setTransId(node.getAttributes().getNamedItem("TransId").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("Name")!=null) {
                    infoData.setName(node.getAttributes().getNamedItem("Name").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("MobileNo")!=null) {
                    infoData.setMobileNo(node.getAttributes().getNamedItem("MobileNo").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("RefNo")!=null) {
                    infoData.setRefNo(node.getAttributes().getNamedItem("RefNo").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("PerDayAmt")!=null) {
                    infoData.setPerDayAmt(node.getAttributes().getNamedItem("PerDayAmt").getNodeValue());
                }
            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                ParseCollectionSearchData(nodeList1, infoData);
            }
        }
        return infoData;
    }

    public List<ReportData> ParseFinanceReportData(NodeList nodeList, List<ReportData> reportList) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("Data")) {
                ReportData reportData = new ReportData();
                if(node.getAttributes().getNamedItem("Date")!=null)
                {
                    reportData.setDate(node.getAttributes().getNamedItem("Date").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("VSNo")!=null) {
                    reportData.setVSNo(node.getAttributes().getNamedItem("VSNo").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("Name")!=null) {
                    reportData.setName(node.getAttributes().getNamedItem("Name").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("MobileNo")!=null) {
                    reportData.setMobileNo(node.getAttributes().getNamedItem("MobileNo").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("RefNo")!=null) {
                    reportData.setRefNo(node.getAttributes().getNamedItem("RefNo").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("Amount")!=null) {
                    reportData.setAmount(node.getAttributes().getNamedItem("Amount").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("NetAmount")!=null) {
                    reportData.setNetAmount(node.getAttributes().getNamedItem("NetAmount").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("PerDayAmt")!=null) {
                    reportData.setPerDayAmt(node.getAttributes().getNamedItem("PerDayAmt").getNodeValue());
                }
                if(node.getAttributes().getNamedItem("Remarks")!=null) {
                    reportData.setRemarks(node.getAttributes().getNamedItem("Remarks").getNodeValue());
                }
                reportList.add(reportData);
            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                ParseFinanceReportData(nodeList1, reportList);
            }
        }
        return reportList;
    }

    public ResultData ParseSaveData(NodeList nodeList, ResultData infoData) {
        for (Node node : iterable(nodeList))
        {
            String nodeName = node.getNodeName();
            if (nodeName.equals("Data")) {
                if(node.getAttributes().getNamedItem("Result")!=null)
                {
                    infoData.Result = (node.getAttributes().getNamedItem("Result").getNodeValue());
                }
            }
            if (node.hasChildNodes()) {
                NodeList nodeList1 = node.getChildNodes();
                ParseSaveData(nodeList1, infoData);
            }
        }
        return infoData;
    }
}
