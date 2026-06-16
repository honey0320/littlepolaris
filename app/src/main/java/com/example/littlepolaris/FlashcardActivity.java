package com.example.littlepolaris;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class FlashcardActivity extends AppCompatActivity {

    ImageView imgCard;
    TextView txtMain, txtSub;

    Button btnNext, btnPrevious, btnSpeak, btnBack;

    TextToSpeech tts;

    int currentIndex = 0;

    String[] mainItems;
    String[] subItems;
    int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        imgCard = findViewById(R.id.imgCard);
        txtMain = findViewById(R.id.txtMain);
        txtSub = findViewById(R.id.txtSub);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnBack = findViewById(R.id.btnBack);

        String tempCategory =
                getIntent().getStringExtra("CATEGORY");

        if (tempCategory == null)
            tempCategory = "NUMBERS";

        final String category = tempCategory;

        switch (category){

            case "ALPHABETS":
                loadAlphabets();
                break;

            case "COLORS":
                loadColors();
                break;

            case "SHAPES":
                loadShapes();
                break;

            case "ANIMALS":
                loadAnimals();
                break;

            case "FRUITS":
                loadFruits();
                break;

            case "VEHICLES":
                loadVehicles();
                break;

            default:
                loadNumbers();
        }

        tts = new TextToSpeech(this,
                status -> {
                    if(status != TextToSpeech.ERROR){
                        tts.setLanguage(Locale.US);
                    }
                });

        showCard();

        btnBack.setOnClickListener(v -> finish());

        btnPrevious.setOnClickListener(v -> {

            if(currentIndex > 0){
                currentIndex--;
                showCard();
            }

        });

        btnNext.setOnClickListener(v -> {

            if(currentIndex < mainItems.length - 1){

                currentIndex++;
                showCard();

            }else {

                SharedPreferences sp =
                        getSharedPreferences(
                                "LittlePolaris",
                                MODE_PRIVATE);

                boolean alreadyCompleted =
                        sp.getBoolean(
                                category + "_DONE",
                                false);

                if (!alreadyCompleted) {

                    StarManager.addStars(this, 5);

                    sp.edit()
                            .putBoolean(
                                    category + "_DONE",
                                    true)
                            .apply();

                    Toast.makeText(
                            this,
                            "⭐ Amazing! +5 Stars Earned!",
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    Toast.makeText(
                            this,
                            "Lesson already completed ⭐",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

        });

        btnSpeak.setOnClickListener(v -> {

            String speech =
                    "This is " +
                            txtMain.getText().toString();

            tts.speak(
                    speech,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );
        });
    }

    private void showCard(){

        txtMain.setText(mainItems[currentIndex]);
        txtSub.setText(subItems[currentIndex]);
        imgCard.setImageResource(images[currentIndex]);
    }

    private void loadNumbers(){

        mainItems = new String[]{
                "1","2","3","4","5",
                "6","7","8","9","10"
        };

        subItems = new String[]{
                "One","Two","Three","Four","Five",
                "Six","Seven","Eight","Nine","Ten"
        };

        images = new int[]{
                R.drawable.number1,
                R.drawable.number2,
                R.drawable.number3,
                R.drawable.number4,
                R.drawable.number5,
                R.drawable.number6,
                R.drawable.number7,
                R.drawable.number8,
                R.drawable.number9,
                R.drawable.number10
        };
    }

    private void loadAlphabets(){

        mainItems = new String[]{
                "A","B","C","D","E","F","G","H","I","J",
                "K","L","M","N","O","P","Q","R","S","T",
                "U","V","W","X","Y","Z"
        };

        subItems = new String[]{
                "Apple","Ball","Cat","Dog","Elephant",
                "Fish","Goat","Hat","Ice Cream","Joker",
                "Kite","Lion","Mango","Nest","Owl",
                "Parrot","Queen","Rabbit","Sun","Tiger",
                "Umbrella","Van","Watch","Xylophone",
                "Yak","Zebra"
        };

        images = new int[]{
                R.drawable.apple,
                R.drawable.ball,
                R.drawable.cat,
                R.drawable.dog,
                R.drawable.elephant,
                R.drawable.fish,
                R.drawable.goat,
                R.drawable.hat,
                R.drawable.icecream,
                R.drawable.joker,
                R.drawable.kite,
                R.drawable.lion,
                R.drawable.mango,
                R.drawable.nest,
                R.drawable.owl,
                R.drawable.parrot,
                R.drawable.queen,
                R.drawable.rabbit,
                R.drawable.sun,
                R.drawable.tiger,
                R.drawable.umbrella,
                R.drawable.van,
                R.drawable.watch,
                R.drawable.xylophone,
                R.drawable.yak,
                R.drawable.zebra
        };
    }

    private void loadColors(){

        mainItems = new String[]{
                "Red", "Blue", "Green", "Yellow",
                "Orange", "Purple", "Pink",
                "Black", "White", "Brown"
        };

        subItems = new String[]{
                "Like an Apple 🍎",
                "Like the Sky ☁️",
                "Like Grass 🌱",
                "Like a Banana 🍌",
                "Like an Orange 🍊",
                "Like Grapes 🍇",
                "Like Candy 🍬",
                "Like a Crow 🐦",
                "Like Milk 🥛",
                "Like Chocolate 🍫"
        };

        images = new int[]{
                R.drawable.red,
                R.drawable.blue,
                R.drawable.green,
                R.drawable.yellow,
                R.drawable.orange,
                R.drawable.purple,
                R.drawable.pink,
                R.drawable.black,
                R.drawable.white,
                R.drawable.brown
        };
    }

    private void loadShapes(){

        mainItems = new String[]{
                "Circle",
                "Square",
                "Triangle",
                "Rectangle",
                "Star",
                "Heart",
                "Oval",
                "Diamond"
        };

        subItems = new String[]{
                "A shape with no corners",
                "A shape with 4 equal sides",
                "A shape with 3 sides",
                "A shape with 4 corners",
                "A shining star shape",
                "A shape that means love",
                "A stretched circle",
                "A diamond-like shape"
        };

        images = new int[]{
                R.drawable.circle,
                R.drawable.square,
                R.drawable.triangle,
                R.drawable.rectangle,
                R.drawable.star,
                R.drawable.heart,
                R.drawable.oval,
                R.drawable.diamond
        };
    }

    private void loadAnimals(){

        mainItems = new String[]{
                "Lion",
                "Tiger",
                "Elephant",
                "Dog",
                "Cat",
                "Monkey",
                "Rabbit",
                "Bear",
                "Cow",
                "Horse"
        };

        subItems = new String[]{
                "Roars Loudly",
                "Has Black Stripes",
                "Has A Long Trunk",
                "Says Woof Woof",
                "Says Meow Meow",
                "Loves Bananas",
                "Has Long Ears",
                "Loves Honey",
                "Gives Fresh Milk",
                "Can Carry People"
        };

        images = new int[]{
                R.drawable.lion,
                R.drawable.tiger,
                R.drawable.elephant,
                R.drawable.dog,
                R.drawable.cat,
                R.drawable.monkey,
                R.drawable.rabbit,
                R.drawable.bear,
                R.drawable.cow,
                R.drawable.horse
        };
    }

    private void loadFruits(){

        mainItems = new String[]{
                "Apple",
                "Banana",
                "Orange",
                "Mango",
                "Grapes",
                "Watermelon",
                "Papaya",
                "Pineapple"
        };

        subItems = new String[]{
                "Apple is red",
                "Banana is yellow",
                "Orange is orange",
                "Mango is sweet",
                "Grapes grow in bunches",
                "Watermelon is juicy",
                "Papaya is healthy",
                "Pineapple has a crown"
        };

        images = new int[]{
                R.drawable.apple,
                R.drawable.banana,
                R.drawable.orangef,
                R.drawable.mango,
                R.drawable.grapes,
                R.drawable.watermelon,
                R.drawable.papaya,
                R.drawable.pineapple
        };
    }

    private void loadVehicles(){

        mainItems = new String[]{
                "Car",
                "Bus",
                "Train",
                "Bicycle",
                "Truck",
                "Boat",
                "Helicopter",
                "Aeroplane"
        };

        subItems = new String[]{
                "Car travels on roads",
                "Bus carries many people",
                "Train runs on tracks",
                "Bicycle has two wheels",
                "Truck carries goods",
                "Boat moves on water",
                "Helicopter flies in the sky",
                "Aeroplane travels long distances"
        };

        images = new int[]{
                R.drawable.car,
                R.drawable.bus,
                R.drawable.train,
                R.drawable.bicycle,
                R.drawable.truck,
                R.drawable.boat,
                R.drawable.helicopter,
                R.drawable.aeroplane
        };
    }

    @Override
    protected void onDestroy() {

        if(tts != null){
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}