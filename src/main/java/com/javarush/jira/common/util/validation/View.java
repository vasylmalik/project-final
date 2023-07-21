package com.javarush.jira.common.util.validation;

import jakarta.validation.groups.Default;

public class View {
    public interface OnCreate extends Default {
    }

    public interface OnUpdate extends Default {
    }
}
