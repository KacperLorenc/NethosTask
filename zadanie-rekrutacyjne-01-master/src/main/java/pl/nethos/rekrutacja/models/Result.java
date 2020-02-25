
package pl.nethos.rekrutacja.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accountAssigned",
    "requestDateTime",
    "requestId"
})
public class Result {

    @JsonProperty("accountAssigned")
    private String accountAssigned;
    @JsonProperty("requestDateTime")
    private String requestDateTime;
    @JsonProperty("requestId")
    private String requestId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accountAssigned")
    public String getAccountAssigned() {
        return accountAssigned;
    }

    @JsonProperty("accountAssigned")
    public void setAccountAssigned(String accountAssigned) {
        this.accountAssigned = accountAssigned;
    }

    @JsonProperty("requestDateTime")
    public String getRequestDateTime() {
        return requestDateTime;
    }

    @JsonProperty("requestDateTime")
    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Result{" +
                "accountAssigned='" + accountAssigned + '\'' +
                ", requestDateTime='" + requestDateTime + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
