package williamho.com.contactlesspayment;

import williamho.com.contactlesspayment.models.ServerRequest;
import williamho.com.contactlesspayment.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by williamho on 12/08/16.
 */

public interface RequestInterface {

    @POST("contactlesspayment-services/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}