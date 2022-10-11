package com.woody.woodytwit.infra.mail;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private List<String> to;

    private String subject;

    private String message;

}
