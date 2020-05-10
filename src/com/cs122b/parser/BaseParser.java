package com.cs122b.parser;

import java.util.HashSet;

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

    void addDupSet(String identifier) {
        this.dupSet.add(identifier);
    }

    Boolean isDuplicate(String identifier, String toString) {
        if (this.dupSet.contains(identifier)) {
            this.dupCount++;
            this.report.append("[Duplicate] ");
            this.report.append(toString + "\n");
        }
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

    String generateReport(String type, int[] rows) {
        this.report.append("----------------------------------");
        this.report.append("------------Statistics------------");
        this.report.append("----------------------------------");
        this.report.append(String.format("%ss Parsed: %d", type, rows.length));
        this.report.append(String.format("%ss Inserted: %d", type, this.getSuccessCount(rows)));
        this.report.append(String.format("%ss Duplicates: %d", type, this.dupCount));
        this.report.append(String.format("%ss Missing Data: %d", type, this.missingDataCount));
        return this.report.toString();
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

}