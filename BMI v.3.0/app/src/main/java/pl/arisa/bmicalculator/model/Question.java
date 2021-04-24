package pl.arisa.bmicalculator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = -1310138642534128712L;

    private String name;


    private List<Answer> answersList = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@JsonProperty("answers")
    public List<Answer> getAnswersList() {
        return answersList;
    }

    //@JsonProperty("answers")
    public void setAnswersList(List<Answer> answersList) {
        this.answersList = answersList;
    }
}
