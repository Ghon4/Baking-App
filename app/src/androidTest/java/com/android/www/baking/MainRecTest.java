package com.android.www.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.www.baking.Activities.MainActivity;
import com.android.www.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainRecTest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void scrollRecyclerView_checkRecipeNameDisplayed() {
        onView(withId(R.id.rv_main_recipe)).perform(RecyclerViewActions.scrollToPosition(Constants.POSITION_THREE));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));

    }

    @Test
    public void clickOnFirstItem_checkStepListIsDisplayed() {
        onView(withId(R.id.rv_main_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(Constants.FRIST_ITEM, click()));
        onView(withId(R.id.rv_recipe_steps)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnSecondItem_checkIngredientListIsDisplayed() {
        onView(withId(R.id.rv_main_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(Constants.SECOND_ITEM, click()));

        onView(withId(R.id.rv_recipe_ingredient)).check(matches(isDisplayed()));


    }




}
