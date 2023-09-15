package api;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.JsonUtils;

import java.util.List;

public class UserObject {
    private JsonPath jsonPath;
    private long maleCount;
    private long femaleCount;

    public UserObject(Response response) {
        this.jsonPath = new JsonPath(response.getBody().asString());
        this.maleCount = getGenderList().stream().filter(g -> g.equalsIgnoreCase("male")).count();
        this.femaleCount = getGenderList().stream().filter(g -> g.equalsIgnoreCase("female")).count();
    }

    private List<String> getGenderList() {
        return this.jsonPath.getList("gender");
    }

    private List<String> getStatusList() {
        return this.jsonPath.getList("status");
    }

    private List<String> getEmailList() {
        return this.jsonPath.getList("email");
    }

    public long getMaleCount() {
        return this.maleCount;

    }

    public long getFemaleCount() {
        return this.femaleCount;
    }

    public long getDifference() {
        return Math.abs(getMaleCount() + (-getFemaleCount()));
    }

    public void validateGenderCountIsEqual() {
        long maleCount = getMaleCount();
        long femaleCount = getFemaleCount();
        long difference = Math.abs(maleCount - femaleCount);
        if (maleCount == femaleCount) {
            System.out.println("male and female counts are even");
        } else if (maleCount > femaleCount) {
            for (int i = 0; i < difference; i++) {
                GoRestApi.postUser(JsonUtils.getRandomUserObject(false));
            }
        } else if (maleCount < femaleCount) {
            for (int i = 0; i < difference; i++) {
                GoRestApi.postUser(JsonUtils.getRandomUserObject(true));
            }
        } else {
            throw new RuntimeException("none of the conditions were satisfied");
        }
    }

    public long getInactiveUsers() {
        return getStatusList().stream().filter(s -> s.equalsIgnoreCase("inactive")).count();
    }
}
