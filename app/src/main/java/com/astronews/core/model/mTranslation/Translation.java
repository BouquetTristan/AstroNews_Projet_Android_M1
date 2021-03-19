package com.astronews.core.model.mTranslation;

import java.util.List;

public class Translation {
   //private Detection detected;
   private List<String> text;
   private String lang;
   private int code;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return text.get(0);
    }
}

