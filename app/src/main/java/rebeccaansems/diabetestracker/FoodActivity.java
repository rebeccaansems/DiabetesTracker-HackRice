package rebeccaansems.diabetestracker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.quickreturn.library.QuickReturnAttacher;
import com.felipecsl.quickreturn.library.widget.QuickReturnAdapter;
import com.felipecsl.quickreturn.library.widget.QuickReturnTargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import static rebeccaansems.diabetestracker.R.layout.food_row_footer;

public class FoodActivity extends AppCompatActivity {
    Toolbar toolbar;
    static boolean IS_SEARCH_VISIBLE; // retain list
    static boolean SEARCH_RETAIN; // retain search toolbar
    private static String STRING_FOOD_SEARCH; // retain editText
    private static int CURRENT_PAGE = 0; // Was used to control page items with CURRENT_PAGE++

    private View mListViewFooter; // ListView Footer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        LayoutInflater inflater = LayoutInflater.from(FoodActivity.this); // 1


        mListViewFooter = inflater.inflate(food_row_footer, null);
        setTitle("Settings");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            IS_SEARCH_VISIBLE = savedInstanceState.getBoolean("IS_SEARCH_VISIBLE");
            STRING_FOOD_SEARCH = savedInstanceState.getString("STRING_FOOD_SEARCH");
            SEARCH_RETAIN = savedInstanceState.getBoolean("SEARCH_RETAIN");
        }

        findViewsById();
        toolbarTop();
        toolbarSearch();
        searchImplementation();
        getImplementation();


    }
    private void updateList() {
        if (mSearchAdapter.getCount() == 0) {
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
    }

    private FatSecretSearch mFatSecretSearch;
    private FatSecretGet mFatSecretGet;
    private InputMethodManager imm;
    private Toolbar mToolbarTop, mToolbarSearch;
    private EditText mSearch;
    private ListView mListView;
    private ProgressBar mProgressMore, mProgressSearch;

    private void findViewsById() {
        mFatSecretSearch = new FatSecretSearch(); // method.search
        mFatSecretGet = new FatSecretGet(); // method.get

        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE); // handle soft keyboard
        mToolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        mToolbarSearch = (Toolbar) findViewById(R.id.toolbar_search);
        mSearch = (EditText) findViewById(R.id.etSearch);
        mListView = (ListView) findViewById(R.id.listView);
        listViewConfigurations();
        mProgressMore = (ProgressBar) mListViewFooter.findViewById(R.id.progressBar);
        mProgressMore.setVisibility(View.INVISIBLE);
        mProgressSearch = (ProgressBar) findViewById(R.id.searchProgress);
        mProgressSearch.setVisibility(View.INVISIBLE);
        updateList();
    }

    private SearchAdapter mSearchAdapter;
    private ArrayList<Food> mItem;
    QuickReturnAttacher mQuickReturnAttacher; // Library: https://github.com/felipecsl/QuickReturn
    RelativeLayout mSlidingLayout;

    private void listViewConfigurations() {
        mListView.addFooterView(mListViewFooter);
        mItem = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(this, mItem);
        mListView.setAdapter(new QuickReturnAdapter(mSearchAdapter));
        mQuickReturnAttacher = QuickReturnAttacher.forView(mListView);
        mSlidingLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mQuickReturnAttacher.addTargetView(mSlidingLayout, QuickReturnTargetView.POSITION_TOP, Equations.dpToPx(this, 55));
    }

    private void toolbarTop() {
        mToolbarTop.setTitle("Search");
        mToolbarTop.inflateMenu(R.menu.menu_main);
        mToolbarTop.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_search) {
                    mToolbarSearch.setVisibility(View.VISIBLE);
                    mToolbarSearch.bringToFront();
                    mSearch.requestFocus();
                    IS_SEARCH_VISIBLE = true;
                    CURRENT_PAGE = 0;
                    imm.toggleSoftInput(0, 0);
                }
                return false;
            }
        });
    }

    private void toolbarSearch() {
        mToolbarSearch.inflateMenu(R.menu.menu_search);
        mToolbarSearch.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back));
        mToolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbarSearch.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                mSearch.setText("");
                mToolbarSearch.getMenu().clear();
                mToolbarSearch.inflateMenu(R.menu.menu_search);
                mToolbarTop.bringToFront();
                mItem.clear();
                mSearchAdapter.notifyDataSetChanged();
                IS_SEARCH_VISIBLE = false;
                CURRENT_PAGE = 0;
                SEARCH_RETAIN = false;
                updateList();
            }
        });
        mToolbarSearch.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.action_voice) {
                    promptSpeechInput();
                }
                if (menuItem == R.id.action_clear) {
                    mSearch.setText("");
                    SEARCH_RETAIN = false;
                }
                return false;
            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    mToolbarSearch.getMenu().clear();
                    mToolbarSearch.inflateMenu(R.menu.menu_search_clear);
                } else {
                    mToolbarSearch.getMenu().clear();
                    mToolbarSearch.inflateMenu(R.menu.menu_search);
                    mItem.clear();
                    mSearchAdapter.notifyDataSetChanged();
                    updateList();
                    CURRENT_PAGE = 0;
                    SEARCH_RETAIN = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchImplementation() {
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchFood(mSearch.getText().toString(), CURRENT_PAGE);
                    mSearch.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void getImplementation() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mItem.size()) { // Should to be refactored
                    getFood(Long.valueOf(mItem.get(position - 1).getID()));
                } else {
                    if (mItem.size() == 20)
                        searchFood(mSearch.getText().toString(), 1);
                    else if (mItem.size() == 40)
                        searchFood(mSearch.getText().toString(), 2);
                    else if (mItem.size() == 60)
                        searchFood(mSearch.getText().toString(), 3);
                    else if (mItem.size() == 80)
                        searchFood(mSearch.getText().toString(), 4);
                    else if (mItem.size() == 100)
                        searchFood(mSearch.getText().toString(), 5);
                    else if (mItem.size() == 120)
                        searchFood(mSearch.getText().toString(), 6);
                    else if (mItem.size() == 140)
                        searchFood(mSearch.getText().toString(), 7);
                    else if (mItem.size() == 160)
                        searchFood(mSearch.getText().toString(), 8);
                    else if (mItem.size() == 180)
                        searchFood(mSearch.getText().toString(), 9);
                    else if (mItem.size() == 200)
                        searchFood(mSearch.getText().toString(), 10);
                }
            }
        });
    }

    /**
     * FatSecret Search
     */
    String brand;

    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                mProgressMore.setVisibility(View.VISIBLE);
                mProgressSearch.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                String food_name = food_items.getString("food_name");
                                String food_description = food_items.getString("food_description");
                                String[] row = food_description.split("-");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                                String food_id = food_items.getString("food_id");
                                mItem.add(new Food(food_name, row[1].substring(1),
                                        "" + brand, food_id));
                            }
                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
                mSearchAdapter.notifyDataSetChanged();
                updateList();
                mProgressMore.setVisibility(View.INVISIBLE);
                mProgressSearch.setVisibility(View.INVISIBLE);
                SEARCH_RETAIN = true;
            }
        }.execute();
    }

    /**
     * FatSecret get
     */
    private void getFood(final long id) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                JSONObject foodGet = mFatSecretGet.getFood(id);
                try {
                    if (foodGet != null) {
                        String food_name = foodGet.getString("food_name");
                        JSONObject servings = foodGet.getJSONObject("servings");

                        JSONObject serving = servings.getJSONObject("serving");
                        String calories = serving.getString("calories");
                        String carbohydrate = serving.getString("carbohydrate");
                        String protein = serving.getString("protein");
                        String fat = serving.getString("fat");
                        String serving_description = serving.getString("serving_description");
                        Log.e("serving_description", serving_description);
                        /**
                         * Displays results in the LogCat
                         */
                        Log.e("food_name", food_name);
                        Log.e("calories", calories);
                        Log.e("carbohydrate", carbohydrate);
                        Log.e("protein", protein);
                        Log.e("fat", fat);
                    }

                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    /*
    Get Method
     Nutrient values for each food item are returned according to the standard serving sizes available. The elements returned for each standard serving size are:

     serving_id – the unique serving identifier.
     serving_description – the full description of the serving size. E.G.: "1 cup" or "100 g".
     serving_url – URL of the serving size for this food item on www.fatsecret.com.
     metric_serving_amount is a Decimal - the metric quantity combined with metric_serving_unit to derive the total standardized quantity of the serving (where available).
     metric_serving_unit – the metric unit of measure for the serving size – either "g" or "ml" or "oz" – combined with metric_serving_amount to derive the total standardized quantity of the serving (where available).
     number_of_units is a Decimal - the number of units in this standard serving size. For instance, if the serving description is "2 tablespoons" the number of units is "2", while if the serving size is "1 cup" the number of units is "1".
     measurement_description – a description of the unit of measure used in the serving description. For instance, if the description is "1/2 cup" the measurement description is "cup", while if the serving size is "100 g" the measurement description is "g".


     The complete nutritional information is returned - see nutrient

     calories is a Decimal – the energy content in kcal.
     carbohydrate is a Decimal – the total carbohydrate content in grams.
     protein is a Decimal – the protein content in grams.
     fat is a Decimal – the total fat content in grams.
     saturated_fat is a Decimal – the saturated fat content in grams (where available).
     polyunsaturated_fat is a Decimal – the polyunsaturated fat content in grams (where available).
     monounsaturated_fat is a Decimal – the monounsaturated fat content in grams (where available).
     trans_fat is a Decimal – the trans fat content in grams (where available).
     cholesterol is a Decimal – the cholesterol content in milligrams (where available).
     sodium is a Decimal – the sodium content in milligrams (where available).
     potassium is a Decimal – the potassium content in milligrams (where available).
     fiber is a Decimal – the fiber content in grams (where available).
     sugar is a Decimal – the sugar content in grams (where available).
     vitamin_a is a Decimal – the percentage of daily recommended Vitamin A, based on a 2000 calorie diet (where available).
     vitamin_c is a Decimal – the percentage of daily recommended Vitamin C, based on a 2000 calorie diet (where available).
     calcium is a Decimal – the percentage of daily recommended Calcium, based on a 2000 calorie diet (where available).
     iron is a Decimal – the percentage of daily recommended Iron, based on a 2000 calorie diet (where available).
      */
    /*
      Speech Input
       */
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mSearch.setText(result.get(0));
                searchFood(mSearch.getText().toString(), CURRENT_PAGE);
                mSearch.clearFocus();
            }
        }
    }


    /**
     * Interface to transfer data to MainActivity
     */



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
