package dev.soumya.ProductServiceApr25.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soumya.ProductServiceApr25.dto.FakeStoreProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Component
public class FakeStoreClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public FakeStoreProductDTO[] getAllProducts(){
        String getAllProductsURL = "https://fakestoreapi.com/products";
        FakeStoreProductDTO[] response = restTemplate.getForObject(getAllProductsURL,
                                         FakeStoreProductDTO[].class);
        return response;
    }

//    public FakeStoreProductDTO getProduct(int productId) {
//        String getProductURL = "https://fakestoreapi.com/products/" + productId;
//        FakeStoreProductDTO response = restTemplate.getForObject(getProductURL,
//                                       FakeStoreProductDTO.class);
//        return response;
//    }

    /*
    implementing generic requestForObject method directly instead of using getForObject
    Object request can be null, @nullable
     */
    public FakeStoreProductDTO getProduct(int productId) {
        String key = "fakestore:product:" + productId;

        //Try fetching from Redis
        Object cachedObj = redisTemplate.opsForValue().get(key);
        if (cachedObj != null) {
            // Convert cached LinkedHashMap (or whatever) to FakeStoreProductDTO
            FakeStoreProductDTO cachedProduct = objectMapper.convertValue(cachedObj, FakeStoreProductDTO.class);
            return cachedProduct;
        }

        //Cache Miss → Fetch from FakeStore API
        String getProductURL = "https://fakestoreapi.com/products/" + productId;
        FakeStoreProductDTO response = requestForObject(
                getProductURL,
                HttpMethod.GET,
                null,
                FakeStoreProductDTO.class
        );

        //Save to Redis for future requests
        if (response != null) {
            redisTemplate.opsForValue().set(key, response, 10, TimeUnit.MINUTES);
        }

        return response;
    }


    public FakeStoreProductDTO createProduct(FakeStoreProductDTO input) {
        String createProductURL = "https://fakestoreapi.com/products";
        FakeStoreProductDTO response = restTemplate.postForObject(createProductURL,
                                       input, FakeStoreProductDTO.class);
        return response;
    }

//    public FakeStoreProductDTO replaceProduct(int productId, FakeStoreProductDTO input) {
//        String replaceProductURL = "https://fakestoreapi.com/products/" + productId;
//        FakeStoreProductDTO response = putForObject(replaceProductURL, input,
//                                       FakeStoreProductDTO.class);
//        return response;
//    }




    public FakeStoreProductDTO replaceProduct(int productId, FakeStoreProductDTO input) {
        String replaceProductURL = "https://fakestoreapi.com/products/" + productId;
        FakeStoreProductDTO response = requestForObject(replaceProductURL, HttpMethod.PUT,
                                       input, FakeStoreProductDTO.class);
        return response;
    }
    /*
    response type cannot be null
     */
    public Boolean deleteProductFromFakeStore(int productId) {
        String deleteProductURL = "https://fakestoreapi.com/products/" + productId;
        try {
            requestForObject(deleteProductURL, HttpMethod.DELETE, null,
                             FakeStoreProductDTO.class);
            return true;
        } catch (Exception exception) {
            return false;
        }

    }

//    /*
//    this method would not be called/used anywhere outside the class so we make it private
//    copied postForObject directly from RestTemplate.class and customised it to make it
//    work for put
//     */
//    private <T> T putForObject(String url, @Nullable Object request, Class<T> responseType,
//    Object... uriVariables) throws RestClientException {
//        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
//
//        HttpMessageConverterExtractor<T> responseExtractor =
//                new HttpMessageConverterExtractor(responseType, restTemplate.getMessageConverters());
//
//        return (T)restTemplate.execute(url, HttpMethod.PUT, requestCallback,
//                responseExtractor, uriVariables);
//    }


    /*
    creating a generic method which can be called for any type of mapping.
    We would not need RestTemplate then for each of the mappings, we can simply
    call this method.
    Now, HTTP method will depend on the function call, so we take HTTP method
    as an input
     */

    private <T> T requestForObject(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType,
                               Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);

        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor(responseType, restTemplate.getMessageConverters());

        return (T)restTemplate.execute(url, httpMethod, requestCallback,
                responseExtractor, uriVariables);
    }
}
