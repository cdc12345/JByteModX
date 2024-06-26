package me.grax.jbytemod.res;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alee.laf.WebLookAndFeel;

import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.utils.ErrorDisplay;

public class LanguageRes {
  private final HashMap<String, String> map = new HashMap<>();
  private final HashMap<String, String> defaultMap = new HashMap<>();

  public LanguageRes() {
    JByteMod.LOGGER.log("读取语言文件..");
    JByteMod.LOGGER.log("正在读取语言文件"+getLanguage());
    this.readXML(map, getXML());
    this.readXML(defaultMap, LanguageRes.class.getResourceAsStream("/locale/en.xml"));
    JByteMod.LOGGER.log("成功读取" + map.size() + "个本地化键和" + defaultMap.size() + "个默认键");
    this.fixUnicodeSupport();
  }

  private void fixUnicodeSupport() {
    for (String translation : map.values()) {
      for (char c : translation.toCharArray()) {
        if (!WebLookAndFeel.globalControlFont.canDisplay(c)) {
          WebLookAndFeel.globalControlFont = fixFont(WebLookAndFeel.globalControlFont);
          WebLookAndFeel.globalTooltipFont = fixFont(WebLookAndFeel.globalTooltipFont);
          WebLookAndFeel.globalAlertFont = fixFont(WebLookAndFeel.globalAlertFont);
          WebLookAndFeel.globalMenuFont = fixFont(WebLookAndFeel.globalMenuFont);
          WebLookAndFeel.globalAcceleratorFont = fixFont(WebLookAndFeel.globalAcceleratorFont);
          WebLookAndFeel.globalTitleFont = fixFont(WebLookAndFeel.globalTitleFont);
          WebLookAndFeel.globalTextFont = fixFont(WebLookAndFeel.globalTextFont);
          JByteMod.LOGGER.log("Updated WebLaF fonts for unicode support");
          return;
        }
      }
    }
    JByteMod.LOGGER.log("Unicode check finished!");
  }

  private Font fixFont(Font font) {
    return new Font(null, font.getStyle(), font.getSize());
  }

  public String getResource(String desc) {
    return map.getOrDefault(desc, defaultMap.getOrDefault(desc, desc));
  }

  private void readXML(Map<String, String> m, InputStream is) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      Element resources = doc.getDocumentElement();
      NodeList nodes = resources.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node e = nodes.item(i);
        if (e.getNodeName().equals("string")) {
          Element el = (Element) e;
          m.put(el.getAttribute("name"), e.getTextContent());
        }
      }
    } catch (Exception e) {
      JByteMod.LOGGER.err("无法载入资源: " + e.getMessage());
      e.printStackTrace();
      new ErrorDisplay(e);
    }
  }

  private InputStream getXML() {
    InputStream is = LanguageRes.class.getResourceAsStream("/locale/" + this.getLanguage() + ".xml");
    if (is == null) {
      JByteMod.LOGGER.warn("语言无法找到,正在使用默认");
      is = LanguageRes.class.getResourceAsStream("/locale/en.xml");
      if (is == null) {
        JByteMod.LOGGER.err("en.xml无法找到!");
      }
    }
    return is;
  }

  private String getLanguage() {
    return System.getProperty("user.language").replace('_','-');
  }
}
