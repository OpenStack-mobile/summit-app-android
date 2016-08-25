package org.openstack.android.summit;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.modules.main.user_interface.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {
            };

    @Test
    public void openDrawerAndCloseDrawer() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void testTracks() {
        onView(withText("TRACKS")).perform(click());
        onView(withText("IT Strategy")).perform(click());
        onView(withId(R.id.list_schedule)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSchedule() {
        onView(withText("SCHEDULE")).perform(click());
        onView(withId(R.id.list_schedule)).check(matches(isDisplayed()));
    }

    @Test
    public void testLevels() {
        onView(withText("LEVELS")).perform(click());
        onView(withText("Beginner")).perform(click());
        onView(withId(R.id.list_schedule)).check(matches(isDisplayed()));
    }

    @Test
    public void clickEventShowsEventDetails() {
        onView(withText("SCHEDULE")).perform(click());
        onView(withText("Upstream University - Saturday & Sunday")).perform(click());
        onView(withText("JW Marriott Austin - Level 8 - Salon A")).check(matches(isDisplayed()));
    }

    @Test
    public void testYoutubeVideoPlayer() {
        onView(withText("SCHEDULE")).perform(click());
        onView(withText("Upstream University - Saturday & Sunday")).perform(click());
        onView(withId(R.id.video_preview)).perform(click());
    }

    @Test
    public void testFilter() {
        onView(withText("LEVELS")).perform(click());
        onView(withText("Beginner")).perform(click());
        onView(withId(R.id.list_schedule)).check(matches(isDisplayed()));
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.filter_tags_autocomplete)).perform(typeText("upstream"));

        onData(instanceOf(String.class)).inRoot(RootMatchers.withDecorView(
                not(is(mMainActivityTestRule.getActivity().getWindow().getDecorView()))))
                .atPosition(0)
                .perform(ViewActions.click());

        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText("CLEAR ACTIVE FILTERS")).perform(click());

    }

}