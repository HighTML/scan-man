package com.hightml.scanman.value;

/**
 * Please enter description here.
 * <p/>
 * User: marcel
 * Date: 07/02/15
 * Time: 23:54
 * <p/>
 * Copyright by HighTML.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Category {
    private List<Category> childCategories;
    private Category parent;
    private String displayName;
    private String explanation;
    private String code;

    public static List<Category> readFromXML(String fileName) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(fileName);

        List<Category> categories = new ArrayList<Category>();

        try {

            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List<Element> list = rootNode.getChildren("category");

            categories = processCategories(list);


        } catch (IOException io) {
            log.error("During reading XML", io);
        } catch (JDOMException jdomex) {
            log.error("During reading XML", jdomex);

        }

        return categories;
    }

    private static List<Category> processCategories(List<Element> list) {
        List<Category> categories = new ArrayList<Category>();
        for (int i = 0; i < list.size(); i++) {
            Category category = processCategory(list.get(i));
            categories.add(category);
        }
    }

    private static Category processCategory(Element node) {
        Category category = new Category();
        category.setDisplayName(node.getChildText("display-name"));
        category.setCode(node.getChildText("code"));
        category.setExplanation(node.getChildText("explanetion"));

        List<Element> list = node.getChildren("category");
        if (list != null) {
            category.setChildCategories(processCategories(list));
        }

        return category;
    }


}

