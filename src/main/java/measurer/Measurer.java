package measurer;

import api.ApiHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import requests.RequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

import static requests.Request.*;

public class Measurer {

    static ArrayList<String> issueDeletingList = new ArrayList<>();

    public static HashMap<Long, ArrayList<Long>> measureIssueCreating(long delay_mSec) {
        HashMap<Long, ArrayList<Long>> runsDataSet = new HashMap();
        ArrayList<Long> runsData = new ArrayList<>();
        ApiHandler apiHandler = new ApiHandler();

        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            CloseableHttpResponse response = apiHandler.execute(RequestBuilder.getHttpRequest(CREATE_NEW_ISSUE));
            long endTime = System.nanoTime();

            isStatusOk(response);
            String entityAsString = null;

            try {
                entityAsString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Logger.getLogger("main").info("\n<<<<< " + response.getStatusLine().toString() + "\n" + entityAsString);

            issueDeletingList.add(getIssueIdFromResponse(entityAsString));

            long duration = (endTime - startTime) / 1000000;
            runsData.add(duration);

            try {
                Thread.sleep(delay_mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        deleteIssuesWasCreated();
        runsDataSet.put(delay_mSec, runsData);
        return runsDataSet;
    }

    public static HashMap<Long, ArrayList<Long>> measureGettingIssueData(long delay_mSec) {
        HashMap<Long, ArrayList<Long>> runsDataSet = new HashMap();
        ArrayList<Long> runsData = new ArrayList<>();
        ApiHandler apiHandler = new ApiHandler();

        CloseableHttpResponse creatingIssueResponse = apiHandler.execute(RequestBuilder.getHttpRequest(CREATE_NEW_ISSUE));
        isStatusOk(creatingIssueResponse);

        String entityAsString = null;
        try {
            entityAsString = EntityUtils.toString(creatingIssueResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.getLogger("main").info("\n<<<<< " + creatingIssueResponse.getStatusLine().toString() + "\n" + entityAsString);

        assert entityAsString != null;

        issueDeletingList.add(getIssueIdFromResponse(entityAsString));
        GET_ISSUE_CONTENT.setParamFullUrl(String.format(getIssueIdFromResponse(entityAsString)));

        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            CloseableHttpResponse response = apiHandler.execute(RequestBuilder.getHttpRequest(GET_ISSUE_CONTENT));
            long endTime = System.nanoTime();

            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new IllegalStateException("Ответ на запрос вернулся со статусом " + status);
            }

            Logger.getLogger("main").info("\n<<<<< " + response.getStatusLine().toString());

            long duration = (endTime - startTime) / 1000000;
            runsData.add(duration);

            try {
                Thread.sleep(delay_mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        deleteIssuesWasCreated();
        runsDataSet.put(delay_mSec, runsData);
        return runsDataSet;
    }

    public static long get90Percentile(ArrayList<Long> range) {
        Collections.sort(range);
        return range.get(range.size() / 100 * 90 + 1);
    }

    private static String getIssueIdFromResponse(String entityAsString) {
        return entityAsString.replace("\"", "").split("id:")[1].split(",")[0];
    }

    private static boolean isStatusOk(CloseableHttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new IllegalStateException("Ответ на запрос вернулся со статусом " + status);
        }
        return true;
    }

    private static void deleteIssuesWasCreated() {
        ApiHandler apiHandler = new ApiHandler();

        issueDeletingList.forEach(id -> {
            DELETE_ISSUE.setParamFullUrl(id);
            CloseableHttpResponse deletingIssueResponse = apiHandler.execute(RequestBuilder.getHttpRequest(DELETE_ISSUE));
            Logger.getLogger("main").info("\n<<<<< " + deletingIssueResponse.getStatusLine().toString());
        });

        issueDeletingList.clear();
    }
}

