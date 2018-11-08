package com.example.developer.ziptown.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.developer.ziptown.activities.MainActivity;
import com.example.developer.ziptown.cache.ZipCache;
import com.example.developer.ziptown.models.forms.CreateCustomSearch;
import com.example.developer.ziptown.models.forms.CreateOffer;
import com.example.developer.ziptown.models.forms.CreateRequest;
import com.example.developer.ziptown.models.forms.CreateUser;
import com.example.developer.ziptown.models.forms.UpdateUser;
import com.example.developer.ziptown.models.mockerClasses.Offer;
import com.example.developer.ziptown.models.objectModels.Offers;
import com.example.developer.ziptown.models.responses.ContactVerificationSuccessResponse;
import com.example.developer.ziptown.models.responses.GenericErrorResponse;
import com.example.developer.ziptown.models.forms.UserLogin;
import com.example.developer.ziptown.models.responses.GenericSuccessResponse;
import com.example.developer.ziptown.models.responses.UserSignInAndLoginResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;
import static com.example.developer.ziptown.activities.LandingPageActivity.zipCache;

public class ServerRequest extends AsyncTask<Map<String, Object>, Void, Object >{
    public static final String WSX = "WSX";
    private final String BASE_PATH = "http://18.188.245.160/ziptown";
    private OnTaskCompleted listener;
    private Context context;
    private ProgressBar progressBar;

    public ServerRequest(OnTaskCompleted listener){
        this.listener = listener;

    }
    public ServerRequest(OnTaskCompleted listener, Context context){
        this.listener = listener;
        this.context = context;

    }


    public void setContext(Context context) {
        this.context = context;
    }



    @Override
    protected Object doInBackground(Map<String, Object>... maps) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Object response;
//        try {
            if(hasInternetAccess()){
                response = chooseMethod(maps[0].get("type").toString(), maps[0], restTemplate);
            }else {
                response = new GenericErrorResponse("Service Unavailable", 503);
                Log.i(WSX, "doInBackground: no internet: "+response.toString());
                listener.onTaskFailed();
            }

//        }catch (Exception e){
//            Log.i("WSX", "doInBackground: error on catch "+e.getMessage()+" stacktrace: "+e.getStackTrace());
//            response = new GenericErrorResponse("Service Unavailable", 503);
//            listener.onTaskFailed();
//        }
        return  response;
    }

    private Object chooseMethod(String type, Map<String, Object> map, RestTemplate restTemplate){

        Log.i("WSX", "chooseMethod: type: "+type);
        switch (type){
            case "CreateUser":
                Log.i("WSX", "1 chooseMethod: createUser");
                return createUser(map, restTemplate);
            case "UserLogin":
                Log.i("WSX", "2 chooseMethod: userLogin");
                return userLogin(map, restTemplate);
            case "CreateOffer":
                Log.i("WSX", "3 chooseMethod: CreateOffer");
                return createOffer(map, restTemplate);
            case "CreateRequest":
                Log.i("WSX", "4 chooseMethod: CreateRequest");
                return createRequest(map, restTemplate);
            case "GetPost":
                Log.i("WSX", "5 chooseMethod: GetPost");
                return getPost(map, restTemplate);
            case "CreateOfferSearch":
                Log.i("WSX", "5 chooseMethod: CreateOfferSearch");
                return createOfferSearch(map, restTemplate);
            case "CreateRequestSearch":
                Log.i("WSX", "5 chooseMethod: GetPost");
                return createRequestSearch(map, restTemplate);
            case "DeletePost":
                Log.i("WSX", "6 chooseMethod: DeletePost");
                deletePost(map, restTemplate);
                return new GenericSuccessResponse("success", 200);
            case "UpdateUser":
                Log.i("WSX", "6 chooseMethod: UpdateUser");

                return updateUser(map, restTemplate);
            case "VerifyContact":
                Log.i("WSX", "6 chooseMethod: VerifyContact");
                return verifyContact(map, restTemplate);
            case "GetUser":
                Log.i("WSX", "6 chooseMethod: GetUser");
                return getUser(restTemplate);
            default:
                Log.i("WSX", "last chooseMethod: " + new GenericErrorResponse("Ops Something went wrong", 500).toString());
                return new GenericErrorResponse("Ops Something went wrong", 500);
        }
    }

    private Object updateUser(Map<String, Object> map, RestTemplate restTemplate){
        UpdateUser user = (UpdateUser) map.get("model");
        String url = BASE_PATH + user.getURL();
        Log.i("WSX", "URL: "+url);

        GenericSuccessResponse response = restTemplate.postForObject(url, user, GenericSuccessResponse.class);
        if (response.getResponse() != 200){
            GenericErrorResponse responseError = restTemplate.postForObject(url, user, GenericErrorResponse.class);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {
            sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.toString());
        }
        return response;
    }
    private Object[] createOfferSearch(Map<String, Object> map, RestTemplate restTemplate){
        CreateCustomSearch search = (CreateCustomSearch) map.get("model");
        String url = BASE_PATH + "/app/search/offers";
        Log.i("WSX", "URL: "+url);

        Offers[] response = restTemplate.postForObject(url, search, Offers[].class);

        if (response.length == 0){
            sendResponseToActivity(new GenericSuccessResponse("No data Found", 404), "success");
            Log.i("WSX", "doInBackground: message: "+"error on getPost");
        }else {
            //sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.length);
            zipCache.clearTable(zipCache.OFFERS_SEARCH);
            for (int i = 0; i < response.length; i ++){
               zipCache.addOffersSearch(zipCache.toContentValues(response[i].ObjectToMap(response[i])));


            }
            sendResponseToActivity(response, "search");
        }
        return response;
    }
    private Object[] createRequestSearch(Map<String, Object> map, RestTemplate restTemplate){
        CreateCustomSearch search = (CreateCustomSearch) map.get("model");
        String url = BASE_PATH + "/app/search/requests";
        Log.i("WSX", "URL: "+url);

        Offers[] response = restTemplate.postForObject(url, search, Offers[].class);

        if (response.length == 0){
            sendResponseToActivity(new GenericSuccessResponse("No data Found", 404), "success");
            Log.i("WSX", "doInBackground: message: "+"error on getPost");
        }else {
            //sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.length);
            zipCache.clearTable(zipCache.OFFERS_SEARCH);
            for (int i = 0; i < response.length; i ++){
                zipCache.addRequestsSearch(zipCache.toContentValues(response[i].ObjectToMap(response[i])));

            }
            sendResponseToActivity(response, "search");
        }
        return response;

    }
    private Object verifyContact(Map<String, Object> map, RestTemplate restTemplate){

        String url = BASE_PATH + "/account/verify?contact="+map.get("contact").toString();
        Log.i("WSX", "URL: "+url);
        ContactVerificationSuccessResponse response = restTemplate.getForObject(url, ContactVerificationSuccessResponse.class);
        if(response.getCode() == null){
            GenericErrorResponse responseError = new GenericErrorResponse("error sending verification", 500);
            sendResponseToActivity(responseError, "error");
        }else {
            sendResponseToActivity(response, "code");
        }
        return response;
    }
    private void deletePost(Map<String, Object> map, RestTemplate restTemplate) {
        String url = BASE_PATH + "/account/user/"+map.get("postType").toString()+"/"+map.get("id").toString()+"/";
        Log.i("WSX", "URL: "+url);
        restTemplate.delete(url);
    }
    private Object getUser(RestTemplate restTemplate){

        String url = BASE_PATH + "/account/user/"+ MainActivity.getString("userId");
        Log.i("WSX", "URL: "+url);

        UserSignInAndLoginResponse response = restTemplate.getForObject(url, UserSignInAndLoginResponse.class);
        Log.i("WSX", "createUser: user: "+response.getUser());
        if (response.getUser() == null){
            GenericErrorResponse responseError = new GenericErrorResponse("failed to get user", 404);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {

            cacheUserData(response);
            cacheUserOffers(response);
            cacheUserRequests(response);
            sendResponseToActivity(response, "user");
            Log.i("WSX", "doInBackground: message: "+response.ObjectToMap(response));
        }


        return response;
    }
    private Object createUser(Map<String, Object> map, RestTemplate restTemplate){

        CreateUser user = (CreateUser) map.get("model");
        Log.i("WSX", "name: "+user.getFullName());
        String url = BASE_PATH + user.getURL();
        Log.i("WSX", "URL: "+url);

        UserSignInAndLoginResponse response = restTemplate.postForObject(url, user, UserSignInAndLoginResponse.class);
        Log.i("WSX", "createUser: user: "+response.getUser());
        if (response.getUser() == null){
            GenericErrorResponse responseError = restTemplate.postForObject(url, user, GenericErrorResponse.class);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {

            cacheUserData(response);
            cacheUserOffers(response);
            cacheUserRequests(response);
            sendResponseToActivity(response, "user");
            Log.i("WSX", "doInBackground: message: "+response.ObjectToMap(response));
        }


        return response;
    }
    private Object userLogin(Map<String, Object> map, RestTemplate restTemplate){
        UserLogin user = (UserLogin) map.get("model");
        Log.i("WSX", "name: "+user.getContact());
        String url = BASE_PATH + user.getURL();
        Log.i("WSX", "URL: "+url);

        UserSignInAndLoginResponse response = restTemplate.postForObject(url, user, UserSignInAndLoginResponse.class);
        if (response.getUser() == null){
            GenericErrorResponse responseError = restTemplate.postForObject(url, user, GenericErrorResponse.class);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {

            cacheUserData(response);
            cacheUserOffers(response);
            cacheUserRequests(response);
            sendResponseToActivity(response, "user");



        }
        return response;
    }

    private void cacheUserData(UserSignInAndLoginResponse response) {
        zipCache.clearTable(ZipCache.USER_DATA);
        zipCache.addUserData(zipCache.toContentValues(response.getUserMap()));
    }

    private void cacheUserOffers(UserSignInAndLoginResponse response) {
        zipCache.clearTable(ZipCache.USER_OFFERS);
        Map<String, Object> offersMap = response.getOffersMap();
        ArrayList<Integer> idList = response.getOffersIdSet();

        for(int i = 0; i < idList.size(); i++){
            Log.i("WSX", "userLogin: offers keyset "+response.ObjectToMap(offersMap.get(idList.get(i).toString())));
            zipCache.addUserOffers(zipCache.toContentValues(response.ObjectToMap(offersMap.get(idList.get(i).toString()))));
        }

    }
    private void cacheUserRequests(UserSignInAndLoginResponse response) {
        zipCache.clearTable(ZipCache.USER_REQUESTS);
        Map<String, Object> requestsMap = response.getRequestsMap();
        ArrayList<Integer> idList = response.getRequestIdSet();

        for(int i = 0; i < idList.size(); i++){
            Log.i("WSX", "userLogin: requests keyset "+response.ObjectToMap(requestsMap.get(idList.get(i).toString())));
            zipCache.addUserRequests(zipCache.toContentValues(response.ObjectToMap(requestsMap.get(idList.get(i).toString()))));
        }

    }

    private Object createOffer(Map<String, Object> map, RestTemplate restTemplate){
        CreateOffer offer = (CreateOffer) map.get("model");
        Log.i("WSX", "name: "+offer.getContact());
        String url = BASE_PATH + offer.getURL();
        Log.i("WSX", "URL: "+url);

        GenericSuccessResponse response = restTemplate.postForObject(url, offer, GenericSuccessResponse.class);
        if (response.getResponse() != 200){
            GenericErrorResponse responseError = restTemplate.postForObject(url, offer, GenericErrorResponse.class);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {
            sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.toString());
        }
        return response;
    }
    private Object createRequest(Map<String, Object> map, RestTemplate restTemplate){
        CreateRequest req = (CreateRequest) map.get("model");
        Log.i("WSX", "name: "+req.getContact());
        String url = BASE_PATH + req.getURL();
        Log.i("WSX", "URL: "+url);

        GenericSuccessResponse response = restTemplate.postForObject(url, req, GenericSuccessResponse.class);
        if (response.getResponse() != 200){
            GenericErrorResponse responseError = restTemplate.postForObject(url, req, GenericErrorResponse.class);
            sendResponseToActivity(responseError, "error");
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {
            sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.toString());
        }
        return response;
    }
    private Object[] getPost(Map<String, Object> map, RestTemplate restTemplate){

        String url = BASE_PATH + "/app/"+map.get("postType").toString()+"?city="+map.get("city").toString().replace(" ", ",").split(",")[0];
        Log.i("WSX", "URL: "+url);
        Offers[] response = restTemplate.getForObject(url, Offers[].class);
        if (response.length == 0){
            String url2 = BASE_PATH + "/app/"+map.get("postType").toString()+"?city=p&fallback=true";
            Offers[] responseAll = restTemplate.getForObject(url2, Offers[].class);

            if(responseAll.length == 0){
                sendResponseToActivity(new GenericErrorResponse("No data Found", 404), "error");
                Log.i("WSX", "doInBackground: message: "+"error on getPost");
            }else {
                if(map.get("postType").toString().equals("offers")){
                    zipCache.clearTable(ZipCache.OFFERS);
                }else {
                    zipCache.clearTable(ZipCache.REQUESTS);
                }
                for (int i = 0; i < responseAll.length; i ++){
                    Log.i("WSX", "type "+map.get("postType").toString()+" getPost: "+responseAll[i].ObjectToMap(responseAll[i]));
                    if(map.get("postType").toString().equals("offers")){
                        zipCache.addOffers(zipCache.toContentValues(responseAll[i].ObjectToMap(responseAll[i])));
                    }else {
                        zipCache.addRequests(zipCache.toContentValues(responseAll[i].ObjectToMap(responseAll[i])));
                    }

                }
            }

        }else {
            //sendResponseToActivity(response, "success");
            Log.i("WSX", "doInBackground: message: "+response.length);
            if(map.get("postType").toString().equals("offers")){
                zipCache.clearTable(ZipCache.OFFERS);
            }else {
                zipCache.clearTable(ZipCache.REQUESTS);
            }
            for (int i = 0; i < response.length; i ++){
                Log.i("WSX", "type "+map.get("postType").toString()+" getPost: "+response[i].ObjectToMap(response[i]));
                if(map.get("postType").toString().equals("offers")){
                    zipCache.addOffers(zipCache.toContentValues(response[i].ObjectToMap(response[i])));
                }else {
                    zipCache.addRequests(zipCache.toContentValues(response[i].ObjectToMap(response[i])));
                }

            }
        }
        return response;

    }
    private void sendResponseToActivity(Object object, String resType){
        Map<String, Object> map = new HashMap<>();
        map.put("object", object);
        map.put("response", resType);
        if(!isOnTaskListenerNull()){
            listener.onDataFetched(map);
        }
    }
    @Override
    protected void onPostExecute(Object o) {
        if(!isOnTaskListenerNull()){
            listener.onTaskCompleted();
        }
    }

    private boolean isOnTaskListenerNull(){
        return listener == null;
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
        void onDataFetched(Map<String, Object> object);
        void onTaskFailed();
    }

    private boolean hasInternetAccess(){
        boolean connection = false;
        try {
            if (isNetworkAvailable()) {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("https://www.google.com/") //ping google
                                .openConnection());
                urlc.setConnectTimeout(10000);
                urlc.connect();
                connection = (urlc.getResponseCode() == 200);
                return connection;
            } else {

                return connection;
            }


        } catch (IOException e) {
            Log.e("WSX", "Error checking internet connection", e);
            return connection;
        }

    }
}
