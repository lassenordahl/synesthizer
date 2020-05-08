package com.cs122b.parser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class BaseParser {
    BaseParser() {

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