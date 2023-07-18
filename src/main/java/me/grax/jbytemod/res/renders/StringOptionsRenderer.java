package me.grax.jbytemod.res.renders;

import me.grax.jbytemod.res.LanguageRes;
import me.grax.jbytemod.res.Option;
import me.grax.jbytemod.res.Options;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * e-mail: 3154934427@qq.com
 * 文本渲染
 *
 * @author cdc123
 * @classname StringOptionsRenderer
 * @date 2023/7/18 20:33
 */
public class StringOptionsRenderer implements OptionRenderer{
    @Override
    public JMenuItem renderMenu(LanguageRes lr, Option op, Options options) {
        JMenu jm = new JMenu(lr.getResource(op.getName()));
        JTextField jtf = new JTextField(op.getString());
        jtf.setPreferredSize(new Dimension(Math.max((int) jtf.getPreferredSize().getWidth(), 128),
                (int) jtf.getPreferredSize().getHeight()));
        jm.add(Box.createHorizontalGlue());
        jm.add(jtf);
        jtf.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                op.setValue(jtf.getText());
                options.save();
            }
        });
        return jm;
    }
}
