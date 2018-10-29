package com.example.developer.ziptown.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.example.developer.ziptown.activities.MainActivity;
import com.example.developer.ziptown.models.CreateUser;
import com.example.developer.ziptown.models.CreateUserResponse;
import com.example.developer.ziptown.models.CreateUserResponseError;
import com.example.developer.ziptown.models.UserLogin;
import com.example.developer.ziptown.models.UserLoginResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

        if(maps[0].get("type").equals(new String("createUSer"))){
            return createUser(maps[0], restTemplate);
        }

        return null;
    }


    private Object createUser(Map<String, Object> map, RestTemplate restTemplate){

        CreateUser user = (CreateUser) map.get("createUser");
        Log.i("WSX", "name: "+user.getFullName());
        String url = BASE_PATH + user.getURL();
        Log.i("WSX", "URL: "+url);

        CreateUserResponse response = restTemplate.postForObject(url, user, CreateUserResponse.class);
        if (response.getMessage() == null){
            CreateUserResponseError responseError = restTemplate.postForObject(url, user, CreateUserResponseError.class);
            Log.i("WSX", "doInBackground: message: "+responseError.getError()+" response "+responseError.getResponse());
        }
        Log.i("WSX", "doInBackground: message: "+response.getMessage()+" response "+response.getResponse()+" data: "+response.getData());

        return response;
    }
    private Object userLogin(Map<String, Object> map, RestTemplate restTemplate){
        UserLogin user = (UserLogin) map.get("userLogin");
        Log.i("WSX", "name: "+user.getContact());
        String url = BASE_PATH + user.getURL();
        Log.i("WSX", "URL: "+url);

        UserLoginResponse response = restTemplate.postForObject(url, user, UserLoginResponse.class);
        if (response.getMessage() == null){
            CreateUserResponseError responseError = restTemplate.postForObject(url, user, CreateUserResponseError.class);
            Log.i("WSX", "doInBackground: message: "+responseError.getError()+" response "+responseError.getResponse());
        }
        Log.i("WSX", "doInBackground: message: "+response.getMessage()+" response "+response.getResponse()+" data: "+response.getData());

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
