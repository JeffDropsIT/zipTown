package com.example.developer.ziptown.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.example.developer.ziptown.models.forms.CreateOffer;
import com.example.developer.ziptown.models.forms.CreateRequest;
import com.example.developer.ziptown.models.forms.CreateUser;
import com.example.developer.ziptown.models.responses.GenericErrorResponse;
import com.example.developer.ziptown.models.forms.UserLogin;
import com.example.developer.ziptown.models.responses.GenericSuccessResponse;
import com.example.developer.ziptown.models.responses.UserSignInAndLoginResponse;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ServerRequest extends AsyncTask<Map<String, Object>, Void, Object >{
    private final String BASE_PATH = "http://18.188.245.160/ziptown";
    private OnTaskCompleted listener;

    public ServerRequest(OnTaskCompleted listener){
        this.listener = listener;

    }
    @Override
    protected Object doInBackground(Map<String, Object>... maps) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        Object response = chooseMethod(maps[0].get("type").toString(), maps[0], restTemplate);



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
            default:
                Log.i("WSX", "last chooseMethod: " + new GenericErrorResponse("Ops Something went wrong", 500).toString());
                return new GenericErrorResponse("Ops Something went wrong", 500);
        }
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
            sendResponseToActivity(response, "user");
            Log.i("WSX", "doInBackground: message: "+response.toString());
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
            sendResponseToActivity(response, "user");
            Log.i("WSX", "doInBackground: message: "+response.toString());
        }
        return response;
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
}
