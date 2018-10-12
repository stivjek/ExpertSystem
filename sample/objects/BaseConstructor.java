package sample.objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Scanner;

public class BaseConstructor {
    private Base base;
    private ObservableList<Base> baseHistory;
    private String errorMess;
    private int error = 0;
    int numOfHypotize = 0;
    int numOfQuestions = 0;

    public BaseConstructor (File file, Charset encoding){
        base = new Base();

        base.setFileName(file.getName());

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        } catch (FileNotFoundException e) {
            error = 1;
            errorMess = e.getMessage();
        }
        String line = null;
        Scanner scanner = null;
        int indexRow = 0;
        int indexCol = 0;

        ObservableList<Hypotize> hypotizes = FXCollections.observableArrayList();
        ObservableList<Question> questions = FXCollections.observableArrayList();
        baseHistory = FXCollections.observableArrayList();

        try {
            while ((line = bufferedReader.readLine()).length() > 0) {
                switch (indexRow){
                    case 0: base.setBaseName(line);
                            break;
                    case 1: base.setBaseAuthor(line);
                            break;
                    case 2: base.setBaseDescription(line);
                            break;
                    default: error = 1;
                             errorMess = "Четвертая файла строка не пустая!";
                }
                indexRow++;
            }
        } catch (IOException e) {
            error = 1;
            errorMess = e.getMessage();
        }

        indexRow = 0;

        try {
            while ((line = bufferedReader.readLine()).length() > 0) {
                switch (indexRow){
                    case 0: base.setHypotizesName(line);
                            break;
                    default: Question question = new Question();
                             question.setId(indexRow);
                             question.setQuestionText(line);
                             questions.add(question);
                             numOfQuestions ++;
                             break;
                }
                indexRow++;
            }
        } catch (IOException e) {
            error = 1;
            errorMess = e.getMessage();
        }

        base.setNumOfQuestions(numOfQuestions);
        base.setQuestionList(questions);
        indexRow = 1;

        try {
            while ((line = bufferedReader.readLine()) !=null) {
                if(line.length()<1)
                {
                    break;
                }
                ObservableList<QuestionForHypotize> questionForHypotizesList = FXCollections.observableArrayList();
                indexCol = 0;
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                Hypotize hypotize = new Hypotize();
                hypotize.setId(indexRow);
                while (indexCol!=2) {
                    scanner.hasNext();
                    String data = scanner.next();
                    if (indexCol == 0)
                        hypotize.setHypotizeName(data);
                    else if (indexCol == 1)
                        hypotize.setpMain(Double.parseDouble(data));
                    indexCol++;
                }
                indexCol = 0;
                QuestionForHypotize questionForHypotizes = null;
                while (scanner.hasNext()) {
                    String data = scanner.next().trim();
                    switch (indexCol){
                        case 0: questionForHypotizes = new QuestionForHypotize();
                                questionForHypotizes.setQuestionId(Integer.parseInt(data));
                                break;
                        case 1: questionForHypotizes.setpPlus(Double.parseDouble(data));
                                break;
                        case 2: questionForHypotizes.setpMinus(Double.parseDouble(data));
                            try {
                                questionForHypotizesList.add(questionForHypotizes.clone());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            indexCol = -1;
                                break;
                    }
                    indexCol++;
                }
                hypotize.setQuestionForHypotizeList(questionForHypotizesList);
                hypotizes.add(hypotize);
                numOfHypotize++;
                indexRow++;
                /*hypotize.getQuestionForHypotizeList().forEach(questionForHypotize -> {
                    System.out.println(hypotize.getHypotizeName()+" "+questionForHypotize.getQuestionId()+" "+questionForHypotize.getpPlus()+" "+questionForHypotize.getpMinus()+"\n");
                });*/
            }
        } catch (IOException e) {
            error = 1;
            errorMess = e.getMessage();
        }

        base.setHypotizeList(hypotizes);
        base.setNumOfHypotizes(numOfHypotize);

        //закрываем наш ридер
        try {
            bufferedReader.close();
        } catch (IOException e) {
            error = 1;
            errorMess = e.getMessage();
        }
        sortHypotize("probability");
        baseHistory.add(base);
    }

    public void sortHypotize(String SortType)
    {
        ObservableList<Hypotize> hypotizeList = base.getHypotizeList();
        switch (SortType)
        {
            case "name": Comparator hypotizeComparatorName = Comparator.comparing(Hypotize::getHypotizeName);
                         hypotizeList.sort(hypotizeComparatorName);
                         break;
            case "probability":  Comparator hypotizeComparatorProbability = Comparator.comparing(Hypotize::getpMain).reversed();
                                 hypotizeList.sort(hypotizeComparatorProbability);
                                 break;
        }
        base.setHypotizeList(hypotizeList);
    }

    public ObservableList<String> getHypotizesStringList ()
    {
        ObservableList<String> hypotizeStringList = FXCollections.observableArrayList();
        ObservableList<Hypotize> hypotizeList = base.getHypotizeList();
        hypotizeList.forEach(hypotize -> {
            String hypotizeString;
            String hClass = "";
            if(hypotize.getTopCalss()!=null){
                if(hypotize.getTopCalss().equals("disable")) {
                    hClass = "---";
                }
            }
            DecimalFormat myFormatterDouble = new DecimalFormat("0.00000");
            String doubleForattedPMain = myFormatterDouble.format(hypotize.getpMain());
            hypotizeString = hClass+"["+doubleForattedPMain+"] "+hypotize.getHypotizeName();
            hypotizeStringList.add(hypotizeString);
        });
        return hypotizeStringList;
    }

    public ObservableList<String> getQuestionStringList ()
    {
        ObservableList<String> questionStringList = FXCollections.observableArrayList();
        ObservableList<Question> questionList = base.getQuestionList();
        questionList.forEach(question -> {
            if(question.getAnswer()==false) {
                String questionString;
                questionString = question.getQuestionText();
                questionStringList.add(questionString);
            }
        });
        return questionStringList;
    }

    public ObservableList<String> getAnswerStringList ()
    {
        ObservableList<String> questionStringList = FXCollections.observableArrayList();
        ObservableList<Question> questionList = base.getQuestionList();
        questionList.forEach(question -> {
            if(question.getAnswer()==true) {
                String questionString;
                questionString = question.getQuestionText() + " ["+question.getAnswerValue()+"]";
                questionStringList.add(questionString);
            }
        });
        return questionStringList;
    }

    public int setAnswer (double value, String questionText)
    {
        if(getQuestionStringList().size()>0) {
            Base nextBase = null;
            try {
                nextBase = base.clone();    //Клонируем текущую базу в новую
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            ObservableList<Question> questionList = nextBase.getQuestionList();
            ObservableList<Hypotize> hypotizeList = nextBase.getHypotizeList();
            questionList.forEach(question -> {
                if (question.getQuestionText().equals(questionText)) {  //Если гипотеза равна текущей
                    question.setAnswer(true);   //Флаг, что вопрос отвечен
                    question.setAnswerValue(value); //Значение ответа пользователя
                    hypotizeList.forEach(hypotize -> hypotize.getQuestionForHypotizeList().forEach(questionForHypotize -> {
                        if(questionForHypotize.getQuestionId()==question.getId()){  //Если id вопроса равен текущему
                            double phe = 0;
                            double newpMain = 0;
                            if (value>0) {
                                phe = (questionForHypotize.getpPlus()*hypotize.getpMain()) /
                                        (questionForHypotize.getpPlus()*hypotize.getpMain() +
                                                questionForHypotize.getpMinus()*(1-hypotize.getpMain()));   //Подсчет нового P
                                newpMain = hypotize.getpMain() + ((value - 0)*(phe-hypotize.getpMain()))/(1-0);
                            }
                            else if (value<0) {
                                phe = ((1-questionForHypotize.getpPlus())*hypotize.getpMain()) /
                                        (((1-questionForHypotize.getpPlus())*hypotize.getpMain()) +
                                                ((1-questionForHypotize.getpMinus())*(1-hypotize.getpMain())));
                                newpMain = phe + ((value - (-1))*(hypotize.getpMain()-phe)) / (0-(-1));
                            }
                            else {
                                newpMain = hypotize.getpMain();
                            }
                            if(newpMain<0.00001)
                            {
                                hypotize.setTopCalss("disable");
                            }
                            hypotize.setpMain(newpMain); //Устанавливаем новое P гипотезе в Double
                        }
                    }));
                }
            });
            nextBase.setQuestionList(questionList);
            nextBase.setHypotizeList(hypotizeList);
            baseHistory.add(nextBase);  //Добавляем новую базу в лист истории базы
            base = nextBase;    //Устанавливаем текущую базу новой
        }
        if (getQuestionStringList().size()==0)  //Если больше нет не отвеченных вопросов, то -1 вернуть
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    public void backAnswer ()   //Вернуть предыдущее состояние системы
    {
        baseHistory.remove(baseHistory.size()-1); //Удалеяем текущий вариант базы из истории
        base = baseHistory.get(baseHistory.size()-1); //Устанавливаем текущую базу в виде последнего объекта в истории (пред. состояние)
    }

    public void resetQuestions ()
    {
        try {
            base = baseHistory.get(0).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        baseHistory.removeAll();
        baseHistory.add(base);
    }

    public String makeResultString ()
    {
        final String[] resultString = {"*** Файл: \""+getBase().getFileName()+"\" (Гипотез: "
                +getBase().getNumOfHypotizes()+", Параметров: "
                +getBase().getNumOfQuestions()+") ***\n" +
                "###########################################################################\n" +
                "--- "+base.getBaseName()+" --- \n" +
                "--- "+base.getBaseAuthor()+" ---\n"+
                "--- "+base.getBaseDescription()+" ---\n" +
                "###########################################################################\n"};
        final int[] baseIndex = {0};

        baseHistory.forEach(baseFile -> {
            if (baseIndex[0]==0)
            {
                resultString[0] += "Начальное состояние:\n" +
                        "Гипотезы:\n" +
                        "===========================================================================\n";
                baseFile.getHypotizeList().forEach(hypotize -> {
                    resultString[0] += "["+hypotize.getpMain()+"] "+hypotize.getHypotizeName()+"\n";
                });
                resultString[0] += "Параметры:\n" +
                        "===========================================================================\n";
                baseFile.getQuestionList().forEach(question -> {
                    resultString[0] += question.getQuestionText() + "\n";
                });
                resultString[0] += "###########################################################################\n";
            }
            else
            {
                resultString[0] += baseFile.getQuestionList().get(baseIndex[0]).getQuestionText() + "\n" +
                        "===========================================================================\n";
                baseFile.getHypotizeList().forEach(hypotize -> {
                    resultString[0] += "["+hypotize.getpMain()+"] "+hypotize.getHypotizeName()+"\n";
                });
                resultString[0] += "###########################################################################\n";
            }
            baseIndex[0]++;
        });
        return resultString[0];
    }

    public int getBaseHistorySize()
    {
        return baseHistory.size();
    }

    public String getErrorMess() {
        return errorMess;
    }

    public int getError() {
        return error;
    }

    public Base getBase() {
        return base;
    }
}