package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import sample.objects.BaseConstructor;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

public class Controller {
    public Button btn_open;
    public Button btn_start;
    public Button btn_stop;
    public Button btn_back;
    public Button btn_save;
    public Label top_label;
    public ListView hypotize_list;
    public Label base_name;
    public Label base_author;
    public Label base_description;
    public ListView answer_list;
    public TextArea main_text;
    public RadioButton hypotize_sort_name;
    public RadioButton hypotize_sort_probability;
    public ListView questions_list;
    public Slider anwer_scroll;
    public TextField base_hypotize_name;
    public TextField value_of_answer;
    public ChoiceBox<String> codeText;
    public Button okAnswer;

    public Charset encodingText;
    public BaseConstructor baseConstructor;
    private DoubleProperty value_of_answer_double;

    public void initialize(){
        ObservableList<String> encodingList = FXCollections.observableArrayList("CP1251", "UTF-8")
                ;
        value_of_answer_double = new SimpleDoubleProperty();
        value_of_answer.textProperty().bindBidirectional(value_of_answer_double, new NumberStringConverter());

        codeText.setItems(encodingList);
        codeText.setValue("CP1251");
        encodingText = Charset.forName("CP1251");

        codeText.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue)
                        -> encodingText = Charset.forName(newValue));

        anwer_scroll.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                DecimalFormat myFormatter = new DecimalFormat("#.##");
                String answer_value_formated = myFormatter.format(anwer_scroll.getValue());
                value_of_answer.setText(answer_value_formated);
            }
        });
    }

    @FXML
    public void fileOpen(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("MKB files (*.mkb)", "*.mkb");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            //Open
            main_text.setText("Процесс открытия файла");
        }
        baseConstructor = new BaseConstructor(file, encodingText);
        if (baseConstructor.getError()==1)
        {
            main_text.setText(baseConstructor.getErrorMess());
            return;
        }
        initBase();
        btn_start.setDisable(false);
        btn_stop.setDisable(true);
        btn_back.setDisable(true);
        btn_save.setDisable(false);
        okAnswer.setDisable(true);
        hypotize_sort_name.setDisable(false);
        hypotize_sort_probability.setDisable(false);
        main_text.setText("Можно начинать опрос");
    }

    public void startSystem(ActionEvent actionEvent) {
        okAnswer.setDisable(false);
        btn_start.setDisable(true);
        btn_stop.setDisable(false);
        getNextQuestion();
    }

    public void stopSystem(ActionEvent actionEvent) {
        baseConstructor.resetQuestions();
        initBase();
        btn_back.setDisable(true);
        btn_stop.setDisable(true);
        okAnswer.setDisable(true);
        btn_start.setDisable(false);
        main_text.setText("Вы остановили опрос. Можно начинать заного");
    }

    public void backSystem(ActionEvent actionEvent) {
        baseConstructor.backAnswer();
        initBase();
        getNextQuestion();
        if(baseConstructor.getQuestionStringList().size()>0)
        {
            okAnswer.setDisable(false);
        }
        else
        {
            okAnswer.setDisable(true);
        }
    }

    @FXML
    public void saveSystem(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Save Document");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(node.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            //Open
            main_text.setText("Процесс сохранения файла");
        }

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.write(baseConstructor.makeResultString());
            bufferedWriter.flush();
        } catch (IOException e) {
            main_text.setText(e.getMessage());
        }
        /*
        try(FileWriter writer = new FileWriter(file, false))
        {
            writer.write(baseConstructor.makeResultString());
            writer.flush();
        }
        catch(IOException ex){
            main_text.setText(ex.getMessage());
        }
        main_text.setText("Файл сохранен");
        */
    }

    public void sortName(ActionEvent actionEvent) {
        baseConstructor.sortHypotize("name");
        initBase();
    }

    public void sortProbability(ActionEvent actionEvent) {
        baseConstructor.sortHypotize("probability");
        initBase();
    }

    public void answerOk(ActionEvent actionEvent) {
        if(baseConstructor.setAnswer(value_of_answer_double.get(), main_text.getText())==0) {
            initBase();
            getNextQuestion();
        }
        else {
            initBase();
            main_text.setText("Конец опроса");
            okAnswer.setDisable(true);
        }
    }

    private void initBase()
    {
        if(hypotize_sort_probability.isSelected())
        {
            baseConstructor.sortHypotize("probability");
        }
        else
        {
            baseConstructor.sortHypotize("name");
        }
        top_label.setText("Файл: \""+baseConstructor.getBase().getFileName()+"\" (Гипотез: "
                +baseConstructor.getBase().getNumOfHypotizes()+", Параметров: "
                +baseConstructor.getBase().getNumOfQuestions()+")");
        base_name.setText(baseConstructor.getBase().getBaseName());
        base_author.setText(baseConstructor.getBase().getBaseAuthor());
        base_description.setText(baseConstructor.getBase().getBaseDescription());
        base_hypotize_name.setText(baseConstructor.getBase().getHypotizesName());
        hypotize_list.setItems(baseConstructor.getHypotizesStringList());
        questions_list.setItems(baseConstructor.getQuestionStringList());
        questions_list.scrollTo(0);
        answer_list.setItems(baseConstructor.getAnswerStringList());
        anwer_scroll.setValue(0);
        if (baseConstructor.getBaseHistorySize()<2)
        {
            btn_back.setDisable(true);
        }
        else {
            btn_back.setDisable(false);
        }
    }

    private void getNextQuestion()
    {
        main_text.setText(questions_list.getItems().get(0).toString());
        questions_list.getItems().remove(0);
    }
}