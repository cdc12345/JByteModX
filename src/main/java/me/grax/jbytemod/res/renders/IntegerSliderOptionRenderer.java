package me.grax.jbytemod.res.renders;

import me.grax.jbytemod.res.LanguageRes;
import me.grax.jbytemod.res.Option;
import me.grax.jbytemod.res.Options;

import javax.swing.*;

/**
 * e-mail: 3154934427@qq.com
 * 整数型滑块渲染
 *
 * @author cdc123
 * @classname IntegerSliderOptionRenderer
 * @date 2023/7/18 20:43
 */
public class IntegerSliderOptionRenderer implements OptionRenderer{

    private final int min;
    private final int max;
    public IntegerSliderOptionRenderer(int min,int max){
        this.max = max;
        this.min = min;
    }

    @Override
    public JMenuItem renderMenu(LanguageRes lr, Option option, Options options) {
        JMenuItem jm = new JMenu(lr.getResource("fontSize"));
        jm.add(Box.createHorizontalGlue());
        JSlider jSlider = new JSlider();
        jSlider.setValue(option.getInteger());
        jSlider.setMaximum(max);
        jSlider.setMinimum(min);
        jSlider.addChangeListener(a-> {
            option.setValue(jSlider.getValue());
            options.save();
        });
        jm.add(jSlider);
        return jm;
    }
}
