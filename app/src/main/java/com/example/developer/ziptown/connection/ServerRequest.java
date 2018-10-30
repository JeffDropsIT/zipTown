package com.example.developer.ziptown.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.example.developer.ziptown.models.CreateUser;
import com.example.developer.ziptown.models.GenericErrorResponse;
import com.example.developer.ziptown.models.UserLogin;
import com.example.developer.ziptown.models.UserSignInAndLoginResponse;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {
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
            Log.i("WSX", "doInBackground: message: "+responseError.toString());
        }else {
            Log.i("WSX", "doInBackground: message: "+response.toString());
        }
        return response;
    }
    private void createOffer(){

    }
    private void createRequest(){

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
        void onTaskFailed();
    }
}
