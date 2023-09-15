package api;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Utils;

import java.util.List;

public class GoRestApi {
    private static String baseUrl = "https://gorest.co.in/public/v2";
    private static String usersPath = "/users";

    public static Response getUsers() {
        return ApiFunctions.get(baseUrl + usersPath, ContentType.JSON);
    }

    public static Response postUser(String data) {
        return ApiFunctions.post(baseUrl + usersPath, data, ContentType.JSON, Utils.readProperty("apiToken"));
    }

    public static Response deleteInActiveUsers() {
        Response response = ApiFunctions.get(baseUrl + usersPath, ContentType.JSON);
        List<Integer> inactiveUsersId = response.path("findAll { it.status == 'inactive' }.id");
        if (inactiveUsersId.size() == 0) {
            return response;
        } else {
            Response deleteResponse = null;
            for (int id : inactiveUsersId) {
                deleteResponse = ApiFunctions.delete(baseUrl + usersPath + "/" + id, ContentType.JSON, Utils.readProperty("apiToken"));
            }
            return deleteResponse;
        }
    }

    public static Response modifyEmailExtension() {
        Response response = ApiFunctions.get(baseUrl + usersPath, ContentType.JSON);
        List<Integer> userId = response.path("findAll { it.email.endsWith('.co.il') == false }.id");
        Response patchResponse = null;
        if (userId.size() == 0) {
            return response;
        } else {
            for (int id : userId) {
                Response userResponse = ApiFunctions.get(baseUrl + usersPath + "/" + id, ContentType.JSON);
                String currentEmail = userResponse.path("email");
                String currentEmailSub = currentEmail.substring(0, currentEmail.lastIndexOf("@") + 1);
                String updatedEmail = modifyEmailExtension(currentEmail);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", currentEmailSub + updatedEmail);
                patchResponse = ApiFunctions.patch(baseUrl + usersPath + "/" + id, ContentType.JSON, jsonObject.toString(), Utils.readProperty("apiToken"));
            }
        }
        return patchResponse;
    }


    private static String modifyEmailExtension(String email) {
        String mailSplit = email.substring(email.lastIndexOf("@") + 1);
        String[] extensionSplit = mailSplit.split("\\.");
        return extensionSplit[0] + ".co.il";
    }

}
