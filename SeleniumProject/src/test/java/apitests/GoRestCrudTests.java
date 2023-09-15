package apitests;

import api.ApiFunctions;
import api.GoRestApi;
import api.UserObject;
import com.github.javafaker.Bool;
import com.squareup.okhttp.OkHttpClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.json.JSONArray;
import utils.JsonUtils;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GoRestCrudTests extends BaseTestApi {
    UserObject userObject;
    private String baseUrl = "https://gorest.co.in/public/v2";


    /**
     * takes the difference between the male and female - if one of the gender count is more than the other than post relevant gender by the difference number to get even
     */
    @Test(description = "retrieve all genders and count them and make male and female count even")
    public void tc01_getUsersAllGendersAndCreateUsersWithSpecificGender() throws InterruptedException {
        Response response = GoRestApi.getUsers();
        ArrayList<String> allGender = response.path("gender");
        System.out.println(allGender);
        userObject = new UserObject(response);
        userObject.validateGenderCountIsEqual();
        long maleCount = userObject.getMaleCount();
        long femaleCount = userObject.getFemaleCount();
        long countDifference = userObject.getDifference();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_CREATED);
        Assert.assertEquals(countDifference, 0);
        Assert.assertEquals(maleCount, femaleCount);
    }

    @Test(description = "delete all users that have inactive status")
    public void tc02_deleteInactiveUsers() throws InterruptedException {
        Response response = GoRestApi.deleteInActiveUsers();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        userObject = new UserObject(response);
        long inActiveUsers = userObject.getInactiveUsers();
        Assert.assertEquals(inActiveUsers, 0);
    }

    @Test(description = "modify all of the users email and modify their email extension to '.co.il' then assert all extensions changed to .co.il")
    public void tc03_updateUserEmailExtension() throws InterruptedException {
        Response response = GoRestApi.modifyEmailExtension();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
    }
}


