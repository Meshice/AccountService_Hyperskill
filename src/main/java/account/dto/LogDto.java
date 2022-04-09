package account.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"date", "action", "subject", "object", "path"})
@AllArgsConstructor
@NoArgsConstructor
public class LogDto implements DtoMarker {

    private String date;
    private String action;
    private String subject;
    private String object;
    private String path;

}
