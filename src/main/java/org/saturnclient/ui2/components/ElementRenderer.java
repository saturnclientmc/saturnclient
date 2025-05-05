package org.saturnclient.ui2.components;

import java.util.List;

import org.saturnclient.ui2.Element;

public interface ElementRenderer {
    List<Element> getChildren();
    void draw(Element element);
}
