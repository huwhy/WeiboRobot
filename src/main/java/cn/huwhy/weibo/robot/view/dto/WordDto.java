package cn.huwhy.weibo.robot.view.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WordDto {
    private SimpleIntegerProperty id;
    private SimpleStringProperty word;

    public WordDto() {
    }

    public WordDto(int id, String word) {
        this.id = new SimpleIntegerProperty(id);
        this.word = new SimpleStringProperty(word);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }
}
