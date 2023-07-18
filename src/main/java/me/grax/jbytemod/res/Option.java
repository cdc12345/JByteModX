package me.grax.jbytemod.res;

import me.grax.jbytemod.res.renders.BooleanOptionRenderer;
import me.grax.jbytemod.res.renders.IntegerOptionsRenderer;
import me.grax.jbytemod.res.renders.OptionRenderer;
import me.grax.jbytemod.res.renders.StringOptionsRenderer;

public class Option {

  private String name;
  private String group;

  private Object value;
  private Type type;

  private boolean visible = true;

  private OptionRenderer optionRenderer;

  public Option(String name, Object value, Type type) {
    this(name, value, type, "general");
  }

  public Option(String name, Object value, Type type, String group) {
    this.name = name;
    this.value = value;
    this.type = type;
    this.group = group;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean getBoolean() {
    return Boolean.parseBoolean(getString());
  }

  public String getString() {
    return value.toString();
  }

  public int getInteger() {
    return Integer.parseInt(getString());
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setRenderer(OptionRenderer optionRenderer){
    this.optionRenderer = optionRenderer;
  }

  public OptionRenderer getOptionRenderer(){
    return (optionRenderer == null )? type.optionRenderer:optionRenderer;
  }

  public enum Type {
    BOOLEAN(new BooleanOptionRenderer()), STRING(new StringOptionsRenderer()), INT(new IntegerOptionsRenderer());

    private final OptionRenderer optionRenderer;
    Type(OptionRenderer renderer){
      this.optionRenderer = renderer;
    }
  }
}
