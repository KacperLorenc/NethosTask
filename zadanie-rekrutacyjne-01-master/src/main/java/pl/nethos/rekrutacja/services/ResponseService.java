package pl.nethos.rekrutacja.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.nethos.rekrutacja.models.EntityCheckResponse;
import pl.nethos.rekrutacja.models.Result;

@Service
public class ResponseService {

    public Result getResult (String nip,String bankAccount){
        EntityCheckResponse entity =new RestTemplate().getForObject(
                "https://wl-test.mf.gov.pl/api/check/nip/"+nip+"/bank-account/"+bankAccount ,
                EntityCheckResponse.class
        );
        System.out.println(entity.getResult());
        return entity.getResult();
    }
}
