package sample.objects;

public class Question implements Cloneable{
    private int id;
    private Boolean isAnswer = false;
    private double answerValue;
    private String questionText;

    @Override
    public Question clone () throws CloneNotSupportedException {
        Question clone = (Question) super.clone();
        return clone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }

    public double getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(double answerValue) {
        this.answerValue = answerValue;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
