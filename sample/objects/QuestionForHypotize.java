package sample.objects;

public class QuestionForHypotize implements Cloneable{
    private int questionId;
    private double pPlus;
    private double pMinus;

    @Override
    public QuestionForHypotize clone() throws CloneNotSupportedException {
        return (QuestionForHypotize)super.clone();
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public double getpPlus() {
        return pPlus;
    }

    public void setpPlus(double pPlus) {
        this.pPlus = pPlus;
    }

    public double getpMinus() {
        return pMinus;
    }

    public void setpMinus(double pMinus) {
        this.pMinus = pMinus;
    }
}
