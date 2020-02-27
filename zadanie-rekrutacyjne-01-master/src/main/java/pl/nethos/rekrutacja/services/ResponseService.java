package pl.nethos.rekrutacja.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.nethos.rekrutacja.models.EntityCheckResponse;
import pl.nethos.rekrutacja.models.Result;

import java.util.Objects;

@Service
public class ResponseService {

    //gets EntityCheckResponse from external api
    //and returns its result
    public Result getResult (String nip,String bankAccount){
        EntityCheckResponse entity =new RestTemplate().getForObject(
                "https://wl-api.mf.gov.pl/api/check/nip/"+nip+"/bank-account/"+bankAccount ,
                EntityCheckResponse.class
        );
        return Objects.requireNonNull(entity).getResult();
    }
}
