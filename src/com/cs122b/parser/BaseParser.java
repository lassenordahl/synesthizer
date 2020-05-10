package com.cs122b.parser;

import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.Statement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class BaseParser {
    int dupCount;
    int missingDataCount;
    StringBuilder report;
    Set<String> dupSet;

    BaseParser() {
        this.dupCount = 0;
        this.missingDataCount = 0;
        this.report = new StringBuilder();
        this.dupSet = new HashSet<String>();
    }

    void addToDupSet(String identifier) {
        this.dupSet.add(identifier);
    }

    Boolean isDuplicate(String identifier, String toString) {
        if (this.dupSet.contains(identifier)) {
            this.dupCount++;
            this.report.append("[Duplicate] ");
            this.report.append(toString + "\n");
            return true;
        }
        return false;
    }

    void addMissingData(String toString) {
        this.report.append("[Missing Data] ");
        this.report.append(toString + "\n");
        this.missingDataCount++;
    }

    int getFailCount(int[] rows) {
        int failure = 0;
        for (int row : rows) {
            if (row == Statement.EXECUTE_FAILED)
                failure++;
        }
        return failure;
    }

    int getSuccessCount(int[] rows) {
        int success = 0;
        for (int row : rows) {
            if (row == Statement.SUCCESS_NO_INFO)
                success++;
        }
        return success;
    }

    String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                if (((Element) el.getParentNode()).equals(ele)) {
                    textVal = el.getFirstChild().getNodeValue();
                    break;
                }
            }
        }

        return textVal;
    }

    int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        if (getTextValue(ele, tagName) == null) {
            return 0;
        }
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    float getFloatValue(Element ele, String tagName) {
        if (getTextValue(ele, tagName) == null) {
            return 0;
        }
        return Float.parseFloat(getTextValue(ele, tagName));
    }

    public String generateReport(String type, int inserts, int totalParsed) {
        this.report.append("--------------------------------------------\n");
        this.report.append("-----------------Statistics-----------------\n");
        this.report.append("--------------------------------------------\n");
        this.report.append(String.format("%ss Parsed: %d\n", type, totalParsed));
        this.report.append(String.format("%ss Inserted: %d\n", type, inserts));
        this.report.append(String.format("%ss Duplicates: %d\n", type, this.dupCount));
        this.report.append(String.format("%ss Missing Data: %d\n", type, this.missingDataCount));
        this.report.append("--------------------------------------------\n");
        return this.report.toString();
    }

}