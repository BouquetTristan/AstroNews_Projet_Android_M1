package com.astronews.core.model.News;

import java.util.List;

public class ArticleCollection {
    private String type;
    private String didUMean;
    private int totalCount;
    private List<Article> value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDidUMean() {
        return didUMean;
    }

    public void setDidUMean(String didUMean) {
        this.didUMean = didUMean;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Article> getValue() {
        return value;
    }

    public void setValue(List<Article> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String res =  totalCount + " articles trouvés";
        for( int i = 0; i < value.size(); i++) {
            res += "\n\t" + value.get(i).toString();
        }
        return res;
    }
}
