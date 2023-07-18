package me.grax.jbytemod.res;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.strobel.decompiler.DecompilerSettings;

import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.decompiler.CFRDecompiler;
import me.grax.jbytemod.decompiler.FernflowerDecompiler;
import me.grax.jbytemod.res.Option.Type;
import me.grax.jbytemod.res.renders.IntegerSliderOptionRenderer;
import me.grax.jbytemod.utils.ErrorDisplay;

/**
 * 配置类
 */
public class Options {
  private static final File propFile = new File(JByteMod.workingDir, JByteMod.configPath);

  private List<Option> importedOptions = new ArrayList<>();
  private final List<Option> defaults = new ArrayList<>(Arrays.asList(
          new Option("sort_methods", false, Type.BOOLEAN),
      new Option("use_rt", false, Type.BOOLEAN),
          new Option("compute_maxs", true, Type.BOOLEAN),
          new Option("select_code_tab", true, Type.BOOLEAN),
      new Option("memory_warning", true, Type.BOOLEAN),
          new Option("python_path", "", Type.STRING),
      new Option("hints", false, Type.BOOLEAN, "editor"),
          new Option("copy_formatted", false, Type.BOOLEAN, "editor"),
      new Option("analyze_errors", true, Type.BOOLEAN, "editor"),
          new Option("simplify_graph", true, Type.BOOLEAN, "graph"),
      new Option("remove_redundant", false, Type.BOOLEAN, "graph"),
          new Option("max_redundant_input", 2, Type.INT, "graph"),
      new Option("decompile_graph", true, Type.BOOLEAN, "graph"),
          new Option("primary_color", "#557799", Type.STRING, "color"),
      new Option("secondary_color", "#995555", Type.STRING, "color"),
          new Option("use_weblaf", true, Type.BOOLEAN, "style")
          ,new OptionBuilder().setName("lastPath").setValue(System.getProperty("user.home")+ "/Desktop").setVisible(false).setOptionRenderer(null).build()
  ,new OptionBuilder().setName("fontSize").setValue(20).setType(Type.INT).setOptionRenderer(new IntegerSliderOptionRenderer(0,20)).build()));

  public Options() {
    initializeDecompilerOptions();
    if (propFile.exists()) {
      JByteMod.LOGGER.log("载入配置中... ");
      try {
        Files.lines(propFile.toPath()).forEach(l -> {
          String[] split = l.split("=");
          String value = (split.length != 2)?"":split[1];
          try {
            importedOptions.add(new Option(split[0], value, Type.STRING,""));
          } catch (Exception e) {
            JByteMod.LOGGER.warn("不能解析行: " + l);
          }
        });
        for (int i = 0; i < importedOptions.size(); i++) {
          Option o1 = importedOptions.get(i);
          Option o2 = defaults.get(i);
          if (o1 == null || o2 == null || find(o2.getName()) == null || findDefault(o1.getName()) == null) {
            JByteMod.LOGGER.warn("配置无法匹配实例,也许它来自旧版本?");
            //覆盖配置
            this.initWithDefaults(true);
            this.save();
            return;
          }
          //一些比较特殊的配置不会被存入文件中
          o1.setVisible(o2.isVisible());
          o1.setType(o2.getType());
          o1.setGroup(o2.getGroup());
          o1.setRenderer(o2.getOptionRenderer());
        }
        if (importedOptions.isEmpty()) {
          JByteMod.LOGGER.warn("无法读取文件,也许为空");
          this.initWithDefaults(false);
          this.save();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      JByteMod.LOGGER.warn("Property File \"" + propFile.getName() + "\" does not exist, creating...");
      this.initWithDefaults(false);
      this.save();
    }
  }

  private void initializeDecompilerOptions() {
    for (Entry<String, Boolean> e : FernflowerDecompiler.options.entrySet()) {
      defaults.add(new Option("ff_" + e.getKey(), e.getValue(), Type.BOOLEAN, "decompiler_fernflower"));
    }
    for (Entry<String, String> e : CFRDecompiler.options.entrySet()) {
      defaults.add(new Option("cfr_" + e.getKey(), Boolean.valueOf(e.getValue()), Type.BOOLEAN, "decompiler_cfr"));
    }
    try {
      DecompilerSettings s = new DecompilerSettings();
      for (Field f : s.getClass().getDeclaredFields()) {
        if (f.getType() == boolean.class) {
          f.setAccessible(true);
          defaults.add(new Option("procyon" + f.getName(), f.getBoolean(s), Type.BOOLEAN, "decompiler_procyon"));
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private void initWithDefaults(boolean keepExisting) {
    if (keepExisting) {
      //如果不存在默认,则添加
      for (Option o : defaults) {
        if (find(o.getName()) == null) {
          importedOptions.add(o);
        }
      }
      //如果默认不存在,则去除
      importedOptions.removeIf(o -> findDefault(o.getName()) == null);
    } else {
      importedOptions.clear();
      importedOptions.addAll(defaults);
    }
  }

  public void save() {
    new Thread(() -> {
      try {
        if (!propFile.exists()) {
          propFile.getParentFile().mkdirs();
          propFile.createNewFile();
          JByteMod.LOGGER.log("配置文件不存在,正在创建");
        }
        PrintWriter pw = new PrintWriter(propFile);
        for (Option o : importedOptions) {
          pw.println(o.getName()+"=" + o.getValue());
        }
        pw.close();
        JByteMod.LOGGER.log("正在存储");
      } catch (Exception e) {
        new ErrorDisplay(e);
      }
    }).start();
  }

  public Option get(String name) {
    Option op = find(name);
    if (op != null) {
      return op;
    }
    JOptionPane.showMessageDialog(null, "Missing option: " + name + "\nRewriting your config file!");
    this.initWithDefaults(false);
    save();
    op = find(name);
    if (op != null) {
      return op;
    }
    throw new RuntimeException("无法找到配置: " + name);
  }

  private Option find(String name) {
    for (Option o : importedOptions) {
      if (o.getName().equalsIgnoreCase(name)) {
          return o;
      }
    }
    return null;
  }

  private Option findDefault(String name) {
    for (Option o : defaults) {
      if (o.getName().equalsIgnoreCase(name)) {
        return o;
      }
    }
    return null;
  }

  public List<Option> getImportedOptions(){
    return importedOptions.stream().filter(Option::isVisible).collect(Collectors.toList());
  }

}
