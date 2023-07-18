package me.grax.jbytemod.res.renders;

import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.res.LanguageRes;
import me.grax.jbytemod.res.Option;
import me.grax.jbytemod.res.Options;

import javax.swing.*;

/**
 * e-mail: 3154934427@qq.com
 * 布尔渲染
 *
 * @author cdc123
 * @classname BooleanMenuRenderer
 * @date 2023/7/18 20:27
 */
public class BooleanOptionRenderer implements OptionRenderer {
    @Override
    public JMenuItem renderMenu(LanguageRes lr, Option op, Options options) {
        JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(lr.getResource(op.getName()), op.getBoolean());
        jmi.addActionListener(e -> {
            op.setValue(jmi.isSelected());
            options.save();
            if (op.getName().equals("use_weblaf")) {
                JByteMod.resetLAF();
                JByteMod.restartGUI();
            }
        });
        return jmi;
    }
}
