package me.grax.jbytemod.res;

import me.grax.jbytemod.res.renders.OptionRenderer;

/**
 * e-mail: 3154934427@qq.com
 * 构建者
 *
 * @author cdc123
 * @classname OptionBuilder
 * @date 2023/7/18 19:38
 */
public class OptionBuilder {
    private final Option result;

    /**
     * 默认为 new Option("","", Option.
     */
    public OptionBuilder(){
        result = new Option("","", Option.Type.STRING);
    }

    public OptionBuilder setName(String name){
        result.setName(name);
        return this;
    }

    public OptionBuilder setValue(Object value){
        result.setValue(value);
        return this;
    }

    public OptionBuilder setType(Option.Type type){
        result.setType(type);
        return this;
    }

    public OptionBuilder setVisible(boolean visible){
        result.setVisible(visible);
        return this;
    }

    public OptionBuilder setOptionRenderer(OptionRenderer renderer){
        result.setRenderer(renderer);
        return this;
    }

    public OptionBuilder setGroup(String group){
        result.setGroup(group);
        return this;
    }

    public Option build(){
        return result;
    }
}
