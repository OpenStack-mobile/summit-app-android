package org.openstack.android.summit.common;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.utils.HtmlTextParser;
@RunWith(AndroidJUnit4.class)
public class UtilTest {


    @Test
    public void htmlParserTest(){
        String input = "<p><span>Project Onboarding gives attendees a chance to meet some of the project team and get to know the project. Attendees will learn about the project itself, the code structure/ overall architecture, etc, and places where contribution is needed. Attendees will also get to know some of the core contributors and other established community members. Ideally, attendees will know/ have completed the basics of contribution (i.e. irc, gerrit, Launchpad, StoryBoard, Foundation Membership) BEFORE attending the session. All of this can be done through our Contributor Guide[1]. [1] https://docs.openstack.org/contributors/code-and-documentation/index.html</span></p>";
        String desiredOutput = "<p><span>Project Onboarding gives attendees a chance to meet some of the project team and get to know the project. Attendees will learn about the project itself, the code structure/ overall architecture, etc, and places where contribution is needed. Attendees will also get to know some of the core contributors and other established community members. Ideally, attendees will know/ have completed the basics of contribution (i.e. irc, gerrit, Launchpad, StoryBoard, Foundation Membership) BEFORE attending the session. All of this can be done through our Contributor Guide[1]. [1] <a href=\"https://docs.openstack.org/contributors/code-and-documentation/index.html\">https://docs.openstack.org/contributors/code-and-documentation/index.html</a></span></p>";
        String output = HtmlTextParser.convertLinksToAnchorTags(input);

        Assert.assertTrue(desiredOutput.contentEquals(output));
    }
}