package org.panda_lang.pandomium.settings.categories;

import java.util.ArrayList;
import java.util.List;

public class CommandLineSettings {

    private final List<String> arguments;

    public CommandLineSettings() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(String argument) {
        arguments.add(argument);
    }

    public String[] getArguments() {
        String[] args = new String[arguments.size()];
        return arguments.toArray(args);
    }

}
