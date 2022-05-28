package account.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public abstract class Event {

    private String action;
    private String subject;
    private String object;
    private String path;

}
