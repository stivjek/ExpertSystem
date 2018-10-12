package sample.objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Base implements Cloneable{
    private String baseName;
    private String baseAuthor;
    private String baseDescription;
    private String fileName;
    private String hypotizesName;
    private int numOfHypotizes;
    private int numOfQuestions;
    private ObservableList<Hypotize> HypotizeList;
    private ObservableList<Question> QuestionList;
    private String sortType;

    @Override
    public Base clone () throws CloneNotSupportedException {
        Base clone = (Base)super.clone();
        ObservableList<Hypotize> cloneHypotizeList = FXCollections.observableArrayList();
        HypotizeList.forEach(hypotize -> {
            try {
                cloneHypotizeList.add(hypotize.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
        ObservableList<Question> cloneQuestionList = FXCollections.observableArrayList();
        QuestionList.forEach(question -> {
            try {
                cloneQuestionList.add(question.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
        clone.setHypotizeList(cloneHypotizeList);
        clone.setQuestionList(cloneQuestionList);
        return clone;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseAuthor() {
        return baseAuthor;
    }

    public void setBaseAuthor(String baseAuthor) {
        this.baseAuthor = baseAuthor;
    }

    public String getBaseDescription() {
        return baseDescription;
    }

    public void setBaseDescription(String baseDescription) {
        this.baseDescription = baseDescription;
    }

    public String getHypotizesName() {
        return hypotizesName;
    }

    public void setHypotizesName(String hypotizesName) {
        this.hypotizesName = hypotizesName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumOfHypotizes() {
        return numOfHypotizes;
    }

    public void setNumOfHypotizes(int numOfHypotizes) {
        this.numOfHypotizes = numOfHypotizes;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public ObservableList<Hypotize> getHypotizeList() {
        return HypotizeList;
    }

    public void setHypotizeList(ObservableList<Hypotize> hypotizeList) {
        HypotizeList = hypotizeList;
    }

    public ObservableList<Question> getQuestionList() {
        return QuestionList;
    }

    public void setQuestionList(ObservableList<Question> questionList) {
        QuestionList = questionList;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }
}
