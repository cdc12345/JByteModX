package me.grax.jbytemod.res.renders;

import me.grax.jbytemod.res.LanguageRes;
import me.grax.jbytemod.res.Option;
import me.grax.jbytemod.res.Options;
import me.grax.jbytemod.ui.dialogue.ClassDialogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * e-mail: 3154934427@qq.com
 * 数字option
 *
 * @author cdc123
 * @classname IntegerOptionsRenderer
 * @date 2023/7/18 20:35
 */
public class IntegerOptionsRenderer implements OptionRenderer{
    @Override
    public JMenuItem renderMenu(LanguageRes lr, Option op, Options options) {
        JMenu jm = new JMenu(lr.getResource(op.getName()));
        JFormattedTextField jnf = ClassDialogue.createNumberField(Integer.class, 0, Integer.MAX_VALUE);
        jnf.setValue(op.getInteger());
        jnf.setPreferredSize(new Dimension(Math.max((int) jnf.getPreferredSize().getWidth(), 64),
                (int) jnf.getPreferredSize().getHeight()));
        jm.add(jnf);
        jnf.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                op.setValue((int) jnf.getValue());
                options.save();
            }
        });
        return jm;
    }
}
