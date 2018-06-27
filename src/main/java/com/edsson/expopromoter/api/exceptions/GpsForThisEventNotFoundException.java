package com.edsson.expopromoter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="GPS for event not exist exception!")  // 409
public class GpsForThisEventNotFoundException extends Exception{

public GpsForThisEventNotFoundException() {super("GPS for event not exist exception!");}



}
