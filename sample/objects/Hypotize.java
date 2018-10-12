package sample.objects;

import javafx.collections.ObservableList;

public class Hypotize implements Cloneable{
    private int id;
    private String hypotizeName;
    private double pMain;
    private String topCalss;
    private ObservableList<QuestionForHypotize> questionForHypotizeList;

    @Override
    public Hypotize clone () throws CloneNotSupportedException {
        Hypotize clone = (Hypotize)super.clone();
        return clone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHypotizeName() {
        return hypotizeName;
    }

    public void setHypotizeName(String hypotizeName) {
        this.hypotizeName = hypotizeName;
    }

    public double getpMain() {
        return pMain;
    }

    public void setpMain(double pMain) {
        this.pMain = pMain;
    }

    public String getTopCalss() {
        return topCalss;
    }

    public void setTopCalss(String topCalss) {
        this.topCalss = topCalss;
    }

    public ObservableList<QuestionForHypotize> getQuestionForHypotizeList() {
        return questionForHypotizeList;
    }

    public void setQuestionForHypotizeList(ObservableList<QuestionForHypotize> questionForHypotizeList) {
        this.questionForHypotizeList = questionForHypotizeList;
    }
}
