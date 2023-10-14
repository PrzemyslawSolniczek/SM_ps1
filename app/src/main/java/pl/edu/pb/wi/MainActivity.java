package pl.edu.pb.wi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    // indeksy i liczniki
    private int currentIndex = 0;
    private int correctAnswersCount = 0;
    private int currentQuestionIndex = 0;
    // Stała dla opóźnienia resetowania gry
    private static final int RESET_DELAY = 4000;

    // elementy wyświetlane w interfejsie
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;

    // tablica z pytaniami ze strings pytań
    private Question[] questions = new Question[]{
            new Question(R.string.q_1, true),
            new Question(R.string.q_2, true),
            new Question(R.string.q_3, true),
            new Question(R.string.q_4, true),
            new Question(R.string.q_5, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // przypisanie elementów interfejsu użytkownika
        questionTextView = findViewById(R.id.question_text_view);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        showCurrentQuestion();

        // ustawienie nasłuchu dla przycisków
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // przejście do następnego pytania lub powrót do pierwszego gdy to ostatnie pytanie
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.length;
                showCurrentQuestion();
            }
        });
    }

    // ustawienie tekstu dla bieżącego pytania
    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    // klasa reprezentująca pojedyncze pytanie
    public class Question {
        private int questionId;
        private boolean trueAnswer;

        public Question(int questionId, boolean trueAnswer) {
            this.questionId = questionId;
            this.trueAnswer = trueAnswer;
        }

        public int getQuestionId() {
            return questionId;
        }

        public boolean getTrueAnswer() {
            return trueAnswer;
        }
    }

    // sprawdzenie poprawności odpowiedzi użytkownika
    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentQuestionIndex].getTrueAnswer();
        int messageId;
        if (userAnswer == correctAnswer) {
            messageId = R.string.correct_answer;
            correctAnswersCount++;  // zwiększenie licznika poprawnych odpowiedzi jesli jest dobre
        } else {
            messageId = R.string.incorrect_answer;
        }
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();

        // jeśli to ostatnie pytanie, wyświetl wynik
        if (currentQuestionIndex == questions.length - 1) {
            displayFinalScore();

            // opóźnienie przed resetowaniem gry
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            }, RESET_DELAY);
        }
    }

    // wyświetlenie końcowego wyniku użytkownika
    private void displayFinalScore() {
        String message = String.format("Uzyskałeś %d z %d poprawnych odpowiedzi!", correctAnswersCount, questions.length);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // aktualizacja widoku do bieżącego pytania
    private void showCurrentQuestion() {
        questionTextView.setText(questions[currentQuestionIndex].getQuestionId());
    }

    // resetowanie gry
    private void resetGame() {
        currentQuestionIndex = 0;
        correctAnswersCount = 0;
        showCurrentQuestion();
    }
}