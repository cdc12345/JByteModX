package me.grax.jbytemod.res.renders;

import me.grax.jbytemod.res.LanguageRes;
import me.grax.jbytemod.res.Option;
import me.grax.jbytemod.res.Options;

import javax.swing.*;

/**
 * e-mail: 3154934427@qq.com
 * 配置渲染
 *
 * @author cdc123
 * @classname MenuRenderer
 * @date 2023/7/18 20:20
 */
@FunctionalInterface
public interface OptionRenderer {
    JMenuItem renderMenu(LanguageRes lr, Option option, Options options);
}
